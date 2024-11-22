package com.example.market.controller;

import com.example.market.model.Stock;
import com.example.market.service.MarketService;
import com.example.market.service.TraderService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService marketService;
    private final TraderService traderService;

    public MarketController(MarketService marketService, TraderService traderService) {
        this.marketService = marketService;
        this.traderService = traderService;
    }

    // Add a new stock
    @PostMapping("/stocks")
    public String addStock(@RequestBody Stock stock) {
        marketService.addStock(stock);
        return "Stock added: " + stock.getSymbol();
    }

    // Get all stocks
    @GetMapping("/stocks")
    public Map<String, Stock> getAllStocks() {
        return marketService.getAllStocks();
    }

    // Start a trader
    @PostMapping("/traders/{name}")
    public String startTrader(@PathVariable String name) {
        traderService.simulateTrader(name);
        return "Trader " + name + " started.";
    }
}
