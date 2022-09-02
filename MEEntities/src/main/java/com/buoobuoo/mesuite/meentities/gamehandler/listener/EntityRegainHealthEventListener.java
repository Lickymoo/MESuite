package com.buoobuoo.mesuite.meentities.gamehandler.listener;

import com.buoobuoo.mesuite.meentities.MEEntitiesPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityRegainHealthEventListener implements Listener {

    private final MEEntitiesPlugin plugin;

    public EntityRegainHealthEventListener(MEEntitiesPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(EntityRegainHealthEvent event){
        event.setCancelled(true);
    }
}
