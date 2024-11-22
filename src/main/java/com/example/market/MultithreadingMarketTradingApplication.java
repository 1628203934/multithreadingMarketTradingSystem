package com.example.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MultithreadingMarketTradingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MultithreadingMarketTradingApplication.class, args);
    }
}
