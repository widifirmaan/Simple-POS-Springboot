package com.primaelectronic.pos.controller;

import com.primaelectronic.pos.model.Stock;
import com.primaelectronic.pos.repository.StockRepository;
import com.primaelectronic.pos.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @GetMapping("/store/{storeId}")
    public List<Stock> getStocksByStore(@PathVariable int storeId) {
        return stockRepository.findByStore(storeId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable long id) {
        return stockRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Stock createStock(@RequestBody Stock stock) {
        stock.setId(sequenceGeneratorService.generateSequence(Stock.SEQUENCE_NAME));
        stock.setCreatedAt(LocalDateTime.now());
        stock.setModifiedAt(LocalDateTime.now());
        return stockRepository.save(stock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable long id, @RequestBody Stock stockDetails) {
        return stockRepository.findById(id)
                .map(stock -> {
                    stock.setName(stockDetails.getName());
                    stock.setQty(stockDetails.getQty());
                    stock.setPriceInit(stockDetails.getPriceInit());
                    stock.setPriceFinal(stockDetails.getPriceFinal());
                    stock.setStore(stockDetails.getStore());
                    stock.setModifiedAt(LocalDateTime.now());
                    stock.setModifiedBy("system"); // Or authenticate user
                    return ResponseEntity.ok(stockRepository.save(stock));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable long id) {
        return stockRepository.findById(id)
                .map(stock -> {
                    stockRepository.delete(stock);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
