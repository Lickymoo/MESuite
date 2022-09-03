package com.buoobuoo.mesuite.mecore.gamehandler.listener.virtualplayer;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer.PlayerAnimatePacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer.PlayerPosePacket;
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
import org.bukkit.inventory.EquipmentSlot;

public class PlayerAnimateEventListener implements Listener {

    private final MECorePlugin plugin;

    public PlayerAnimateEventListener(MECorePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onIntreact(PlayerInteractEvent event){
        Player player = event.getPlayer();
        boolean mainHand = event.getHand() == EquipmentSlot.HAND;
        switch (event.getAction()){
            case LEFT_CLICK_BLOCK, LEFT_CLICK_AIR -> {
                if(mainHand)
                    sendAnimation(player, PlayerAnimatePacket.PlayerAnimation.SWING_MAIN_HAND);
                else
                    sendAnimation(player, PlayerAnimatePacket.PlayerAnimation.SWING_OFF_HAND);
            }
        }
    }

    public void sendAnimation(Player player, PlayerAnimatePacket.PlayerAnimation animation){
        PlayerAnimatePacket animatePacket = new PlayerAnimatePacket(player.getUniqueId(), animation);
        plugin.getMeLinker().getPacketManager().sendPacket(animatePacket);
    }
}
