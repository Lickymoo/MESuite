package com.buoobuoo.mesuite.mecore.gamehandler.listener;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.MoistureChangeEvent;

public class MoistureChangeEventListener implements Listener {

    private final MECorePlugin plugin;

    public MoistureChangeEventListener(MECorePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(MoistureChangeEvent event){
        event.setCancelled(true);
    }
}
