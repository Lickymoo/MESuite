package com.buoobuoo.mesuite.melinker.redis.packet.impl.party;

import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PartyDataByInvitePacket extends MECallbackPacket<PartyDataByInviteResponsePacket> {
    private UUID playerID;

    @Override
    public void onRespond(PartyDataByInviteResponsePacket packet) {

    }
}
