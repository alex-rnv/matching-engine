package com.alexrnv.matchingengine;

import java.util.List;

public class IcebergOrder extends LimitOrder {

    private final int visibleQuantity;
    private int hiddenQuantity;

    public IcebergOrder(String id, Side side, int price, int visibleQuantity, int totalQuantity) {
        super(id, side, price, visibleQuantity);
        assert totalQuantity >= visibleQuantity;
        this.visibleQuantity = visibleQuantity;
        this.hiddenQuantity = totalQuantity;
    }

    @Override
    protected int fill(int quantity) {
        int remaining = super.fill(quantity);
        if (remaining == 0) {
            showMore();
        }
        return remaining;
    }

    private void showMore() {
        if (hiddenQuantity > 0) {
            if (hiddenQuantity < visibleQuantity) {
                quantity = hiddenQuantity;
                hiddenQuantity = 0;
            } else {
                quantity = visibleQuantity;
                hiddenQuantity -= visibleQuantity;
            }
            state.set(State.PARTIALLY_FILLED);
        }
    }

    @Override
    public List<Trade> match(OrderMatcher orderMatcher) {
        return orderMatcher.matchOrder(this);
    }

    @Override
    public String toString() {
        return "IcebergOrder{" +
                "price=" + price +
                ", state=" + state +
                ", visibleQuantity=" + visibleQuantity +
                ", hiddenQuantity=" + hiddenQuantity +
                ", id='" + id + '\'' +
                ", side=" + side +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}
