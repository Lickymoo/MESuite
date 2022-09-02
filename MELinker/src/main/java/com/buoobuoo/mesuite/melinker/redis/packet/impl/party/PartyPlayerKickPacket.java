package com.buoobuoo.mesuite.melinker.redis.packet.impl.party;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PartyPlayerKickPacket extends MEPacket {
    private UUID partyID;
    private UUID memberID;
}
