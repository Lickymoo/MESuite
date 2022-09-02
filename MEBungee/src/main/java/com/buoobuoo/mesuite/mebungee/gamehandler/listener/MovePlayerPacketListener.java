package com.buoobuoo.mesuite.mebungee.gamehandler.listener;

import com.buoobuoo.mesuite.mebungee.MEBungeePlugin;
import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketBungeeEvent;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.ChangePlayerServerPacket;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MovePlayerPacketListener implements Listener {

    private final MEBungeePlugin plugin;

    public MovePlayerPacketListener(MEBungeePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(MEPacketBungeeEvent event){
        MEPacket packet = event.getPacket();
        if(packet instanceof ChangePlayerServerPacket changePlayerServerPacket){
            ServerInfo serverInfo = plugin.getProxy().getServerInfo(changePlayerServerPacket.getServerName());
            ProxiedPlayer player = plugin.getProxy().getPlayer(changePlayerServerPacket.getPlayerID());

            if(player.getServer().getInfo().getName().equals(changePlayerServerPacket.getServerName()))
                return;

            player.connect(serverInfo);
        }
    }

}
