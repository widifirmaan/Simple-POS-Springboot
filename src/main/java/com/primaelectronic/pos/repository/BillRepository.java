package com.primaelectronic.pos.repository;

import com.primaelectronic.pos.model.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends MongoRepository<Bill, Long> {
    List<Bill> findByStatus(int status);
}
