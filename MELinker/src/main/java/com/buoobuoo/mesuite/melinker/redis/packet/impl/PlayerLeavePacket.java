package com.buoobuoo.mesuite.melinker.redis.packet.impl;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PlayerLeavePacket extends MEPacket {
    private UUID uuid;

}
