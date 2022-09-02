package com.buoobuoo.mesuite.meentities.gamehandler.listener;

import com.buoobuoo.mesuite.meentities.MEEntitiesPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

public class EntityCombustEventListener implements Listener {

    private final MEEntitiesPlugin plugin;

    public EntityCombustEventListener(MEEntitiesPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(EntityCombustEvent event){
        event.setCancelled(true);
    }
}
