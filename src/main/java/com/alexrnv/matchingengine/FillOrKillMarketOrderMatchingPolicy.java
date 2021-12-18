package com.alexrnv.matchingengine;

public class FillOrKillMarketOrderMatchingPolicy extends MatchingPolicy {

    private final FillOrKillMarketOrder orderToFill;

    public FillOrKillMarketOrderMatchingPolicy(FillOrKillMarketOrder orderToFill) {
        this.orderToFill = orderToFill;
    }

    @Override
    protected boolean shouldStopMatching() {
        return orderToFill.isEnoughCapacity();
    }

    /**
     * Since there is no immediate matching, we always assume that
     * book order could be filled (otherwise shouldStopMatching() will break the cycle).
     */
    protected boolean shouldMoveToNextBookOrder(BookOrder bookOrder) {
        return true;
    }

    @Override
    protected void afterWholeBookProcessed(Book book) {
        if (orderToFill.isEnoughCapacity()) {
            trades.forEach(Trade::execute);
            cleanBook(book);
        } else {
            orderToFill.resetCapacity();
            trades.clear();
        }
    }

    private void cleanBook(Book book) {
        Book.BookIterator iterator = getBookIterator(book);
        while (iterator.hasNext()) {
            BookOrder bookOrder = iterator.next();
            if (bookOrder.isFilled()) {
                iterator.remove();
            }
        }
    }

    @Override
    protected Book.BookIterator getBookIterator(Book book) {
        if(orderToFill.getSide() == Side.ASK) {
            return book.matchingBids(Integer.MIN_VALUE);
        } else {
            return book.matchingAsks(Integer.MAX_VALUE);
        }
    }

    @Override
    protected void afterTradeCreated(Trade trade) {
        orderToFill.reserveCapacity(trade.getVolume());
    }

    @Override
    protected Trade tryToMatch(BookOrder bookOrder) {
        //lock open or partially filled order for processing, if it is not locked by other operation, or filled
        if (bookOrder.tryMarkForProcessing()) {
            return createTrade(bookOrder, orderToFill);
        }
        return null;
    }

    private Trade createTrade(BookOrder bookOrder, FillOrKillMarketOrder order) {
        int volume = Math.min(orderToFill.getQuantity() - orderToFill.reservedCapacity(), bookOrder.getQuantity());
        int price = bookOrder.getPrice();
        if (bookOrder.getSide() == Side.BID) {
            return new Trade(bookOrder, order, price, volume);
        } else {
            assert order.getSide() == Side.BID;
            return new Trade(order, bookOrder, price, volume);
        }
    }

}
