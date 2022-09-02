package com.buoobuoo.mesuite.meplayerdata.gamehandler.event.display;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ActionbarQueryEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    @Setter
    private String actionbar;

    public ActionbarQueryEvent(Player player, String actionbar){
        super(player);
        this.actionbar = actionbar;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
