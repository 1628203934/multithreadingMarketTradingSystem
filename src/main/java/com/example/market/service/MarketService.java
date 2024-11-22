package com.example.market.service;

import com.example.market.model.Stock;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MarketService {

    private final Map<String, Stock> stocks = new ConcurrentHashMap<>();

    // Add a new stock to the market
    public void addStock(Stock stock) {
        stocks.put(stock.getSymbol(), stock);
    }

    // Get a specific stock by symbol
    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    // Get all stocks in the market
    public Map<String, Stock> getAllStocks() {
        return stocks;
    }

    // Handle trade operations (buy or sell)
    public boolean trade(String symbol, int amount, boolean isBuy) {
        Stock stock = stocks.get(symbol);
        if (stock == null) {
            return false;
        }
        return isBuy ? stock.buy(amount) : stock.sell(amount);
    }
}
