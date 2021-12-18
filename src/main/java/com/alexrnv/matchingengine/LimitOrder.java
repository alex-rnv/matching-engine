package com.alexrnv.matchingengine;

import java.util.List;

public class LimitOrder extends BookOrder {
    public LimitOrder(String id, Side side, int price, int quantity) {
        super(id, side, quantity, price);
    }

    @Override
    public List<Trade> match(OrderMatcher orderMatcher) {
        return orderMatcher.matchOrder(this);
    }

    @Override
    public String toString() {
        return "LimitOrder{" +
                "price=" + price +
                ", state=" + state +
                ", id='" + id + '\'' +
                ", side=" + side +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}
