package com.buoobuoo.mesuite.meplayerdata.packet;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.Getter;

import java.util.UUID;

@Getter
public class SuspendPlayerDataPacket extends MEPacket {
    private UUID player;

    public SuspendPlayerDataPacket(UUID player){
        this.player = player;
    }
}
