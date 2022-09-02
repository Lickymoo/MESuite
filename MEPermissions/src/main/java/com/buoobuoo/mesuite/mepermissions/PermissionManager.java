package com.buoobuoo.mesuite.mepermissions;

import com.buoobuoo.mesuite.mepermissions.data.PlayerPermissionData;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.UUID;

public class PermissionManager {
    private final MEPermissionsPlugin plugin;

    public static PermissionGroup defaultGroup = PermissionGroup.MEMBER;

    public PermissionManager(MEPermissionsPlugin plugin){
        this.plugin = plugin;
    }

    public static HashMap<UUID, PermissionAttachment> permissionList = new HashMap<>();

    public void removePermission(Player player, String perm) {
        permissionList.get(player.getUniqueId()).unsetPermission(perm);
    }

    public void setPermission(Player player, String perm) {
        setPermission(player, perm, true);
    }

    public void setPermission(Player player, String perm, boolean value) {
        if(!permissionList.containsKey(player.getUniqueId())) return;
        permissionList.get(player.getUniqueId()).setPermission(perm, value);
    }

    public void setPlayerGroup(Player player, PermissionGroup group) {
        PlayerPermissionData user = plugin.getPlayerPermissionDataManager().getPermissionData(player);
        user.setGroup(group);
        assignPermissions(player);
    }

    /*
     * Called on join
     */
    public void assignPermissions(Player player) {
        PlayerPermissionData user = plugin.getPlayerPermissionDataManager().getPermissionData(player);
        if(user.getGroup() == null) user.setGroup(defaultGroup);
        for(String str : user.getGroup().getAllPerms())
        {
            setPermission(player, str);
        }
    }

}


