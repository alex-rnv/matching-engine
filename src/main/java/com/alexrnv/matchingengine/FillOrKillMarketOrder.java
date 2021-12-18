package com.alexrnv.matchingengine;

import java.util.List;

public class FillOrKillMarketOrder extends MarketOrder {

    private int fillCapacity;

    public FillOrKillMarketOrder(String id, Side side, int quantity) {
        super(id, side, quantity);
    }

    @Override
    public List<Trade> match(OrderMatcher orderMatcher) {
        return orderMatcher.matchOrder(this);
    }

    @Override
    protected int fill(int quantity) {
        resetCapacity();
        return super.fill(quantity);
    }

    protected void reserveCapacity(int cap) {
        fillCapacity += cap;
    }

    protected int reservedCapacity() {
        return fillCapacity;
    }

    protected boolean isEnoughCapacity() {
        return fillCapacity == quantity;
    }

    protected void resetCapacity() {
        fillCapacity = 0;
    }

    @Override
    public boolean isFilled() {
        return super.isFilled();
    }

    @Override
    public String toString() {
        return "FillOrKillMarketOrder{" +
                "fillCapacity=" + fillCapacity +
                ", id='" + id + '\'' +
                ", side=" + side +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}
