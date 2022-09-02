package com.buoobuoo.mesuite.mequests.command;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.mequests.MEQuestsPlugin;
import com.buoobuoo.mesuite.mequests.QuestManager;
import com.buoobuoo.mesuite.mequests.inventory.QuestMainInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@CommandAlias("quest|quests")
public class QuestCommand extends BaseCommand {

    private final MEQuestsPlugin plugin;
    private final QuestManager questManager;

    public QuestCommand(MEQuestsPlugin plugin) {
        this.plugin = plugin;
        this.questManager = plugin.getQuestManager();
    }

    @Default
    public void quest(Player player){
        Inventory inv = new QuestMainInventory(plugin ,player).getInventory();
        player.openInventory(inv);
    }

    @Subcommand("start")
    @CommandCompletion("@players @quests")
    public void start(Player player, OnlinePlayer target, String questID){
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(target.getPlayer());
        questManager.startQuest(questID, profileData);
    }

    @Subcommand("finish")
    @CommandCompletion("@players @quests")
    public void finish(Player player, OnlinePlayer target, String questID){
        //Force start
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(target.getPlayer());
        questManager.finishQuest(questID, profileData);
    }

    @Subcommand("reset")
    @CommandCompletion("@players @quests")
    public void reset(Player player, OnlinePlayer target, String questID){
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(target.getPlayer());
        questManager.resetQuest(questID, profileData);
    }


}