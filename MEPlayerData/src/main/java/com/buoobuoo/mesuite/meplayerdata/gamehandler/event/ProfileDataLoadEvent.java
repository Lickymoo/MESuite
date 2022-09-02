package com.buoobuoo.mesuite.meplayerdata.gamehandler.event;


import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ProfileDataLoadEvent extends Event {

    @Getter
    private final ProfileData data;

    public ProfileDataLoadEvent(ProfileData data){
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
