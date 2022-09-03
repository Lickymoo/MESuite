package com.buoobuoo.mesuite.meitems.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import com.buoobuoo.mesuite.meitems.CustomItem;
import com.buoobuoo.mesuite.meitems.CustomItems;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("enh")
public class ItemCommand extends BaseCommand {

    private final MEItemsPlugin plugin;

    public ItemCommand(MEItemsPlugin plugin){
        this.plugin = plugin;
    }


    @Subcommand("debug")
    public class debug extends BaseCommand{
        @Subcommand("give")
        public class give extends BaseCommand {

            @Subcommand("item")
            @CommandCompletion("@custom-items")
            public void item(Player player, String item) {
                ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);
                CustomItem handler = CustomItems.getHandler(item);
                ItemStack stack = plugin.getCustomItemManager().getItem(profileData, handler);
                stack = handler.update(plugin, profileData, stack);

                player.getInventory().addItem(stack);
            }

            @Subcommand("item")
            @CommandCompletion("@custom-items")
            public void item(Player player, String item, int amount) {
                ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);
                CustomItem handler = CustomItems.getHandler(item);
                ItemStack stack = plugin.getCustomItemManager().getItem(profileData, handler);
                stack.setAmount(amount);
                stack = handler.update(plugin, profileData, stack);

                player.getInventory().addItem(stack);
            }
        }
    }
}



