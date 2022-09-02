package com.buoobuoo.mesuite.melinker.gamehandler.event;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;

public class MEPacketBungeeEvent extends Event {

    @Getter
    private final MEPacket packet;

    public MEPacketBungeeEvent(MEPacket packet){
        this.packet = packet;
    }
}
