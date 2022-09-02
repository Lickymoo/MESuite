package com.buoobuoo.mesuite.melinker.redis.packet.impl;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class PlayerLeaveMEPacket extends MEPacket {
    public UUID uuid;

}
