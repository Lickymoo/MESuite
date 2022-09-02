package com.buoobuoo.mesuite.mequests.inventory.questrelated;

import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.meitems.CustomItemManager;
import com.buoobuoo.mesuite.meitems.CustomItems;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.mequests.MEQuestsPlugin;
import com.buoobuoo.mesuite.mequests.gamehandler.event.ACT1_MQ2PickItemEvent;
import com.buoobuoo.mesuite.meutils.Colour;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ACT1_MQ2Inventory extends CustomInventory {
    private final MEQuestsPlugin plugin;

    public ACT1_MQ2Inventory(MEQuestsPlugin plugin, Player player) {
        super(plugin.getInventoryManager(), player, "&8Choose a weapon", 27);
        this.plugin = plugin;

        this.addDefaultHandler(event -> {
            ItemStack item = event.getCurrentItem();

            if(item == null)
                return;

            player.getInventory().addItem(item);

            ACT1_MQ2PickItemEvent ev = new ACT1_MQ2PickItemEvent(player);
            Bukkit.getPluginManager().callEvent(ev);
            player.closeInventory();
        });
    }



    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title));
        CustomItemManager customItemManager = plugin.getCustomItemManager();

        ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);

        inv.addItem(customItemManager.getItem(profileData, CustomItems.getHandler("STARTER_SWORD")));
        inv.addItem(customItemManager.getItem(profileData, CustomItems.getHandler("STARTER_STAFF")));
        inv.addItem(customItemManager.getItem(profileData, CustomItems.getHandler("STARTER_BOW")));
        inv.addItem(customItemManager.getItem(profileData, CustomItems.getHandler("STARTER_AXE")));
        return inv;
    }
}
