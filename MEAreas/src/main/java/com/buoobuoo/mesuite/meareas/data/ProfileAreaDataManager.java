package com.buoobuoo.mesuite.meareas.data;

import com.buoobuoo.mesuite.meareas.MEAreasPlugin;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.ProfileDataRemoveEvent;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.ProfileDataSaveEvent;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileAreaDataManager implements Listener {
    private final MEAreasPlugin plugin;
    private final Map<UUID, ProfileAreaData> playerAreaDataMap = new HashMap<>();

    public ProfileAreaDataManager(MEAreasPlugin plugin){
        this.plugin = plugin;
    }

    public ProfileAreaData getAreaData(Player player){
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);
        if(profileData == null)
            return null;

        ProfileAreaData areaData = playerAreaDataMap.computeIfAbsent(profileData.getProfileID(), key -> plugin.getMongoHook().loadObject(key.toString(), ProfileAreaData.class, "areaData"));
        areaData.setOwnerID(player.getUniqueId());
        areaData.setProfileID(profileData.getProfileID());
        return areaData;
    }

    public ProfileAreaData getAreaData(UUID uuid){
        ProfileAreaData areaData = playerAreaDataMap.computeIfAbsent(uuid, key -> plugin.getMongoHook().loadObject(key.toString(), ProfileAreaData.class, "areaData"));
        areaData.setProfileID(uuid);
        return areaData;
    }

    //piggyback player data save
    @EventHandler
    public void onDataSave(ProfileDataSaveEvent event){
        MongoHook mongoHook = plugin.getMongoHook();
        UUID uuid = event.getData().getProfileID();
        if(uuid == null)
            return;

        mongoHook.saveObject(uuid.toString(), getAreaData(uuid), "areaData");
    }

    @EventHandler
    public void onDataRemove(ProfileDataRemoveEvent event){
        UUID uuid = event.getData().getProfileID();
        if(uuid == null)
            return;

        playerAreaDataMap.remove(uuid);
    }
}
