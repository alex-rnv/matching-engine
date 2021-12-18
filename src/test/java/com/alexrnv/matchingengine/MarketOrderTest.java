package com.alexrnv.matchingengine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MarketOrderTest {

    private static Book testBook() {
        Book book = new PriceTimePriorityBook();

        book.add(new LimitOrder("1001", Side.BID, 99, 50));
        book.add(new LimitOrder("1002", Side.BID, 98, 100));
        book.add(new LimitOrder("1003", Side.BID, 98, 200));
        book.add(new LimitOrder("1004", Side.BID, 97, 10));

        book.add(new LimitOrder("2001", Side.ASK, 100, 50));
        book.add(new LimitOrder("2002", Side.ASK, 100, 1000));
        book.add(new LimitOrder("2003", Side.ASK, 103, 100));
        book.add(new LimitOrder("2004", Side.ASK, 105, 200));
        return book;
    }

    @Test
    public void testSell_MatchOneBookOrder() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        MarketOrder marketSell = new MarketOrder("3001", Side.ASK, 5);
        List<Trade> trades = marketSell.match(orderMatcher);
        assertEquals(1, trades.size());
        Utils.assertTrade(trades.get(0), "1001", "3001", 99, 5);
    }

    @Test
    public void testSell_MatchTwoBookOrders() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        MarketOrder marketSell = new MarketOrder("3001", Side.ASK, 100);
        List<Trade> trades = marketSell.match(orderMatcher);
        assertEquals(2, trades.size());
        Utils.assertTrade(trades.get(0), "1001", "3001", 99, 50);
        Utils.assertTrade(trades.get(1), "1002", "3001", 98, 50);
    }

    @Test
    public void testSell_MatchWholeBook() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        MarketOrder marketSell = new MarketOrder("3001", Side.ASK, 500);
        List<Trade> trades = marketSell.match(orderMatcher);
        assertEquals(4, trades.size());
        Utils.assertTrade(trades.get(0), "1001", "3001", 99, 50);
        Utils.assertTrade(trades.get(1), "1002", "3001", 98, 100);
        Utils.assertTrade(trades.get(2), "1003", "3001", 98, 200);
        Utils.assertTrade(trades.get(3), "1004", "3001", 97, 10);
        //assert unfilled
        assertEquals(140, marketSell.getQuantity());
    }

    @Test
    public void testBuy_MatchOneBookOrder() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        MarketOrder marketBuy = new MarketOrder("3002", Side.BID, 5);
        List<Trade>  trades = marketBuy.match(orderMatcher);
        assertEquals(1, trades.size());
        Utils.assertTrade(trades.get(0), "3002", "2001", 100, 5);
    }

    @Test
    public void testBuy_MatchThreeBookOrders() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        MarketOrder marketBuy = new MarketOrder("3002", Side.BID, 1100);
        List<Trade>  trades = marketBuy.match(orderMatcher);
        assertEquals(3, trades.size());
        Utils.assertTrade(trades.get(0), "3002", "2001", 100, 50);
        Utils.assertTrade(trades.get(1), "3002", "2002", 100, 1000);
        Utils.assertTrade(trades.get(2), "3002", "2003", 103, 50);
    }

    @Test
    public void testBuy_MatchWholeBook() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        MarketOrder marketBuy = new MarketOrder("3002", Side.BID, 1400);
        List<Trade>  trades = marketBuy.match(orderMatcher);
        assertEquals(4, trades.size());
        Utils.assertTrade(trades.get(0), "3002", "2001", 100, 50);
        Utils.assertTrade(trades.get(1), "3002", "2002", 100, 1000);
        Utils.assertTrade(trades.get(2), "3002", "2003", 103, 100);
        Utils.assertTrade(trades.get(3), "3002", "2004", 105, 200);
        //assert unfilled part
        assertEquals(50, marketBuy.getQuantity());
    }

}