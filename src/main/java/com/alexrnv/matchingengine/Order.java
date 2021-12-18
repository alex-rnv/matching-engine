package com.alexrnv.matchingengine;

import java.util.List;
import java.util.Objects;

/**
 * Abstract Order interface with mandatory shared fields.
 * All implementing classes are required to override
 *     @Override
 *     public List<Trade> match(OrderMatcher orderMatcher) {
 *         return orderMatcher.matchOrder(this);
 *     }
 * to implement double-dispatch logic.
 * Order matching policy is defined by OrderMatcher based in runtime order implementation.
 */
public abstract class Order {

    protected final String id;
    protected final Side side;
    protected int quantity;
    protected final long timestamp;

    public Order(String id, Side side, int quantity) {
        this.id = Objects.requireNonNull(id);
        this.side = Objects.requireNonNull(side);
        this.quantity = quantity;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public Side getSide() {
        return side;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public abstract List<Trade> match(OrderMatcher orderMatcher);

    /**
     * @param quantity - volume to fill
     * @return remaining quantity.
     * Positive return value means that this order has some unfilled volume remained.
     * Negative return value means that not the whole quantity is filled by this order.
     * In both cases the remainder is returned.
     */
    protected int fill(int quantity) {
        int remainingOrdQty = this.quantity - quantity;
        this.quantity = Math.max(remainingOrdQty, 0);
        return remainingOrdQty;
    }

    public boolean isFilled() {
        return quantity == 0;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", side=" + side +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}
