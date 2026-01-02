package com.primaelectronic.pos.service;

import com.primaelectronic.pos.model.Stock;
import com.primaelectronic.pos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public List<Stock> getStocksByStore(int storeId) {
        return stockRepository.findByStore(storeId);
    }

    @Override
    public Optional<Stock> getStockById(long id) {
        return stockRepository.findById(id);
    }

    @Override
    public Stock createStock(Stock stock) {
        stock.setId(sequenceGeneratorService.generateSequence(Stock.SEQUENCE_NAME));
        stock.setCreatedAt(LocalDateTime.now());
        stock.setModifiedAt(LocalDateTime.now());
        return stockRepository.save(stock);
    }

    @Override
    public Stock updateStock(long id, Stock stockDetails) {
        return stockRepository.findById(id).map(stock -> {
            stock.setName(stockDetails.getName());
            stock.setQty(stockDetails.getQty());
            stock.setPriceInit(stockDetails.getPriceInit());
            stock.setPriceFinal(stockDetails.getPriceFinal());
            stock.setStore(stockDetails.getStore());
            stock.setModifiedAt(LocalDateTime.now());
            // stock.setModifiedBy("system"); // Assuming system for now
            return stockRepository.save(stock);
        }).orElse(null);
    }

    @Override
    public void deleteStock(long id) {
        stockRepository.deleteById(id);
    }
}
