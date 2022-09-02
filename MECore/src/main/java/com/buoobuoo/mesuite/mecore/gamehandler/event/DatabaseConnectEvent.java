package com.buoobuoo.mesuite.mecore.gamehandler.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class DatabaseConnectEvent extends Event {

    private final DatabaseType databaseType;

    public DatabaseConnectEvent(DatabaseType databaseType){
        this.databaseType = databaseType;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum DatabaseType{
        REDIS,
        MONGODB
    }
}