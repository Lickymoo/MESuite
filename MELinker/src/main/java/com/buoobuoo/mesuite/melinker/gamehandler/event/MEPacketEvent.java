package com.buoobuoo.mesuite.melinker.gamehandler.event;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MEPacketEvent extends Event {

    @Getter
    private final MEPacket packet;

    public MEPacketEvent(MEPacket packet){
        this.packet = packet;
    }


    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
