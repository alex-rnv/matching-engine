package com.alexrnv.matchingengine;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Parent class for all book orders.
 * No expiration date is configured. Order remains in the book until filled.
 */
public abstract class BookOrder extends Order {

    protected int price;
    protected AtomicReference<State> state = new AtomicReference<>(State.OPEN);

    public BookOrder(String id, Side side, int quantity, int price) {
        super(id, side, quantity);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    protected boolean tryMarkForProcessing() {
        State updated = this.state.updateAndGet(cur -> {
            if (cur == State.OPEN || cur == State.PARTIALLY_FILLED) {
                return State.IN_PROGRESS;
            }
            return cur;
        });
        return updated == State.IN_PROGRESS;
    }

    protected void cancelProcessing() {
        assert this.state.get() == State.IN_PROGRESS;
        this.state.compareAndSet(State.IN_PROGRESS, State.OPEN);
    }

    protected int fill(int quantity) {
        int remaining = super.fill(quantity);
        this.state.set(remaining > 0 ? State.PARTIALLY_FILLED : State.FILLED);
        return remaining;
    }

    @Override
    public boolean isFilled() {
        if (super.isFilled()) {
            assert state.get() == State.FILLED;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "BookOrder{" +
                "id='" + id +
                ", state=" + state +
                ", side=" + side +
                ", price=" + price +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}
