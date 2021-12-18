## Matching Engine Demo
A showcase of how matching engine for different order types could be implemented with minimum code duplication.    
Orders supported:    
- market order 
- limit order
- fill-or-kill market order
- iceberg order

 A reference implementation of price-time priority order book is implemented.    
 Matching policies (strategy pattern) are implemented with a single template method approach.
 Double dispatch via visitor pattern is used to apply policies based on realtime order type.
 
Example usage:
```java
        Book book = new PriceTimePriorityBook();
        OrderMatcher orderMatcher = new DefaultBookOrderMatcher(book);
        LimitOrder limitSell = new LimitOrder("3004", Side.ASK, 100, 700);
        limitSell.match(orderMatcher); //book is empty, limit order lands in the book
        MarketOrder marketBuy = new MarketOrder("3002", Side.BID, 5);
        List<Trade> trades = marketBuy.match(orderMatcher); //one trade is emitted at price 100, volume 5
```

