package com.buoobuoo.mesuite.meaggregator.inventory.impl.playermenu;

import com.buoobuoo.mesuite.meaggregator.MEAggregatorPlugin;
import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.meplayerdata.model.PlayerData;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SettingsMenuInventory extends CustomInventory {
    private final MEAggregatorPlugin plugin;
    public SettingsMenuInventory(MEAggregatorPlugin plugin, Player player) {
        super(plugin.getInventoryManager(), player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PLAYERMENU_SETTINGS_1, 54);
        this.plugin = plugin;

        this.addHandler(event -> {
            PlayerData playerData = plugin.getPlayerDataManager().getData(player);
            playerData.setSetting_gui_sliders(!playerData.isSetting_gui_sliders());

            Inventory inv = new SettingsMenuInventory(plugin, player).getInventory();
            player.openInventory(inv);
        }, 0);
    }

    @Override
    public Inventory getInventory(){
        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title));
        PlayerData playerData = plugin.getPlayerDataManager().getData(player);

        //slider bar
        boolean setting_gui_sliders = playerData.isSetting_gui_sliders();
        ItemStack setting_gui_sliders_item = new ItemBuilder(setting_gui_sliders ? Material.GREEN_WOOL : Material.RED_WOOL)
                .name("&7Dispaly Health & Mana bars as sliders")
                .lore(setting_gui_sliders ? "&aENABLED" : "&cDISABLED")
                .create();

        inv.setItem(0, setting_gui_sliders_item);

        return inv;
    }
}
