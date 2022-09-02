package com.buoobuoo.mesuite.melinker.redis.packet.impl.party;

import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackResponsePacket;
import com.buoobuoo.mesuite.melinker.util.PartyData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyDataRequestResponsePacket extends MECallbackResponsePacket {

    private PartyData partyData;
}
