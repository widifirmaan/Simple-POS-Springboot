package com.primaelectronic.pos.repository;

import com.primaelectronic.pos.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, Long> {
    List<Transaction> findByToko(int toko);

    List<Transaction> findByDateBetween(LocalDateTime start, LocalDateTime end);

    List<Transaction> findByTokoAndDateBetween(int toko, LocalDateTime start, LocalDateTime end);

}
