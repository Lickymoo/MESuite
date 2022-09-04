package com.buoobuoo.mesuite.metrade.packet.trade.p2p;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class P2PTradeInitiatePacket extends MEPacket {

    private final UUID player1;
    private final UUID player2;
}
