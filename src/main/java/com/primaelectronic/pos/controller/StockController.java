package com.primaelectronic.pos.controller;

import com.primaelectronic.pos.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private com.primaelectronic.pos.service.StockService stockService;

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/store/{storeId}")
    public List<Stock> getStocksByStore(@PathVariable int storeId) {
        return stockService.getStocksByStore(storeId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable long id) {
        return stockService.getStockById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Stock createStock(@RequestBody Stock stock) {
        return stockService.createStock(stock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable long id, @RequestBody Stock stockDetails) {
        Stock updatedStock = stockService.updateStock(id, stockDetails);
        if (updatedStock != null) {
            return ResponseEntity.ok(updatedStock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable long id) {
        stockService.deleteStock(id);
        return ResponseEntity.ok().build();
    }
}
