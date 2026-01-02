package com.primaelectronic.pos.service;

import com.primaelectronic.pos.model.Stock;
import java.util.List;
import java.util.Optional;

public interface StockService {
    List<Stock> getAllStocks();

    List<Stock> getStocksByStore(int storeId);

    Optional<Stock> getStockById(long id);

    Stock createStock(Stock stock);

    Stock updateStock(long id, Stock stockDetails);

    void deleteStock(long id);
}
