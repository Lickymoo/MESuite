package com.buoobuoo.mesuite.mesocial.gamehandler.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PartyInviteUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private UUID playerID;

    public PartyInviteUpdateEvent(UUID playerID) {
        this.playerID = playerID;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}