package com.buoobuoo.mesuite.melinker.redis.packet.impl.party;

import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public class PartyCreatePacket extends MECallbackPacket<PartyDataRequestResponsePacket> {
    private UUID ownerID;
    private transient Consumer<PartyDataRequestResponsePacket> responsePacketConsumer;

    @Override
    public void onRespond(PartyDataRequestResponsePacket packet) {
        if(responsePacketConsumer != null)
            responsePacketConsumer.accept(packet);
    }
}
