package com.buoobuoo.mesuite.melinker.redis.packet.impl.party;

import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackResponsePacket;
import com.buoobuoo.mesuite.melinker.util.PartyData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
@Setter
@AllArgsConstructor
public class PartyDataByInviteResponsePacket extends MECallbackResponsePacket {
    private  final List<PartyData> partyIds;
}
