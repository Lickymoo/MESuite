package com.buoobuoo.mesuite.meplayerdata.inventory;

import com.buoobuoo.mesuite.mecore.gamehandler.event.AnvilRenameEvent;
import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;
import java.util.function.Consumer;

public class ProfileRenameInventory extends CustomInventory {
    private final Consumer<String> consumer;
    private final UUID profileUUID;
    private final MEPlayerDataPlugin plugin;

    public ProfileRenameInventory(MEPlayerDataPlugin plugin, Player player, UUID profileUUID, Consumer<String> consumer){
        super(plugin.getInventoryManager(), player, UnicodeSpaceUtil.getNeg(60)
                + "&r&f" + CharRepo.UI_INVENTORY_PLAYER_SEARCH_OVERLAY
                + UnicodeSpaceUtil.getNeg(117)
                +"&8Enter profile name", 27);
        this.plugin = plugin;
        this.profileUUID = profileUUID;

        this.consumer = consumer;
        slotMap.put(2, event -> {
            if(event.getInventory().getItem(2) == null){
                return;
            }
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);

            ItemStack item = event.getInventory().getItem(2);
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            String name = pdc.get(NamespacedKey.minecraft("me-profile-sel"), PersistentDataType.STRING);

            consumer.accept(name);
        });
    }

    @Override
    public Inventory getInventory() {
        PlayerDataManager dataManager = plugin.getPlayerDataManager();
        ProfileData profileData =  dataManager.getProfile(profileUUID);
        ItemStack item = new ItemBuilder(MatRepo.INVISIBLE).name(profileData.getProfileName()).create();

        Inventory inv = Bukkit.createInventory(this, InventoryType.ANVIL, Colour.format(title));
        inv.setItem(0, item);

        ItemStack invis = new ItemBuilder(MatRepo.INVISIBLE).name(" ").create();
        inv.setItem(1,invis);

        return inv;
    }

    @EventHandler
    public void onRename(AnvilRenameEvent event){
        if(event.getHandler() != this)
            return;

        String name = event.getName().trim();

        ItemStack accept = new ItemBuilder(MatRepo.GREEN_TICK).name(Colour.format("&a&lRename Profile")).create();
        ItemMeta meta = accept.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(NamespacedKey.minecraft("me-profile-sel"), PersistentDataType.STRING, name);
        accept.setItemMeta(meta);

        Inventory inv = event.getPlayer().getOpenInventory().getTopInventory();
        inv.setItem(2,accept);

    }
}
