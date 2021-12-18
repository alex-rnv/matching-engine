package com.alexrnv.matchingengine;

import java.util.List;

/**
 *
 */
public class MarketOrder extends Order {
    public MarketOrder(String id, Side side, int quantity) {
        super(id, side, quantity);
    }

    @Override
    public List<Trade> match(OrderMatcher orderMatcher) {
        return orderMatcher.matchOrder(this);
    }

    @Override
    public String toString() {
        return "MarketOrder{" +
                "id='" + id + '\'' +
                ", side=" + side +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}
