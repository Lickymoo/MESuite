package com.buoobuoo.mesuite.meutils.navigation;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RouteFinishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Route route;

    public RouteFinishEvent(Route route){
        this.route = route;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}