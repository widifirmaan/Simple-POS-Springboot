package com.primaelectronic.pos.controller;

import com.primaelectronic.pos.model.Transaction;
import com.primaelectronic.pos.repository.TransactionRepository;
import com.primaelectronic.pos.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @GetMapping("/store/{storeId}")
    public List<Transaction> getTransactionsByStore(@PathVariable int storeId) {
        return transactionRepository.findByToko(storeId);
    }

    @GetMapping("/filter")
    public List<Transaction> getTransactionsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return transactionRepository.findByDateBetween(start, end);
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        transaction.setId(sequenceGeneratorService.generateSequence(Transaction.SEQUENCE_NAME));
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }
        return transactionRepository.save(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable long id, @RequestBody Transaction details) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transaction.setAmount(details.getAmount());
                    transaction.setProduct(details.getProduct());
                    transaction.setToko(details.getToko());
                    transaction.setPayment(details.getPayment());
                    // Update other fields if necessary
                    return ResponseEntity.ok(transactionRepository.save(transaction));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable long id) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transactionRepository.delete(transaction);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Autowired
    private com.primaelectronic.pos.repository.StockRepository stockRepository;

    @Autowired
    private com.primaelectronic.pos.repository.InvoiceRepository invoiceRepository;

    @PostMapping("/process")
    public ResponseEntity<String> processTransaction(
            @RequestBody com.primaelectronic.pos.payload.TransactionRequest request,
            jakarta.servlet.http.HttpSession session) {
        try {
            String username = (String) session.getAttribute("username");
            Integer toko = (Integer) session.getAttribute("toko");

            if (username == null || toko == null) {
                // For API testing safety, maybe default or error
                // return ResponseEntity.status(401).body("Unauthorized");
                // PHP code assumed session exists.
                if (username == null)
                    username = "system"; // fallback
                if (toko == null)
                    toko = 1; // fallback
            }

            // 1. Save Invoice
            com.primaelectronic.pos.model.Invoice invoice = new com.primaelectronic.pos.model.Invoice();
            invoice.setDatabarang(
                    new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(request.getDataBarang()));
            invoice.setNota(request.getDataHTML());
            invoice.setMoney(request.getDataTrans());
            invoice.setToko(toko);
            invoice.setTgl(LocalDateTime.now());
            invoice.setUsername(username);
            invoice.setPayment(request.getDataPayment());
            invoiceRepository.save(invoice);

            // 2. Process Items & Update Stock
            StringBuilder productString = new StringBuilder();

            for (com.primaelectronic.pos.payload.TransactionItemDTO item : request.getDataBarang()) {
                long stockId;
                try {
                    stockId = Long.parseLong(item.getIdbarang());
                } catch (NumberFormatException e) {
                    continue; // Skip invalid IDs
                }

                com.primaelectronic.pos.model.Stock stock = stockRepository.findById(stockId).orElse(null);

                if (stock != null) {
                    productString.append(stock.getName()).append(" (").append(item.getQty()).append("), ");

                    int currentQty = stock.getQty();
                    int buyQty = item.getQty();
                    stock.setQty(currentQty - buyQty);
                    stock.setModifiedAt(LocalDateTime.now());
                    stockRepository.save(stock);
                } else {
                    productString.append("ID:" + stockId).append(" (").append(item.getQty()).append("), ");
                }
            }

            // 3. Save Transaction
            Transaction transaction = new Transaction();
            transaction.setId(sequenceGeneratorService.generateSequence(Transaction.SEQUENCE_NAME));
            transaction.setDate(LocalDateTime.now());
            transaction.setAmount((int) request.getDataTrans()); // Transaction model uses int for amount
            transaction.setProduct(productString.toString());
            transaction.setUserId(username);
            transaction.setToko(toko);
            transaction.setPayment(request.getDataPayment());
            transactionRepository.save(transaction);

            return ResponseEntity.ok("Transaksi Berhasil");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing transaction: " + e.getMessage());
        }
    }
}
