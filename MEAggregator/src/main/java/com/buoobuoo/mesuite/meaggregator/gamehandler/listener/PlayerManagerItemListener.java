package com.buoobuoo.mesuite.meaggregator.gamehandler.listener;

import com.buoobuoo.mesuite.meaggregator.MEAggregatorPlugin;
import com.buoobuoo.mesuite.meaggregator.inventory.impl.playermenu.PlayerMenuSelfMainInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public class PlayerManagerItemListener implements Listener {

    private final MEAggregatorPlugin plugin;

    public PlayerManagerItemListener(MEAggregatorPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(event.getPlayer().getInventory().getHeldItemSlot() != 8)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void interact(PlayerInteractEvent event){
        if(event.getPlayer().getInventory().getHeldItemSlot() != 8)
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        Inventory menu = new PlayerMenuSelfMainInventory(plugin, player, player).getInventory();
        player.openInventory(menu);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void click(InventoryClickEvent event){

        Inventory inv = event.getClickedInventory();
        if(!(inv instanceof PlayerInventory))
            return;

        int slot = event.getSlot();
        if(slot != 8)
            return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        Inventory menu = new PlayerMenuSelfMainInventory(plugin, player, player).getInventory();
        player.openInventory(menu);
    }
}
