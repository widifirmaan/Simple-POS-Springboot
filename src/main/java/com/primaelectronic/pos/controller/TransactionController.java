package com.primaelectronic.pos.controller;

import com.primaelectronic.pos.model.Transaction;
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
    private com.primaelectronic.pos.service.TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/store/{storeId}")
    public List<Transaction> getTransactionsByStore(@PathVariable int storeId) {
        return transactionService.getTransactionsByStore(storeId);
    }

    @GetMapping("/filter")
    public List<Transaction> getTransactionsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return transactionService.getTransactionsByDate(start, end);
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable long id, @RequestBody Transaction details) {
        Transaction updatedTransaction = transactionService.updateTransaction(id, details);
        if (updatedTransaction != null) {
            return ResponseEntity.ok(updatedTransaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/process")
    public ResponseEntity<String> processTransaction(
            @RequestBody com.primaelectronic.pos.payload.TransactionRequest request,
            jakarta.servlet.http.HttpSession session) {
        try {
            String username = (String) session.getAttribute("username");
            Integer toko = (Integer) session.getAttribute("toko");
            transactionService.processTransaction(request, username, toko);
            return ResponseEntity.ok("Transaksi Berhasil");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing transaction: " + e.getMessage());
        }
    }
}
