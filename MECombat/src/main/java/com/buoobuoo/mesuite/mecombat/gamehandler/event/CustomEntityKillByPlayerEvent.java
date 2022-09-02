package com.buoobuoo.mesuite.mecombat.gamehandler.event;

import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CustomEntityKillByPlayerEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final CustomEntity entity;

    public CustomEntityKillByPlayerEvent(Player player, CustomEntity entity){
        super(player);
        this.entity = entity;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
