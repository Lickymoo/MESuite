package com.buoobuoo.mesuite.mesocial.gamehandler.event;

import com.buoobuoo.mesuite.melinker.util.PartyData;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PartyUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private UUID partyID;

    public PartyUpdateEvent(UUID partyID) {
        this.partyID = partyID;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}