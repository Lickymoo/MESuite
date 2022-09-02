package com.buoobuoo.mesuite.meplayerdata.inventory.logout;

import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.meplayerdata.model.PlayerData;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class LogoutInventory extends CustomInventory {
    private Consumer<Player> noOption;
    private final MEPlayerDataPlugin plugin;

    public LogoutInventory(MEPlayerDataPlugin plugin, Player player, Consumer<Player> noOption) {
        super(plugin.getInventoryManager(), player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_LOGOUT + UnicodeSpaceUtil.getNeg(169), 36);
        this.plugin = plugin;

        this.addHandler(event -> {
            noOption.accept(player);
        }, 19, 20);

        this.addHandler(event -> {
            PlayerDataManager dataManager = plugin.getPlayerDataManager();
            PlayerData playerData = dataManager.getData(player);
            dataManager.saveData(playerData);
            dataManager.removeData(playerData);

            player.kickPlayer("&7Thankyou for playing :D");
        }, 24, 25);
    }


    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title));

        ItemStack yes = new ItemBuilder(MatRepo.INVISIBLE).name("&7No").lore("&r&fClick to return").create();
        inv.setItem(19, yes);
        inv.setItem(20, yes);

        ItemStack no = new ItemBuilder(MatRepo.INVISIBLE).name("&cYes").lore("&r&fClick to log out").create();
        inv.setItem(24, no);
        inv.setItem(25, no);

        return inv;
    }
}









































