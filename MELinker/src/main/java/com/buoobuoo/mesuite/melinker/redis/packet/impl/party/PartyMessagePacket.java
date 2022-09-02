package com.buoobuoo.mesuite.melinker.redis.packet.impl.party;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PartyMessagePacket extends MEPacket {

    private final UUID partyID;
    private final String[] message;

    public PartyMessagePacket(UUID partyID, String... message){
        this.partyID = partyID;
        this.message = message;
    }
}
