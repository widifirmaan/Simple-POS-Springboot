package com.primaelectronic.pos.service;

import com.primaelectronic.pos.model.Transaction;
import com.primaelectronic.pos.payload.TransactionRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();

    List<Transaction> getTransactionsByStore(int storeId);

    List<Transaction> getTransactionsByDate(LocalDateTime start, LocalDateTime end);

    Transaction createTransaction(Transaction transaction);

    Transaction updateTransaction(long id, Transaction details);

    void deleteTransaction(long id);

    void processTransaction(TransactionRequest request, String username, Integer toko) throws Exception;
}
