package com.buoobuoo.mesuite.meplayerdata.gamehandler.event.display;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PrefixQueryEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    @Setter
    private String prefix;

    public PrefixQueryEvent(Player player, String prefix){
        super(player);
        this.prefix = prefix;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
