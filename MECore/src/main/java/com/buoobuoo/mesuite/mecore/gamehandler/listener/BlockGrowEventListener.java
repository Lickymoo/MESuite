package com.buoobuoo.mesuite.mecore.gamehandler.listener;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class BlockGrowEventListener implements Listener {

    private final MECorePlugin plugin;

    public BlockGrowEventListener(MECorePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(event.getPlayer().getInventory().getHeldItemSlot() != 8)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void grow(BlockFormEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void spread(BlockSpreadEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void grow(BlockGrowEvent event){
        event.setCancelled(true);
    }
}
