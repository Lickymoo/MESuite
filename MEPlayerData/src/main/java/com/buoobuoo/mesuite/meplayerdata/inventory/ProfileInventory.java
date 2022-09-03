package com.buoobuoo.mesuite.meplayerdata.inventory;

import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.meplayerdata.inventory.logout.LogoutInventory;
import com.buoobuoo.mesuite.meplayerdata.model.PlayerData;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class ProfileInventory extends CustomInventory {

    private final boolean capacity;
    private final MEPlayerDataPlugin plugin;

    public ProfileInventory(MEPlayerDataPlugin plugin, Player player) {
        this(plugin, player, plugin.getPlayerDataManager().getData(player).isProfileCapacity());
    }

    public ProfileInventory(MEPlayerDataPlugin plugin, Player player, boolean capacity) {
        super(plugin.getInventoryManager(), player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + (capacity ? CharRepo.UI_INVENTORY_PROFILE_SELECT_FULL : CharRepo.UI_INVENTORY_PROFILE_SELECT_MAIN) + UnicodeSpaceUtil.getNeg(169)
                +"&8Profile Select", 36);
        this.plugin = plugin;
        this.capacity = capacity;


        this.addHandler(event -> {
            if(event.getCurrentItem() == null)
                return;

            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            String id = pdc.get(NamespacedKey.minecraft("me-profile-id"), PersistentDataType.STRING);

            if(event.isRightClick()){
                Inventory inv = new ProfileEditInventory(plugin, player, UUID.fromString(id)).getInventory();
                player.openInventory(inv);
                return;
            }

            UUID uuid = UUID.fromString(id);

            PlayerDataManager dataManager = plugin.getPlayerDataManager();
            PlayerData data = dataManager.getData(player);
            dataManager.setProfile(data, uuid);

            player.closeInventory();

        }, 10, 11, 12, 13, 14, 15, 16);

        if(!capacity) {
            this.addHandler(event -> {
                PlayerDataManager dataManager = plugin.getPlayerDataManager();
                PlayerData data = dataManager.getData(player);
                ProfileData profileData = dataManager.createNewProfile(data);

                Inventory i = new ProfileInventory(plugin, player).getInventory();
                player.openInventory(i);
            }, 30, 31, 32);
        }

        this.addHandler(event -> {
            Inventory inv = new LogoutInventory(plugin, player, pl -> {
                Inventory i = new ProfileInventory(plugin, pl).getInventory();
                pl.openInventory(i);
            }).getInventory();
            player.openInventory(inv);
        }, 27, 28);
    }


    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title));
        PlayerDataManager dataManager = plugin.getPlayerDataManager();
        PlayerData playerData = dataManager.getData(player);

        int index = 10;
        for (UUID uuid : playerData.getProfileIDs()) {
            Material mat;
            String name;
            int level;

            if (!dataManager.isProfileLoaded(uuid)) {
                mat = plugin.getMongoHook().getValue(uuid.toString(), "profileIcon", Material.class, "profileData");
                name = plugin.getMongoHook().getValue(uuid.toString(), "profileName", String.class, "profileData");
                level = plugin.getMongoHook().getValue(uuid.toString(), "level", Integer.class, "profileData");
            } else {
                mat = dataManager.getProfile(uuid).getProfileIcon();
                name = dataManager.getProfile(uuid).getProfileName();
                level = dataManager.getProfile(uuid).getLevel();
            }

            mat = mat == null ? Material.WOODEN_SWORD : mat;

            ItemStack item = new ItemBuilder(mat).name("&r&f" + name).nbtString("me-profile-id", uuid.toString()).lore("&r&7Level &r&f" + level, " ", "&r&fRight click &7to edit profile", "&r&fLeft click &7to select profile").create();
            inv.setItem(index++, item);
        }

        if (!capacity){
            ItemStack create = new ItemBuilder(MatRepo.INVISIBLE).name("&7Create Profile").create();
            inv.setItem(30, create);
            inv.setItem(31, create);
            inv.setItem(32, create);
        }

        ItemStack logout = new ItemBuilder(MatRepo.INVISIBLE).name("&7Logout").create();
        inv.setItem(27, logout);
        inv.setItem(28, logout);

        return inv;
    }
}









































