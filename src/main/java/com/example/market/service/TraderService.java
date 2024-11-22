package com.example.market.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TraderService {

    private final MarketService marketService;
    private final Random random = new Random();

    public TraderService(MarketService marketService) {
        this.marketService = marketService;
    }

    @Async
    public void simulateTrader(String traderName) {
        while (true) {
            // Get all available stock symbols
            String[] stockSymbols = marketService.getAllStocks().keySet().toArray(new String[0]);
            // Randomly select a stock
            if (stockSymbols.length == 0) {
                continue;
            }
            String symbol = stockSymbols[random.nextInt(stockSymbols.length)];
            int amount = random.nextInt(10) + 1; // Random amount between 1 and 10
            boolean isBuy = random.nextBoolean(); // Randomly decide to buy or sell

            // Perform the trade
            if (isBuy) {
                if (marketService.trade(symbol, amount, true)) {
                    System.out.printf("%s bought %d shares of %s%n", traderName, amount, symbol);
                }
            } else {
                if (marketService.trade(symbol, amount, false)) {
                    System.out.printf("%s sold %d shares of %s%n", traderName, amount, symbol);
                }
            }

            // Simulate a delay between trades
            try {
                Thread.sleep(random.nextInt(1000)); // Delay between 0-1000 ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
