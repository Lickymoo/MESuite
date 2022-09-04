package com.buoobuoo.mesuite.melinker.redis.packet.impl.party;

import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public class PartyUpdatePacket extends MEPacket {
    private UUID partyID;
}
