package com.buoobuoo.mesuite.mechat.gamehandler.listener;

import com.buoobuoo.mesuite.mechat.MEChatPlugin;
import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketEvent;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.StaffMessagePacket;
import com.buoobuoo.mesuite.mepermissions.data.PlayerPermissionData;
import com.buoobuoo.mesuite.meutils.Colour;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StaffMessagePacketListener implements Listener {
    private final MEChatPlugin plugin;

    public StaffMessagePacketListener(MEChatPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPacket(MEPacketEvent event){
        MEPacket packet = event.getPacket();
        if(!(packet instanceof StaffMessagePacket staffMessagePacket))
            return;

        for(Player player : Bukkit.getOnlinePlayers()){
            PlayerPermissionData permissionData = plugin.getPlayerPermissionDataManager().getPermissionData(player);
            if(permissionData.getGroup().hasPermission("me.admin")){
                for(String str : staffMessagePacket.getMessage()){
                    player.sendMessage(Colour.format(str));
                }
            }
        }
    }
}
