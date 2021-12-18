package com.alexrnv.matchingengine;

import java.util.List;

/**
 * Visitor to implement double-dispatch logic.
 */
public interface OrderMatcher {
    List<Trade> matchOrder(MarketOrder marketOrder);

    List<Trade> matchOrder(LimitOrder limitOrder);

    List<Trade> matchOrder(IcebergOrder icebergOrder);

    List<Trade> matchOrder(FillOrKillMarketOrder fillOrKillMarketOrder);

}
