package com.buoobuoo.mesuite.mecore.gamehandler.listener.virtualplayer;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer.PlayerPosePacket;
import net.minecraft.world.entity.Pose;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerTogglePoseEventListener implements Listener {

    private final MECorePlugin plugin;

    public PlayerTogglePoseEventListener(MECorePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent event){
        delay(() -> {
            Player player = event.getPlayer();
            sendPose(player);
        });
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        delay(() -> {
            Player player = event.getPlayer();
            sendPose(player);
        });
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        delay(() -> {
            Player player = event.getEntity();
            sendPose(player);
        });
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        delay(() -> {
            Player player = event.getPlayer();
            sendPose(player);
        });
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        delay(() -> {
            Player player = event.getPlayer();
            sendPose(player);
        });
    }

    @EventHandler
    public void onDig(BlockDamageEvent event){
        delay(() -> {
            Player player = event.getPlayer();
            sendPose(player);
        });
    }

    @EventHandler
    public void onDigAbort(BlockDamageAbortEvent event){
        delay(() -> {
            Player player = event.getPlayer();
            sendPose(player);
        });
    }


    public void sendPose(Player player){
        PlayerPosePacket toggleCrouchPacket = new PlayerPosePacket(player.getUniqueId(), player.getPose());
        plugin.getMeLinker().getPacketManager().sendPacket(toggleCrouchPacket);
    }

    public void delay(Runnable runnable){
        Bukkit.getScheduler().runTaskLater(plugin, runnable, 1);
    }
}
