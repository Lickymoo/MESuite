package com.buoobuoo.mesuite.metrade.p2p;

import com.buoobuoo.mesuite.metrade.METradePlugin;
import com.buoobuoo.mesuite.metrade.p2p.inventory.impl.P2PTradeInstance;

import java.util.HashSet;
import java.util.Set;

public class P2PTradeManager {

    private METradePlugin plugin;
    private Set<P2PTradeInstance> tradeInstances = new HashSet<>();

    public P2PTradeManager(METradePlugin plugin){
        this.plugin = plugin;
    }

}

