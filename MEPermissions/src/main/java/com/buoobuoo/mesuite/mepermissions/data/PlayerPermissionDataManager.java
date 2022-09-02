package com.buoobuoo.mesuite.mepermissions.data;

import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.mepermissions.MEPermissionsPlugin;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.PlayerDataSaveEvent;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerPermissionDataManager implements Listener {
    private final MEPermissionsPlugin plugin;
    private final Map<UUID, PlayerPermissionData> playerQuestDataMap = new HashMap<>();

    public PlayerPermissionDataManager(MEPermissionsPlugin plugin){
        this.plugin = plugin;
    }

    public PlayerPermissionData getPermissionData(Player player){
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);
        if(profileData == null)
            return null;

        PlayerPermissionData permissionData = playerQuestDataMap.computeIfAbsent(profileData.getOwnerID(), key -> plugin.getMongoHook().loadObject(key.toString(), PlayerPermissionData.class, "permissionData"));
        permissionData.setOwnerID(player.getUniqueId());
        return permissionData;
    }

    public PlayerPermissionData getPermissionData(UUID uuid){
        PlayerPermissionData permissionData = playerQuestDataMap.computeIfAbsent(uuid, key -> plugin.getMongoHook().loadObject(key.toString(), PlayerPermissionData.class, "permissionData"));
        permissionData.setOwnerID(uuid);
        return permissionData;
    }

    //piggyback player data save
    @EventHandler
    public void onDataSave(PlayerDataSaveEvent event){
        MongoHook mongoHook = plugin.getMongoHook();
        UUID uuid = event.getData().getOwnerID();
        if(uuid == null)
            return;

        mongoHook.saveObject(uuid.toString(), getPermissionData(uuid), "permissionData");
    }

    @EventHandler
    public void onDataRemove(PlayerDataSaveEvent event){
        UUID uuid = event.getData().getActiveProfileID();
        if(uuid == null)
            return;

        playerQuestDataMap.remove(uuid);
    }
}
