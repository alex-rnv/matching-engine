package com.alexrnv.matchingengine;

import java.util.Iterator;

public interface Book {
    void add(BookOrder order);
    void remove(BookOrder order);

    BookIterator matchingBids(Integer price);
    BookIterator matchingAsks(Integer price);

    interface BookIterator extends Iterator<BookOrder> { }
}
