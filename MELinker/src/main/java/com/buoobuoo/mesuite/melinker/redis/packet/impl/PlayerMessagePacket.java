package com.buoobuoo.mesuite.melinker.redis.packet.impl;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PlayerMessagePacket extends MEPacket {
    private UUID recipient;
    private String[] message;

    public PlayerMessagePacket(UUID recipient, String... message){
        this.recipient = recipient;
        this.message = message;
    }
}
