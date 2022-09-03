package com.buoobuoo.mesuite.mequests.data;

import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.PlayerDataSaveEvent;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.mequests.MEQuestsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileQuestDataManager implements Listener {
    private final MEQuestsPlugin plugin;
    private final Map<UUID, ProfileQuestData> playerQuestDataMap = new HashMap<>();

    public ProfileQuestDataManager(MEQuestsPlugin plugin){
        this.plugin = plugin;
    }

    public ProfileQuestData getQuestData(Player player){
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);
        if(profileData == null)
            return null;

        ProfileQuestData abilityData = playerQuestDataMap.computeIfAbsent(profileData.getProfileID(), key -> plugin.getMongoHook().loadObject(key.toString(), ProfileQuestData.class, "questData"));

        abilityData.setOwnerID(player.getUniqueId());
        abilityData.setProfileID(profileData.getProfileID());
        return abilityData;
    }

    public ProfileQuestData getQuestData(UUID uuid){
        ProfileQuestData abilityData = playerQuestDataMap.computeIfAbsent(uuid, key -> plugin.getMongoHook().loadObject(key.toString(), ProfileQuestData.class, "questData"));
        abilityData.setProfileID(uuid);
        return abilityData;
    }

    //piggyback player data save
    @EventHandler
    public void onDataSave(PlayerDataSaveEvent event){
        MongoHook mongoHook = plugin.getMongoHook();
        UUID uuid = event.getData().getActiveProfileID();
        if(uuid == null)
            return;

        mongoHook.saveObject(uuid.toString(), getQuestData(uuid), "questData");
    }

    @EventHandler
    public void onDataRemove(PlayerDataSaveEvent event){
        UUID uuid = event.getData().getActiveProfileID();
        if(uuid == null)
            return;

        playerQuestDataMap.remove(uuid);
    }
}
