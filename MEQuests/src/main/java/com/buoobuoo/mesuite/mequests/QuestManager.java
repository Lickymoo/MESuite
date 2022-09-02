package com.buoobuoo.mesuite.mequests;

import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.mequests.data.ProfileQuestData;
import com.buoobuoo.mesuite.mequests.impl.act1.ACT1_MQ1;
import com.buoobuoo.mesuite.mequests.impl.act1.ACT1_MQ2;
import com.buoobuoo.mesuite.mequests.impl.act1.ACT1_MQ3;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QuestManager {
    private List<QuestLine> registeredQuests = new ArrayList<>();

    private final MEQuestsPlugin plugin;

    public QuestManager(MEQuestsPlugin plugin){
        this.plugin = plugin;
        init();
    }

    public void init(){
        registerQuests(
                new ACT1_MQ1(plugin),
                new ACT1_MQ2(plugin),
                new ACT1_MQ3(plugin)
        );
    }

    public void registerQuests(QuestLine... quests){
        registeredQuests.addAll(List.of(quests));
        plugin.registerEvents(quests);
    }

    public QuestLine getQuestByClass(Class<? extends QuestLine> clazz){
        for(QuestLine questLine : registeredQuests){
            if(questLine.getClass() == clazz)
                return questLine;
        }
        return null;
    }

    public QuestLine getQuestByID(String id){
        for(QuestLine questLine : registeredQuests){
            if(questLine.getQuestID().equalsIgnoreCase(id))
                return questLine;
        }
        return null;
    }

    public Class<? extends QuestLine> getQuestClassByID(String id){
        for(QuestLine questLine : registeredQuests){
            if(questLine.getQuestID().equalsIgnoreCase(id))
                return questLine.getClass();
        }
        return null;
    }

    public List<String> allQuestID(){
        List<String> quests = new ArrayList<>();
        for(QuestLine quest : registeredQuests){
            quests.add(quest.getQuestID());
        }
        return quests;
    }

    public void startQuest(String questID, ProfileData profileData){
        startQuest(getQuestClassByID(questID), profileData);
    }

    public void startQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        Player player = Bukkit.getPlayer(profileData.getOwnerID());
        questLine.start(profileData);
    }

    public void finishQuest(String questID, ProfileData profileData){
        finishQuest(getQuestClassByID(questID), profileData);
    }

    public void finishQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        applyCompletedToProfile(questClass, profileData);
    }

    public void resetQuest(String questID, ProfileData profileData){
        resetQuest(getQuestClassByID(questID), profileData);
    }

    public void resetQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        removeActiveQuest(questClass, profileData);
        ProfileQuestData questData = plugin.getQuestDataManager().getQuestData(profileData.getProfileID());
        questData.getCompletedQuest().remove(questLine.getQuestID());
    }

    public void loadQuests(ProfileData profileData){
        ProfileQuestData questData = plugin.getQuestDataManager().getQuestData(profileData.getProfileID());
        for(String str : questData.getActiveQuests()){
            String questID = str.split(":")[0];
            QuestLine questLine = getQuestByID(questID);
            questLine.loadQuestString(profileData, str);
        }

        if(!hasActiveQuest(ACT1_MQ1.class, questData) && !hasCompletedQuest(ACT1_MQ1.class, questData)){
            startQuest(ACT1_MQ1.class, profileData);
        }
    }

    public void applyActiveToProfile(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        Player player = Bukkit.getPlayer(profileData.getOwnerID());

        removeActiveQuest(questClass, profileData);
        String str = questLine.saveQuestString(player);
        ProfileQuestData questData = plugin.getQuestDataManager().getQuestData(profileData.getProfileID());
        questData.getActiveQuests().add(str);
    }

    public void applyCompletedToProfile(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        Player player = Bukkit.getPlayer(profileData.getOwnerID());

        removeActiveQuest(questClass, profileData);
        ProfileQuestData questData = plugin.getQuestDataManager().getQuestData(profileData.getProfileID());
        questData.getCompletedQuest().add(questLine.getQuestID());
    }

    public void removeActiveQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        QuestLine questLine = getQuestByClass(questClass);
        String flag = null;
        ProfileQuestData questData = plugin.getQuestDataManager().getQuestData(profileData.getProfileID());
        for(String str : questData.getActiveQuests()){
            String questID = str.split(":")[0];
            if(questLine.getQuestID().equalsIgnoreCase(questID))
                flag = str;
        }
        if(flag != null)
            questData.getActiveQuests().remove(flag);
    }

    public boolean hasActiveQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        ProfileQuestData questData = plugin.getQuestDataManager().getQuestData(profileData.getProfileID());
        return hasActiveQuest(questClass, questData);
    }

    public boolean hasActiveQuest(Class<? extends QuestLine> questClass, ProfileQuestData questData){
        QuestLine questLine = getQuestByClass(questClass);
        for(String str : questData.getActiveQuests()){
            String questID = str.split(":")[0];
            if(questLine.getQuestID().equalsIgnoreCase(questID))
                return true;
        }
        return false;
    }

    public boolean hasCompletedQuest(Class<? extends QuestLine> questClass, ProfileData profileData){
        if(profileData == null)
            return false;

        ProfileQuestData questData = plugin.getQuestDataManager().getQuestData(profileData.getProfileID());
        return hasCompletedQuest(questClass, questData);
    }

    public boolean hasCompletedQuest(Class<? extends QuestLine> questClass, ProfileQuestData questData){
        QuestLine questLine = getQuestByClass(questClass);
        for(String str : questData.getCompletedQuest()){
            if(questLine.getQuestID().equalsIgnoreCase(str))
                return true;
        }
        return false;
    }

    public void clearPlayer(Player player){
        for(QuestLine questLine : registeredQuests){
            questLine.clearPlayer(player);
        }
    }
}







































