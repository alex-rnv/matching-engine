package com.alexrnv.matchingengine;

import java.util.*;

/**
 * Reference implementation of the order book; stores order tables according to price-time priority.
 */
public class PriceTimePriorityBook implements Book {

    /**
     * Implementation detail. Orders are stored in sorted maps by price (ascending for asks, and descending for
     * bids). Each entry contains a bucket - a sorted by timestamp (earliest to latest) list of orders with the same
     * price.
     */

    private final TreeMap<Integer, List<BookOrder>> asks = new TreeMap<>();
    private final TreeMap<Integer, List<BookOrder>> bids = new TreeMap<>(Comparator.reverseOrder());

    @Override
    public void remove(BookOrder order) {
        if (order.getSide() == Side.ASK) {
            asks.getOrDefault(order.price, Collections.emptyList()).remove(order);
        } else {
            bids.getOrDefault(order.price, Collections.emptyList()).remove(order);
        }
    }

    @Override
    public void add(BookOrder order) {
        if (order.getSide() == Side.ASK) {
            asks.computeIfAbsent(order.price, k -> new ArrayList<>()).add(order);
        } else {
            bids.computeIfAbsent(order.price, k -> new ArrayList<>()).add(order);
        }
    }

    public BookIterator matchingBids(Integer startPrice) {
        return new PriceTimeIterator(bids.headMap(startPrice, true));
    }

    public BookIterator matchingAsks(Integer startPrice) {
        return new PriceTimeIterator(asks.headMap(startPrice, true));
    }

    @Override
    public String toString() {
        BookIterator bidsIterator = matchingBids(Integer.MIN_VALUE);
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("BIDS:");
        while (bidsIterator.hasNext()) {
            joiner.add(bidsIterator.next().toString());
        }

        BookIterator asksIterator = matchingAsks(Integer.MAX_VALUE);
        joiner.add("ASKS:");
        while (asksIterator.hasNext()) {
            joiner.add(asksIterator.next().toString());
        }

        return joiner.toString();
    }

    public static class PriceTimeIterator implements BookIterator {

        private final NavigableMap<Integer, List<BookOrder>> priceTimeSubTree;
        private final Iterator<Integer> priceIterator;
        private Iterator<BookOrder> orderIterator;

        PriceTimeIterator(NavigableMap<Integer, List<BookOrder>> priceTimeSubTree) {
            this.priceTimeSubTree = priceTimeSubTree;
            this.priceIterator = priceTimeSubTree.navigableKeySet().iterator();
        }

        @Override
        public boolean hasNext() {
            if (orderIterator != null) {
                if (orderIterator.hasNext())
                    return true;
            }
            while(priceIterator.hasNext()) {
                List<BookOrder> priceOrders = priceTimeSubTree.get(priceIterator.next());
                if (priceOrders.isEmpty()) {
                    //cleanup empty buckets
                    priceIterator.remove();
                } else {
                    orderIterator = priceOrders.iterator();
                    if (orderIterator.hasNext())
                        return true;
                }
            }
            return false;
        }

        @Override
        public BookOrder next() {
            return orderIterator.next();
        }

        @Override
        public void remove() {
            if (orderIterator != null) {
                orderIterator.remove();
            }
        }
    }

}
