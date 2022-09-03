package com.buoobuoo.mesuite.meaggregator.inventory.impl.playermenu;

import com.buoobuoo.mesuite.meaggregator.MEAggregatorPlugin;
import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.meplayerdata.inventory.ProfileInventory;
import com.buoobuoo.mesuite.meplayerdata.model.PlayerData;
import com.buoobuoo.mesuite.mequests.inventory.QuestMainInventory;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerMenuSelfMainInventory extends CustomInventory {
    private final Player target;
    private final MEAggregatorPlugin plugin;
    public PlayerMenuSelfMainInventory(MEAggregatorPlugin plugin, Player target, Player player) {
        super(plugin.getInventoryManager(), player, "", 36);

        this.target = target;
        this.plugin = plugin;

        this.addHandler(event -> {
            Inventory inv = new SettingsMenuInventory(plugin, player).getInventory();
            player.openInventory(inv);
        }, 6);
        
        this.addHandler(event -> {
            Inventory inv = new ProfileInventory(plugin.getMePlayerDataPlugin(), player).getInventory();
            PlayerData playerData = plugin.getPlayerDataManager().getData(player);
            plugin.getPlayerDataManager().setProfile(playerData, null);;
            player.openInventory(inv);
        }, 27);

        this.addHandler( event -> {
            Inventory inv = new QuestMainInventory(plugin.getMeQuestsPlugin(), player).getInventory();
            player.openInventory(inv);
        }, 31);

        this.addHandler( event -> {
            Inventory inv = new AbilityInventory(plugin, player).getInventory();
            player.openInventory(inv);
        }, 33);
    }

    @Override
    public Inventory getInventory(){
        StringBuilder title = new StringBuilder(UnicodeSpaceUtil.getNeg(8) + "&r&f");
        title.append(CharRepo.UI_INVENTORY_PLAYERMENU_SELF_MAIN);
        title.append(UnicodeSpaceUtil.getNeg(177));

        PlayerInventory targetInventory = target.getInventory();
        ItemStack helmet = targetInventory.getHelmet();
        ItemStack chestplate = targetInventory.getChestplate();
        ItemStack leggings = targetInventory.getLeggings();
        ItemStack boots = targetInventory.getBoots();

        //if null add icon
        if(helmet == null) {
            title.append(CharRepo.UI_INVENTORY_PLAYERMENU_HELMET_ICON);
            title.append(UnicodeSpaceUtil.getNeg(177));
        }
        if(chestplate == null){
            title.append(CharRepo.UI_INVENTORY_PLAYERMENU_CHESTPLATE_ICON);
            title.append(UnicodeSpaceUtil.getNeg(177));
        }
        if(leggings == null){
            title.append(CharRepo.UI_INVENTORY_PLAYERMENU_LEGGINGS_ICON);
            title.append(UnicodeSpaceUtil.getNeg(177));
        }
        if(boots == null) {
            title.append(CharRepo.UI_INVENTORY_PLAYERMENU_BOOTS_ICON);
            title.append(UnicodeSpaceUtil.getNeg(177));
        }



        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title.toString()));
        inv.setItem(8, helmet);
        inv.setItem(17, chestplate);
        inv.setItem(26, leggings);
        inv.setItem(35, boots);

        ItemStack settings = new ItemBuilder(MatRepo.INVISIBLE).name("&7Settings").create();
        inv.setItem(6, settings);

        ItemStack profileMenu = new ItemBuilder(MatRepo.INVISIBLE).name("&7Chose another profile").create();
        inv.setItem(27, profileMenu);

        ItemStack quests = new ItemBuilder(MatRepo.INVISIBLE).name("&7Quests").create();
        inv.setItem(31, quests);

        ItemStack abilities = new ItemBuilder(MatRepo.INVISIBLE).name("&7Abilities").create();
        inv.setItem(33, abilities);

        return inv;
    }
}
