package com.buoobuoo.mesuite.meabilities.data;

import com.buoobuoo.mesuite.meabilities.MEAbilitiesPlugin;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.ProfileDataRemoveEvent;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.ProfileDataSaveEvent;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ProfileAbilityDataManager implements Listener {
    private final MEAbilitiesPlugin plugin;
    private final Map<UUID, ProfileAbilityData> playerAbilityDataMap = new HashMap<>();

    public ProfileAbilityDataManager(MEAbilitiesPlugin plugin){
        this.plugin = plugin;
    }

    public ProfileAbilityData getAbilityData(Player player){
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);
        if(profileData == null)
            return null;

        ProfileAbilityData abilityData = playerAbilityDataMap.computeIfAbsent(profileData.getProfileID(), key -> plugin.getMongoHook().loadObject(key.toString(), ProfileAbilityData.class, "abilityData"));
        abilityData.setOwnerID(player.getUniqueId());
        abilityData.setProfileID(profileData.getProfileID());
        return abilityData;
    }

    public ProfileAbilityData getAbilityData(UUID uuid){
        ProfileAbilityData abilityData = playerAbilityDataMap.computeIfAbsent(uuid, key -> plugin.getMongoHook().loadObject(key.toString(), ProfileAbilityData.class, "abilityData"));
        abilityData.setProfileID(uuid);
        return abilityData;
    }

    //piggyback player data save
    @EventHandler
    public void onDataSave(ProfileDataSaveEvent event){
        MongoHook mongoHook = plugin.getMongoHook();
        UUID uuid = event.getData().getProfileID();
        if(uuid == null)
            return;

        mongoHook.saveObject(uuid.toString(), getAbilityData(uuid), "abilityData");
    }

    @EventHandler
    public void onDataRemove(ProfileDataRemoveEvent event){
        UUID uuid = event.getData().getProfileID();
        if(uuid == null)
            return;

        playerAbilityDataMap.remove(uuid);
    }
}
