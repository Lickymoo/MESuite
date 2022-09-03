package com.buoobuoo.mesuite.mechat.gamehandler.listener;

import com.buoobuoo.mesuite.mechat.MEChatPlugin;
import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketEvent;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.PlayerMessagePacket;
import com.buoobuoo.mesuite.meutils.Colour;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMessagePacketListener implements Listener {
    private final MEChatPlugin plugin;

    public PlayerMessagePacketListener(MEChatPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPacket(MEPacketEvent event){
        MEPacket packet = event.getPacket();
        if(!(packet instanceof PlayerMessagePacket playerMessagePacket))
            return;

        Player player = Bukkit.getPlayer(playerMessagePacket.getRecipient());
        if(player == null)
            return;

        for(String str : playerMessagePacket.getMessage()){
            player.sendMessage(Colour.format(str));
        }
    }
}
