package com.buoobuoo.mesuite.mecore.gamehandler.listener.virtualplayer;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer.PlayerMovePacket;
import com.buoobuoo.mesuite.melinker.util.MELoc;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveEventListener implements Listener {

    private final MECorePlugin plugin;

    public PlayerMoveEventListener(MECorePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();

        Location loc = player.getLocation();

        String name = loc.getWorld().getName();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        float pitch = loc.getPitch();
        float yaw = loc.getYaw();

        MELoc location = new MELoc(name, x, y, z, pitch, yaw);

        PlayerMovePacket movePacket = new PlayerMovePacket(player.getUniqueId(), location);
        plugin.getMeLinker().getPacketManager().sendPacket(movePacket);
    }
}
