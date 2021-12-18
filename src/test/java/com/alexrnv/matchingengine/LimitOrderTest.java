package com.alexrnv.matchingengine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LimitOrderTest {

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
    public void testSell_NoMatch() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        LimitOrder limitSell = new LimitOrder("3004", Side.ASK, 100, 700);
        List<Trade> trades = limitSell.match(orderMatcher);
        assertEquals(0, trades.size());
    }

    @Test
    public void testSell_MatchesOneBookOrder() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        LimitOrder limitSell = new LimitOrder("3004", Side.ASK, 99, 30);
        List<Trade> trades = limitSell.match(orderMatcher);
        assertEquals(1, trades.size());
        Utils.assertTrade(trades.get(0), "1001", "3004", 99, 30);
    }

    @Test
    public void testSell_MatchesThreeBookOrders() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        LimitOrder limitSell = new LimitOrder("3004", Side.ASK, 98, 300);
        List<Trade> trades = limitSell.match(orderMatcher);
        assertEquals(3, trades.size());
        Utils.assertTrade(trades.get(0), "1001", "3004", 99, 50);
        Utils.assertTrade(trades.get(1), "1002", "3004", 98, 100);
        Utils.assertTrade(trades.get(2), "1003", "3004", 98, 150);
    }

    @Test
    public void testBuy_NoMatch() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        LimitOrder limitBuy = new LimitOrder("3003", Side.BID, 99, 2000);
        List<Trade> trades = limitBuy.match(orderMatcher);
        assertEquals(0, trades.size());
    }

    @Test
    public void testBuy_MatchesOneBookOrder() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        LimitOrder limitBuy = new LimitOrder("3003", Side.BID, 100, 40);
        List<Trade> trades = limitBuy.match(orderMatcher);
        assertEquals(1, trades.size());
        Utils.assertTrade(trades.get(0), "3003", "2001", 100, 40);
    }

    @Test
    public void testBuy_MatchesTwoBookOrders() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        LimitOrder limitBuy = new LimitOrder("3003", Side.BID, 101, 500);
        List<Trade> trades = limitBuy.match(orderMatcher);
        assertEquals(2, trades.size());
        Utils.assertTrade(trades.get(0), "3003", "2001", 100, 50);
        Utils.assertTrade(trades.get(1), "3003", "2002", 100, 450);
    }

}