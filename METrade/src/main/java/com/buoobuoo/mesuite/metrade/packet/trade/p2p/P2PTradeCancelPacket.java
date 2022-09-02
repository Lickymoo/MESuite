package com.buoobuoo.mesuite.metrade.packet.trade.p2p;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.metrade.p2p.inventory.impl.P2PTradeInstance;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class P2PTradeCancelPacket extends MEPacket {

    private final P2PTradeInstance tradeInstance;
}
