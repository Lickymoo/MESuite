package com.buoobuoo.mesuite.mecore.gamehandler.listener;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.PlayerJoinPacket;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {

    private final MECorePlugin plugin;

    public PlayerJoinEventListener(MECorePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(PlayerJoinEvent event){
        PlayerJoinPacket packet = new PlayerJoinPacket(event.getPlayer().getUniqueId());
        plugin.getMeLinker().getPacketManager().sendPacket(packet);
    }
}
