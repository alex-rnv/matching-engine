package com.alexrnv.matchingengine;

import static org.junit.jupiter.api.Assertions.*;

public class Utils {

    static void assertTrade(Trade toAssert, String bidOrderID, String askOrderID, int price, int volume) {
        assertEquals(bidOrderID, toAssert.getBid().getId());
        assertEquals(askOrderID, toAssert.getAsk().getId());
        assertEquals(price, toAssert.getPrice());
        assertEquals(volume, toAssert.getVolume());
    }

}
