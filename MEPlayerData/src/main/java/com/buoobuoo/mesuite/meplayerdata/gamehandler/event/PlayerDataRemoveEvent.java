package com.buoobuoo.mesuite.meplayerdata.gamehandler.event;


import com.buoobuoo.mesuite.meplayerdata.model.PlayerData;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDataRemoveEvent extends Event {

    @Getter
    private final PlayerData data;

    public PlayerDataRemoveEvent(PlayerData data){
        this.data = data;
    }


    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
