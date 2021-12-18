package com.alexrnv.matchingengine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTest {

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
    public void testCustomSequence() {
        Book book = testBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        MarketOrder marketSell = new MarketOrder("3001", Side.ASK, 5);
        List<Trade> trades = marketSell.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(1, trades.size());
        Utils.assertTrade(trades.get(0), "1001", "3001", 99, 5);

        MarketOrder marketBuy = new MarketOrder("3002", Side.BID, 5);
        trades = marketBuy.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(1, trades.size());
        Utils.assertTrade(trades.get(0), "3002", "2001", 100, 5);

        LimitOrder limitBuy = new LimitOrder("3003", Side.BID, 101, 2000);
        trades = limitBuy.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(2, trades.size());
        Utils.assertTrade(trades.get(0), "3003", "2001", 100, 45);
        Utils.assertTrade(trades.get(1), "3003", "2002", 100, 1000);

        LimitOrder limitSell = new LimitOrder("3004", Side.ASK, 98, 700);
        trades = limitSell.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(1, trades.size());
        Utils.assertTrade(trades.get(0), "3003", "3004", 101, 700);

        limitSell = new LimitOrder("3005", Side.ASK, 98, 700);
        trades = limitSell.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(4, trades.size());
        Utils.assertTrade(trades.get(0), "3003", "3005", 101, 255);
        Utils.assertTrade(trades.get(1), "1001", "3005", 99, 45);
        Utils.assertTrade(trades.get(2), "1002", "3005", 98, 100);
        Utils.assertTrade(trades.get(3), "1003", "3005", 98, 200);

        limitSell = new LimitOrder("3006", Side.ASK, 99, 300);
        trades = limitSell.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(0, trades.size());

        limitBuy = new LimitOrder("3007", Side.BID, 96, 100);
        trades = limitBuy.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(0, trades.size());

        limitBuy = new LimitOrder("3008", Side.BID, 98, 100);
        trades = limitBuy.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(1, trades.size());
        Utils.assertTrade(trades.get(0), "3008", "3005", 98, 100);

        FillOrKillMarketOrder fokBuy = new FillOrKillMarketOrder("3009", Side.BID, 2);
        trades = fokBuy.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(1, trades.size());
        Utils.assertTrade(trades.get(0), "3009", "3006", 99, 2);

        fokBuy = new FillOrKillMarketOrder("3010", Side.BID, 1000);
        trades = fokBuy.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(0, trades.size());

        IcebergOrder icebergOrder = new IcebergOrder("3011", Side.BID, 98, 100, 500);
        trades = icebergOrder.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(0, trades.size());

        limitSell = new LimitOrder("3012", Side.ASK, 98, 100);
        trades = limitSell.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(1, trades.size());
        Utils.assertTrade(trades.get(0), "3011", "3012", 98, 100);

        limitSell = new LimitOrder("3013", Side.ASK, 98, 400);
        trades = limitSell.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
        assertEquals(4, trades.size());
        Utils.assertTrade(trades.get(0), "3011", "3013", 98, 100);
        Utils.assertTrade(trades.get(1), "3011", "3013", 98, 100);
        Utils.assertTrade(trades.get(2), "3011", "3013", 98, 100);
        Utils.assertTrade(trades.get(3), "3011", "3013", 98, 100);

    }

}