package com.alexrnv.matchingengine;

import java.util.List;

/**
 * Matches different orders against the shared book, using matching policies defined by the runtime order implementation.
 */
public class DefaultBookOrderMatcher implements OrderMatcher {

    private final Book book;

    public DefaultBookOrderMatcher(Book book) {
        this.book = book;
    }

    @Override
    public List<Trade> matchOrder(MarketOrder marketOrder) {
        MarketOrderMatchingPolicy policy = new MarketOrderMatchingPolicy(marketOrder);
        policy.matchAgainstBookOrders(book);
        return policy.getTrades();
    }

    @Override
    public List<Trade> matchOrder(LimitOrder limitOrder) {
        LimitOrderMatchingPolicy policy = new LimitOrderMatchingPolicy(limitOrder);
        policy.matchAgainstBookOrders(book);
        return policy.getTrades();
    }

    @Override
    public List<Trade> matchOrder(IcebergOrder icebergOrder) {
        LimitOrderMatchingPolicy policy = new LimitOrderMatchingPolicy(icebergOrder);
        policy.matchAgainstBookOrders(book);
        return policy.getTrades();
    }

    @Override
    public List<Trade> matchOrder(FillOrKillMarketOrder fillOrKillMarketOrder) {
        FillOrKillMarketOrderMatchingPolicy policy = new FillOrKillMarketOrderMatchingPolicy(fillOrKillMarketOrder);
        policy.matchAgainstBookOrders(book);
        return policy.getTrades();
    }
}
