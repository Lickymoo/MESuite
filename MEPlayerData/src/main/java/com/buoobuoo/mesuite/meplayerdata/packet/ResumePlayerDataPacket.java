package com.buoobuoo.mesuite.meplayerdata.packet;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ResumePlayerDataPacket extends MEPacket {
    private UUID player;

    public ResumePlayerDataPacket(UUID player){
        this.player = player;
    }
}
