package com.buoobuoo.mesuite.meinventories;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CustomInventoryManager implements Listener {

    private final JavaPlugin plugin;
    private final static List<CustomInventory> customInventoryList = new ArrayList<>();

    public CustomInventoryManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Inventory inv = event.getInventory();

        CustomInventory handler = getHandler(inv);
        unregisterInventory(handler);
        if(handler != null)
            handler.onClose(event);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory clickedInventory = event.getClickedInventory();
        Inventory inv = event.getInventory();
        Player player = (Player)event.getWhoClicked();
        boolean isTopInventory = false;

        if(clickedInventory == player.getOpenInventory().getTopInventory())
            isTopInventory = true;

        CustomInventory handler = getHandler(inv);
        if(handler == null)
            return;

        event.setCancelled(true);

        if(!isTopInventory){
            handler.onBottomClick(event);
            return;
        }

        int slot = event.getSlot();
        if(handler.slotMap.get(slot) != null)
            handler.slotMap.get(slot).accept(event);
    }

    public void registerInventory(CustomInventory... inventories){
        for(CustomInventory inventory : inventories){
            customInventoryList.add(inventory);
            plugin.getServer().getPluginManager().registerEvents(inventory, plugin);
        }
    }

    public void unregisterInventory(CustomInventory... inventories){
        for(CustomInventory inventory : inventories){
            customInventoryList.remove(inventory);
            HandlerList.unregisterAll(inventory);
        }
    }

    public CustomInventory getHandler(Inventory inventory){
        for(CustomInventory inv : customInventoryList){
            if(inv == null || inventory == null)
                continue;

            if(inventory.getHolder() == null)
                continue;

            if(inv == inventory.getHolder())
                return inv;
        }
        return null;
    }

}
