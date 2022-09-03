package com.buoobuoo.mesuite.melinker.redis.packet.impl;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.Getter;

@Getter
public class StaffMessagePacket extends MEPacket {
    private String[] message;

    public StaffMessagePacket(String... message){
        this.message = message;
    }
}
