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
import java.util.function.Consumer;

public class ProfileDeleteInventory extends CustomInventory {
    private Consumer<Player> noOption;
    private UUID uuid;
    private final MEPlayerDataPlugin plugin;

    public ProfileDeleteInventory(MEPlayerDataPlugin plugin, Player player, UUID profileUUID, Consumer<Player> noOption) {
        super(plugin.getInventoryManager(), player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PROFILE_CONFIRM_DELETE + UnicodeSpaceUtil.getNeg(169), 36);
        this.plugin = plugin;
        this.uuid = profileUUID;

        this.addHandler(event -> {
            noOption.accept(player);
        }, 19, 20);

        this.addHandler(event -> {
            PlayerDataManager dataManager = plugin.getPlayerDataManager();
            PlayerData playerData = dataManager.getData(player);
            ProfileData profileData = dataManager.getProfile(profileUUID);
            dataManager.deleteProfile(profileData);

            Inventory i = new ProfileInventory(plugin, player).getInventory();
            player.openInventory(i);
        }, 24, 25);
    }


    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title));

        ItemStack yes = new ItemBuilder(MatRepo.INVISIBLE).name("&7No").lore("&r&fClick to return").create();
        inv.setItem(19, yes);
        inv.setItem(20, yes);

        ItemStack no = new ItemBuilder(MatRepo.INVISIBLE).name("&cYes").lore("&r&fClick to delete profile").create();
        inv.setItem(24, no);
        inv.setItem(25, no);

        return inv;
    }
}









































