package com.alexrnv.matchingengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Base matching policy for all type of orders.
 * Implemented via the template method.
 */
public abstract class MatchingPolicy {

    /**
     * Trades originated during matching.
     */
    protected final List<Trade> trades = new ArrayList<>();

    /**
     * Traverses the book in the order defined by its {@link Book.BookIterator}.
     */
    public void matchAgainstBookOrders(Book book) {
        Book.BookIterator iterator = getBookIterator(book);
        BookOrder bookOrder = null;
        boolean moveToNext = true;
        while (iterator.hasNext() || !moveToNext) {
            if (bookOrder == null || moveToNext) {
                bookOrder = iterator.next();
            }
            matchBookOrder(bookOrder);

            if (bookOrder.isFilled())
                iterator.remove();

            if (shouldStopMatching())
                break;

            moveToNext = shouldMoveToNextBookOrder(bookOrder);
        }

        afterWholeBookProcessed(book);
    }

    /**
     * Checks if the book order was filled in the recent trade.
     * The logic of this method accounts for specific book orders (e.g.: Iceberg),
     * where filling may be completed in several trades.
     */
    protected boolean shouldMoveToNextBookOrder(BookOrder bookOrder) {
        return bookOrder.isFilled();
    }

    /**
     * @return true if book traversal has to be stopped (e.g.: input order is filled or cancelled)
     */
    protected abstract boolean shouldStopMatching();

    /**
     * Callback after processing the whole book.
     */
    protected abstract void afterWholeBookProcessed(Book book);

    protected abstract Book.BookIterator getBookIterator(Book book);

    private void matchBookOrder(BookOrder bookOrder) {
        Trade trade = tryToMatch(bookOrder);
        if (trade != null) {
            this.trades.add(trade);
            afterTradeCreated(trade);
        }
    }

    protected void afterTradeCreated(Trade trade) {
        trade.execute();
    }

    /**
     * Matches book order against input order.
     * @return trade or null, if not trade is possible
     */
    protected abstract Trade tryToMatch(BookOrder bookOrder);

    public List<Trade> getTrades() {
        return trades;
    }
}
