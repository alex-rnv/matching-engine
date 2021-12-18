package com.alexrnv.matchingengine;

import com.alexrnv.matchingengine.Book.BookIterator;

class MarketOrderMatchingPolicy extends MatchingPolicy {

    protected final MarketOrder orderToFill;

    MarketOrderMatchingPolicy(MarketOrder orderToFill) {
        this.orderToFill = orderToFill;
    }

    @Override
    protected boolean shouldStopMatching() {
        return orderToFill.isFilled();
    }

    @Override
    protected void afterWholeBookProcessed(Book book) {}

    @Override
    protected BookIterator getBookIterator(Book book) {
        if(orderToFill.getSide() == Side.ASK) {
            return book.matchingBids(Integer.MIN_VALUE);
        } else {
            return book.matchingAsks(Integer.MAX_VALUE);
        }
    }

    @Override
    protected Trade tryToMatch(BookOrder bookOrder) {
        //lock open or partially filled order for processing, if it is not locked by other operation, or filled
        if (bookOrder.tryMarkForProcessing()) {
            return createTrade(bookOrder, orderToFill);
        }
        return null;
    }

    private Trade createTrade(BookOrder bookOrder, MarketOrder order) {
        int volume = Math.min(orderToFill.getQuantity(), bookOrder.getQuantity());
        int price = bookOrder.getPrice();
        if (bookOrder.getSide() == Side.BID) {
            return new Trade(bookOrder, order, price, volume);
        } else {
            assert order.getSide() == Side.BID;
            return new Trade(order, bookOrder, price, volume);
        }
    }

    public MarketOrder getOrderToFill() {
        return orderToFill;
    }

}
