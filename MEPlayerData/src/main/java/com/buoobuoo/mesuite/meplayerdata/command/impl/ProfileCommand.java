package com.buoobuoo.mesuite.meplayerdata.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.meplayerdata.inventory.ProfileInventory;
import com.buoobuoo.mesuite.meplayerdata.model.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@CommandAlias("profile|prof|profiles")
public class ProfileCommand extends BaseCommand {

    private final MEPlayerDataPlugin plugin;

    public ProfileCommand(MEPlayerDataPlugin plugin){
        this.plugin = plugin;
    }

    @Default
    public void def(Player player){
        PlayerDataManager dataManager = plugin.getPlayerDataManager();
        Inventory inv = new ProfileInventory(plugin, player).getInventory();
        PlayerData playerData = dataManager.getData(player);
        dataManager.setProfile(playerData, null);
        player.openInventory(inv);
    }
}
