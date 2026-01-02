package com.primaelectronic.pos.repository;

import com.primaelectronic.pos.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends MongoRepository<Stock, Long> {
    List<Stock> findByStore(int store);
}
