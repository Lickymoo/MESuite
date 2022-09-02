package com.buoobuoo.mesuite.mequests.inventory;

import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.mequests.MEQuestsPlugin;
import com.buoobuoo.mesuite.mequests.QuestLine;
import com.buoobuoo.mesuite.mequests.QuestManager;
import com.buoobuoo.mesuite.mequests.data.ProfileQuestData;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class QuestMainInventory extends CustomInventory {
    private MEQuestsPlugin plugin;
    public QuestMainInventory(MEQuestsPlugin plugin, Player player) {
        super(plugin.getInventoryManager(), player, "&8Quests", 36);
        this.plugin = plugin;
    }



    @Override
    public Inventory getInventory() {
        QuestManager questManager = plugin.getQuestManager();
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);
        ProfileQuestData questData = plugin.getQuestDataManager().getQuestData(player);

        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title));
        if(questData.getActiveQuests() != null)
        for(String str : questData.getActiveQuests()){
            String id = str.split(":")[0];
            QuestLine questLine = questManager.getQuestByID(id);
            if(questLine == null)
                continue;

            ItemStack item = new ItemBuilder(Material.BOOK).name(questLine.getQuestName()).lore(questLine.getQuestBrief()).create();
            inv.addItem(item);
        }

        if(questData.getCompletedQuest() != null)
        for(String str : questData.getCompletedQuest()){
            QuestLine questLine = questManager.getQuestByID(str);

            if(questLine == null)
                continue;

            ItemStack item = new ItemBuilder(Material.BOOK).name(questLine.getQuestName()).lore(questLine.getQuestBrief()).lore(10, "&a&lCOMPLETED").create();
            inv.addItem(item);
        }

        return inv;
    }
}
