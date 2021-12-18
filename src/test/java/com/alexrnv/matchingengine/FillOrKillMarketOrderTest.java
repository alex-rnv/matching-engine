package com.alexrnv.matchingengine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FillOrKillMarketOrderTest {

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
    public void testSell_OrderFilled() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        FillOrKillMarketOrder sell = new FillOrKillMarketOrder("3001", Side.ASK, 200);
        List<Trade> trades = sell.match(orderMatcher);
        assertEquals(3, trades.size());
        Utils.assertTrade(trades.get(0), "1001", "3001", 99, 50);
        Utils.assertTrade(trades.get(1), "1002", "3001", 98, 100);
        Utils.assertTrade(trades.get(2), "1003", "3001", 98, 50);
    }

    @Test
    public void testSell_OrderNotFilled() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        FillOrKillMarketOrder sell = new FillOrKillMarketOrder("3001", Side.ASK, 400);
        List<Trade> trades = sell.match(orderMatcher);
        assertEquals(0, trades.size());
    }

    @Test
    public void testBuy_OrderFilled() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        FillOrKillMarketOrder buy = new FillOrKillMarketOrder("3001", Side.BID, 200);
        List<Trade> trades = buy.match(orderMatcher);
        assertEquals(2, trades.size());
        Utils.assertTrade(trades.get(0), "3001", "2001", 100, 50);
        Utils.assertTrade(trades.get(1), "3001", "2002", 100, 150);
    }

    @Test
    public void testBuy_OrderNotFilled() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        FillOrKillMarketOrder buy = new FillOrKillMarketOrder("3001", Side.BID, 2000);
        List<Trade> trades = buy.match(orderMatcher);
        assertEquals(0, trades.size());
    }

}