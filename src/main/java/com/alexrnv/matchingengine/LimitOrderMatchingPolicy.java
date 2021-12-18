package com.alexrnv.matchingengine;

import com.alexrnv.matchingengine.Book.BookIterator;

class LimitOrderMatchingPolicy extends MatchingPolicy {

    private final LimitOrder orderToFill;

    LimitOrderMatchingPolicy(LimitOrder orderToFill) {
        this.orderToFill = orderToFill;
    }

    @Override
    protected boolean shouldStopMatching() {
        return orderToFill.isFilled();
    }

    @Override
    protected void afterWholeBookProcessed(Book book) {
        if (orderToFill.state.get() != State.FILLED) {
            book.add(orderToFill);
        }
    }

    @Override
    protected BookIterator getBookIterator(Book book) {
        if(orderToFill.getSide() == Side.ASK) {
            return book.matchingBids(orderToFill.getPrice());
        } else {
            return book.matchingAsks(orderToFill.getPrice());
        }
    }

    @Override
    protected void afterTradeCreated(Trade trade) {
        trade.execute();
    }

    @Override
    protected Trade tryToMatch(BookOrder bookOrder) {
        //lock open order for processing, if it is not locked by other operation, or filled
        if (bookOrder.tryMarkForProcessing()) {
            return createTrade(bookOrder, orderToFill);
        }
        return null;
    }

    private Trade createTrade(BookOrder bookOrder, LimitOrder order) {
        int volume = Math.min(orderToFill.getQuantity(), bookOrder.getQuantity());
        if (bookOrder.getSide() == Side.BID) {
            assert order.getSide() == Side.ASK;
            return new Trade(bookOrder, order, Math.max(order.getPrice(), bookOrder.getPrice()), volume);
        } else {
            assert order.getSide() == Side.BID;
            return new Trade(order, bookOrder, Math.min(order.getPrice(), bookOrder.getPrice()), volume);
        }
    }

    public LimitOrder getOrderToFill() {
        return orderToFill;
    }

}
