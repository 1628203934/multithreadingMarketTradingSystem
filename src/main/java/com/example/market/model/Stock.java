package com.example.market.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;
import com.google.common.util.concurrent.AtomicDouble;

@Data
public class Stock {
    private String symbol;
    private AtomicDouble price;
    private AtomicInteger quantity;

    public Stock(String symbol, double initialPrice, int initialQuantity) {
        this.symbol = symbol;
        this.price = new AtomicDouble(initialPrice);
        this.quantity = new AtomicInteger(initialQuantity);
    }

    public synchronized boolean buy(int amount) {
        if (quantity.get() >= amount) {
            quantity.addAndGet(-amount);
            price.addAndGet(amount * 0.01); // Increase price slightly
            return true;
        }
        return false;
    }

    public synchronized boolean sell(int amount) {
        quantity.addAndGet(amount);
        price.addAndGet(-amount * 0.01); // Decrease price slightly
        return true;
    }
}
