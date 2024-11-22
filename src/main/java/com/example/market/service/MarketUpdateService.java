package com.example.market.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MarketUpdateService {

    private final MarketService marketService;

    public MarketUpdateService(MarketService marketService) {
        this.marketService = marketService;
    }

    @Scheduled(fixedRate = 5000) // Update every 5 seconds
    public void updateStockPrices() {
        marketService.getAllStocks().values().forEach(stock -> {
            double fluctuation = ThreadLocalRandom.current().nextDouble(-0.5, 0.5); // Random price change
            stock.getPrice().addAndGet(fluctuation);
        });

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("Market prices updated " + currentDateTime.format(formatter));
    }
}
