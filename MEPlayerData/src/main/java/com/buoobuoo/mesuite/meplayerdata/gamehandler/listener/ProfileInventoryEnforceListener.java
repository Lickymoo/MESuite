package com.buoobuoo.mesuite.meplayerdata.gamehandler.listener;

import com.buoobuoo.mesuite.mecore.gamehandler.event.UpdateSecondEvent;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.inventory.ProfileInventory;
import com.buoobuoo.mesuite.meplayerdata.model.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

public class ProfileInventoryEnforceListener implements Listener {

    private final MEPlayerDataPlugin plugin;

    public ProfileInventoryEnforceListener(MEPlayerDataPlugin plugin){
        this.plugin = plugin;

    }

    @EventHandler(priority =  EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        //LOAD PLAYER INVENTORY
        Inventory inv = new ProfileInventory(plugin, player).getInventory();
        player.openInventory(inv);
    }


    @EventHandler
    public void onUpdate(UpdateSecondEvent event){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(!plugin.getMongoHook().isConnected())
                return;

            PlayerData playerData = plugin.getPlayerDataManager().getData(player);
            if(playerData.getActiveProfileID() != null)
                continue;

            Inventory inv = player.getOpenInventory().getTopInventory();

            if(plugin.getInventoryManager().getHandler(inv) == null){
                Inventory i = new ProfileInventory(plugin, player).getInventory();
                player.openInventory(i);
            }

        }
    }
}
