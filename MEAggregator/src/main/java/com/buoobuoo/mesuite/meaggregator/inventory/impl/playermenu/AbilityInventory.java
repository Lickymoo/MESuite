package com.buoobuoo.mesuite.meaggregator.inventory.impl.playermenu;

import com.buoobuoo.mesuite.meabilities.Ability;
import com.buoobuoo.mesuite.meabilities.AbilityCastType;
import com.buoobuoo.mesuite.meabilities.AbilityManager;
import com.buoobuoo.mesuite.meabilities.data.ProfileAbilityData;
import com.buoobuoo.mesuite.meabilities.item.AbilityGemItem;
import com.buoobuoo.mesuite.meaggregator.MEAggregatorPlugin;
import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.meitems.CustomItem;
import com.buoobuoo.mesuite.meitems.CustomItemManager;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meutils.*;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AbilityInventory extends CustomInventory {
    private final MEAggregatorPlugin plugin;
    public AbilityInventory(MEAggregatorPlugin plugin, Player player) {
        super(plugin.getInventoryManager(), player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PLAYERMENU_ABILITIES + UnicodeSpaceUtil.getNeg(169) + "&8Ability Menu", 36);
        this.plugin = plugin;

        this.addHandler(event -> {
            ItemStack item = event.getCurrentItem();
            if(item == null)
                return;

            if(player.getInventory().firstEmpty() == -1){
                player.sendMessage(Colour.format("&7Cannot unsocket ability gem, inventory is full."));
                return;
            }

            String abilityID = MEUtils.getNBTString(item, "me-ability-id");

            ProfileAbilityData abilityData = plugin.getProfileAbilityDataManager().getAbilityData(player);

            String[] playerAbilities = JavaUtils.removeOccurrences(abilityData.getAbilityIDs(), abilityID);
            abilityData.setAbilityIDs(playerAbilities);
            Ability ability = plugin.getAbilityManager().getAbilityByID(abilityID);

            //Default to a type with no effectiveness decrease
            item = new ItemBuilder(item).clearLore().lore(ability.getLore(AbilityCastType.NONE)).create();

            player.getInventory().addItem(item);

            int slot = (event.getSlot()-10)/2;

            AbilityCastType[] types = abilityData.getAbilityCastTypes();
            types[slot] = null;
            abilityData.setAbilityCastTypes(types);

            Inventory inv = new AbilityInventory(plugin, player).getInventory();
            player.openInventory(inv);
        }, 10, 12, 14, 16);


        this.addHandler(event -> {
            Inventory inv = event.getClickedInventory();
            if(inv.getItem(event.getSlot() - 18) == null)
                return;

            int slot = (event.getSlot()-28)/2;

            Inventory castInv = new AbilityCastTypeInventory(plugin, player, slot).getInventory();
            player.openInventory(castInv);
        }, 28, 30, 32, 34);

        this.addHandler(event -> {
            Inventory i = new PlayerMenuSelfMainInventory(plugin, player, player).getInventory();
            player.openInventory(i);
        }, 0);
    }

    @Override
    public Inventory getInventory(){
        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title));
        AbilityManager abilityManager = plugin.getAbilityManager();
        CustomItemManager customItemManager = plugin.getCustomItemManager();

        ProfileAbilityData abilityData = plugin.getProfileAbilityDataManager().getAbilityData(player);
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);


        int abilityIndex = 10;
        int castTypeIndex = 28;
        int index = 0;

        AbilityCastType[] arr = abilityData.getAbilityCastTypes();
        AbilityCastType[] types = JavaUtils.setArrSize(arr == null ? new AbilityCastType[4] : arr, 4);

        for(Ability ability : abilityManager.getPlayerAbilities(abilityData)){
            AbilityCastType type = types[index];
            if(ability != null) {
                AbilityGemItem gemItem = new AbilityGemItem(ability);
                ItemStack gem = customItemManager.getItem(profileData, gemItem);

                if(type != null)
                    gem = new ItemBuilder(gem).name(ability.getName()).clearLore().lore(ability.getLore(type)).create();

                inv.setItem(abilityIndex, gem);
            }
            if(type != null) {
                ItemStack castTypeItem = new ItemBuilder(type.getMat())
                        .setCustomModelData(type.getCustomModelData())
                        .name(type.getDisplayName())
                        .lore(type.getDisplayLore())
                        .create();

                inv.setItem(castTypeIndex, castTypeItem);

                if(type == AbilityCastType.NONE)
                    inv.setItem(castTypeIndex, null);
            }

            abilityIndex+=2;
            castTypeIndex+=2;
            index++;
        }

        ItemStack back = new ItemBuilder(MatRepo.INVISIBLE).name("&7Return to profiles").create();
        inv.setItem(0, back);

        return inv;
    }

    @Override
    public void onBottomClick(InventoryClickEvent event){
        ItemStack item = event.getCurrentItem();
        if(item == null)
            return;

        CustomItemManager customItemManager = plugin.getCustomItemManager();
        CustomItem handler = customItemManager.getRegistry().getHandler(item);
        AbilityManager abilityManager = plugin.getAbilityManager();
        if(!(handler instanceof AbilityGemItem))
            return;

        ProfileAbilityData abilityData = plugin.getProfileAbilityDataManager().getAbilityData(player);
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);

        String abilityID = MEUtils.getNBTString(item, "me-ability-id");
        String[] playerAbilities = abilityData.getAbilityIDs();

        System.out.println(abilityID);
        Ability ability = abilityManager.getAbilityByID(abilityID);

        if(abilityManager.hasAbility(abilityData, ability)){
            player.sendMessage(Colour.format("&7You cannot slot two of the same abilities."));
            return;
        }
        if(!abilityManager.hasEmptyAbilitySlot(abilityData)){
            player.sendMessage(Colour.format("&7You do not have any empty ability slots."));
            return;
        }

        abilityManager.addAbilityToPlayer(abilityData, ability);

        //will always be 1 stack
        player.getInventory().remove(item);

        event.setCancelled(true);

        Inventory inv = new AbilityInventory(plugin, player).getInventory();
        player.openInventory(inv);
    }
}






















