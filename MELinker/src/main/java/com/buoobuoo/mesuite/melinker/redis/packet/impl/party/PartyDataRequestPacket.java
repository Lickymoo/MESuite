package com.buoobuoo.mesuite.melinker.redis.packet.impl.party;

import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.function.Consumer;

@Getter
@Setter
@AllArgsConstructor
public class PartyDataRequestPacket extends MECallbackPacket<PartyDataRequestResponsePacket> {
    private UUID partyID;
    private transient Consumer<PartyDataRequestResponsePacket> responsePacketConsumer;

    @Override
    public void onRespond(PartyDataRequestResponsePacket packet) {
        if(responsePacketConsumer != null)
            responsePacketConsumer.accept(packet);
    }
}
