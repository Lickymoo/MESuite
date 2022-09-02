package com.buoobuoo.mesuite.meplayerdata.inventory;

import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.meplayerdata.model.PlayerData;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ProfileEditInventory extends CustomInventory {
    private UUID uuid;
    private final MEPlayerDataPlugin plugin;

    public ProfileEditInventory(MEPlayerDataPlugin plugin, Player player, UUID profileUUID) {
        super(plugin.getInventoryManager(), player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PROFILE_EDIT_MAIN + UnicodeSpaceUtil.getNeg(169)
                +"&8Profile Edit", 27);
        this.plugin = plugin;
        this.uuid = profileUUID;

        this.addHandler(event -> {
            Inventory i = new ProfileDeleteInventory(plugin, player, profileUUID, pl -> {
                Inventory i2 = new ProfileEditInventory(plugin, pl, profileUUID).getInventory();
                pl.openInventory(i2);
            }).getInventory();
            player.openInventory(i);
        }, 26);

        this.addHandler(event -> {
            Inventory i = new ProfileInventory(plugin, player).getInventory();
            player.openInventory(i);
        }, 0);

        this.addHandler(event -> {
            Inventory i = new ProfileIconChooseInventory(plugin, player, profileUUID).getInventory();
            player.openInventory(i);
        }, 13);

        this.addHandler(event -> {
            Inventory i = new ProfileRenameInventory(plugin, player, profileUUID, name -> {
                PlayerDataManager dataManager = plugin.getPlayerDataManager();
                ProfileData profileData = dataManager.getProfile(uuid);
                profileData.setProfileName(name);
                dataManager.saveProfile(profileData);

                Inventory i2 = new ProfileEditInventory(plugin, player, profileUUID).getInventory();
                player.openInventory(i2);
            }).getInventory();
            player.openInventory(i);
        }, 17);
    }


    @Override
    public Inventory getInventory() {
        PlayerDataManager dataManager = plugin.getPlayerDataManager();
        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title));
        PlayerData playerData = dataManager.getData(player);
        ProfileData profileData = dataManager.getProfile(uuid);

        ItemStack back = new ItemBuilder(MatRepo.INVISIBLE).name("&7Return to profiles").create();
        inv.setItem(0, back);

        ItemStack rename = new ItemBuilder(MatRepo.INVISIBLE).name("&7Rename Profile").lore("&7Current: &r&f" + profileData.getProfileName()).create();
        inv.setItem(17, rename);

        ItemStack delete = new ItemBuilder(MatRepo.INVISIBLE).name("&7Delete Profile").create();
        inv.setItem(26, delete);

        ItemStack icon = new ItemBuilder(profileData.getProfileIcon()).name("&7Click to change profile icon").create();
        inv.setItem(13, icon);

        return inv;
    }
}









































