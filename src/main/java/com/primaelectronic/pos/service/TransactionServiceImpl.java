package com.primaelectronic.pos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primaelectronic.pos.model.Invoice;
import com.primaelectronic.pos.model.Stock;
import com.primaelectronic.pos.model.Transaction;
import com.primaelectronic.pos.payload.TransactionItemDTO;
import com.primaelectronic.pos.payload.TransactionRequest;
import com.primaelectronic.pos.repository.InvoiceRepository;
import com.primaelectronic.pos.repository.StockRepository;
import com.primaelectronic.pos.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionsByStore(int storeId) {
        return transactionRepository.findByToko(storeId);
    }

    @Override
    public List<Transaction> getTransactionsByDate(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByDateBetween(start, end);
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        transaction.setId(sequenceGeneratorService.generateSequence(Transaction.SEQUENCE_NAME));
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateTransaction(long id, Transaction details) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setAmount(details.getAmount());
            transaction.setProduct(details.getProduct());
            transaction.setToko(details.getToko());
            transaction.setPayment(details.getPayment());
            return transactionRepository.save(transaction);
        }).orElse(null);
    }

    @Override
    public void deleteTransaction(long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void processTransaction(TransactionRequest request, String username, Integer toko) throws Exception {
        if (username == null)
            username = "system";
        if (toko == null)
            toko = 1;

        // 1. Save Invoice
        Invoice invoice = new Invoice();
        invoice.setDatabarang(new ObjectMapper().writeValueAsString(request.getDataBarang()));
        invoice.setNota(request.getDataHTML());
        invoice.setMoney(request.getDataTrans());
        invoice.setToko(toko);
        invoice.setTgl(LocalDateTime.now());
        invoice.setUsername(username);
        invoice.setPayment(request.getDataPayment());
        invoiceRepository.save(invoice);

        // 2. Process Items & Update Stock
        StringBuilder productString = new StringBuilder();

        for (TransactionItemDTO item : request.getDataBarang()) {
            long stockId;
            try {
                stockId = Long.parseLong(item.getIdbarang());
            } catch (NumberFormatException e) {
                continue;
            }

            Stock stock = stockRepository.findById(stockId).orElse(null);

            if (stock != null) {
                productString.append(stock.getName()).append(" (").append(item.getQty()).append("), ");
                int currentQty = stock.getQty();
                int buyQty = item.getQty();
                stock.setQty(currentQty - buyQty);
                stock.setModifiedAt(LocalDateTime.now());
                stockRepository.save(stock);
            } else {
                productString.append("ID:").append(stockId).append(" (").append(item.getQty()).append("), ");
            }
        }

        // 3. Save Transaction
        Transaction transaction = new Transaction();
        transaction.setId(sequenceGeneratorService.generateSequence(Transaction.SEQUENCE_NAME));
        transaction.setDate(LocalDateTime.now());
        transaction.setAmount((int) request.getDataTrans());
        transaction.setProduct(productString.toString());
        transaction.setUserId(username);
        transaction.setToko(toko);
        transaction.setPayment(request.getDataPayment());
        transactionRepository.save(transaction);
    }
}
