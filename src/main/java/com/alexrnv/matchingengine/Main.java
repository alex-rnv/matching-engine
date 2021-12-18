package com.alexrnv.matchingengine;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Book book = new PriceTimePriorityBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);

        book.add(new LimitOrder("1001", Side.BID, 99, 50));
        book.add(new LimitOrder("1002", Side.BID, 98, 100));
        book.add(new LimitOrder("1003", Side.BID, 98, 200));
        book.add(new LimitOrder("1004", Side.BID, 97, 10));

        book.add(new LimitOrder("2001", Side.ASK, 100, 50));
        book.add(new LimitOrder("2002", Side.ASK, 100, 1000));
        book.add(new LimitOrder("2003", Side.ASK, 103, 100));
        book.add(new LimitOrder("2004", Side.ASK, 105, 200));

        MarketOrder marketSell = new MarketOrder("3001", Side.ASK, 5);
        List<Trade> trades = marketSell.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();

        MarketOrder marketBuy = new MarketOrder("3002", Side.BID, 5);
        trades = marketBuy.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();

        LimitOrder limitBuy = new LimitOrder("3003", Side.BID, 101, 2000);
        trades = limitBuy.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();

        LimitOrder limitSell = new LimitOrder("3004", Side.ASK, 98, 700);
        trades = limitSell.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();

        limitSell = new LimitOrder("3005", Side.ASK, 98, 700);
        trades = limitSell.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();

        limitSell = new LimitOrder("3006", Side.ASK, 99, 300);
        trades = limitSell.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();

        limitBuy = new LimitOrder("3007", Side.BID, 96, 100);
        trades = limitBuy.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();

        limitBuy = new LimitOrder("3008", Side.BID, 98, 100);
        trades = limitBuy.match(orderMatcher);
        System.out.println(trades);
        System.out.println(book);
        System.out.println();
    }
}
