package com.buoobuoo.mesuite.meabilities.command.impl;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import com.buoobuoo.mesuite.meabilities.Ability;
import com.buoobuoo.mesuite.meabilities.AbilityManager;
import com.buoobuoo.mesuite.meabilities.MEAbilitiesPlugin;
import com.buoobuoo.mesuite.meabilities.item.AbilityGemItem;
import com.buoobuoo.mesuite.meitems.CustomItemManager;
import com.buoobuoo.mesuite.meitems.CustomItems;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("enh")
public class AbilityCommand extends BaseCommand {

    private final MEAbilitiesPlugin plugin;

    public AbilityCommand(MEAbilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @Subcommand("debug")
    public class debug extends BaseCommand {

        @Subcommand("give")
        public class give extends BaseCommand {

            @Subcommand("ability")
            @CommandCompletion("@abilities")
            public void ability(Player player, String abilityID) {
                ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);
                AbilityManager abilityManager = plugin.getAbilityManager();
                CustomItemManager customItemManager = plugin.getCustomItemManager();

                Ability ability = abilityManager.getAbilityByID(abilityID);

                AbilityGemItem gemItem = new AbilityGemItem(ability);
                ItemStack item = customItemManager.getItem(profileData, gemItem);
                player.getInventory().addItem(item);
            }
        }
    }
}