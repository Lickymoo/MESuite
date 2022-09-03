package com.buoobuoo.mesuite.meaggregator.inventory.impl.playermenu;

import com.buoobuoo.mesuite.meabilities.AbilityCastType;
import com.buoobuoo.mesuite.meabilities.data.ProfileAbilityData;
import com.buoobuoo.mesuite.meaggregator.MEAggregatorPlugin;
import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.meutils.*;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AbilityCastTypeInventory extends CustomInventory {
    private final int slot;
    private final MEAggregatorPlugin plugin;

    public AbilityCastTypeInventory(MEAggregatorPlugin plugin, Player player, int slot) {
        super(plugin.getInventoryManager(), player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PLAYERMENU_ABILITY_CASTTYPE + UnicodeSpaceUtil.getNeg(169) + "&8Ability Cast Type", 36);

        this.slot = slot;
        this.plugin = plugin;

        this.addDefaultHandler(event -> {
            ItemStack item = event.getCurrentItem();
            if(item == null)
                return;

            String type = MEUtils.getNBTString(item, "cast-type");
            AbilityCastType castType = AbilityCastType.valueOf(type);

            ProfileAbilityData abilityData = plugin.getProfileAbilityDataManager().getAbilityData(player);
            AbilityCastType[] preT = abilityData.getAbilityCastTypes();
            AbilityCastType[] types = JavaUtils.setArrSize(preT == null ? new AbilityCastType[4] : preT , 4);

            types[slot] = castType;
            abilityData.setAbilityCastTypes(types);

            Inventory i = new AbilityInventory(plugin, player).getInventory();
            player.openInventory(i);
        });

        this.addHandler(event -> {
            Inventory i = new AbilityInventory(plugin, player).getInventory();
            player.openInventory(i);
        }, 0);
    }

    @Override
    public Inventory getInventory(){
        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title));

        ItemStack back = new ItemBuilder(MatRepo.INVISIBLE).name("&7Return to Ability Menu").create();
        inv.setItem(0, back);

        int index = 9;
        for(AbilityCastType castType : AbilityCastType.values()){
            if(castType == null) {
                index++;
                continue;
            }

            ItemStack item = new ItemBuilder(castType.getMat())
                    .setCustomModelData(castType.getCustomModelData())
                    .name(castType.getDisplayName())
                    .lore(castType.getDisplayLore())
                    .nbtString("cast-type", castType.name()).create();
            inv.setItem(index++, item);
        }

        return inv;
    }
}






















