package com.alexrnv.matchingengine;

public class Trade {
    private final Order bid;
    private final Order ask;
    private final int price;
    private final int volume;

    public Trade(Order bid, Order ask, int price, int volume) {
        this.bid = bid;
        this.ask = ask;
        this.price = price;
        this.volume = volume;
    }

    public void execute() {
        bid.fill(volume);
        ask.fill(volume);
    }

    public Order getBid() {
        return bid;
    }

    public Order getAsk() {
        return ask;
    }

    public int getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "bid=" + bid +
                ", ask=" + ask +
                ", price=" + price +
                ", volume=" + volume +
                '}';
    }

}
