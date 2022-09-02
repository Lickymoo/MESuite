package com.buoobuoo.mesuite.meentities.gamehandler.event;


import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class MEEntityDeathEvent extends Event {

    private final CustomEntity entity;

    public MEEntityDeathEvent(CustomEntity entity){
        this.entity = entity;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}