package com.alexrnv.matchingengine;

public enum Side {
    BID('B'),
    ASK('S');

    private final char symbol;

    Side(char symbol) {
        this.symbol = symbol;
    }
}
