package com.buoobuoo.mesuite.meplayerdata.gamehandler.event;

import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class QueryItemModifiersEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final EntityStatInstance statInstance;
    public QueryItemModifiersEvent(Player player, EntityStatInstance statInstance){
        super(player);
        this.statInstance = statInstance;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
