package com.buoobuoo.mesuite.mecore.virtualplayers;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.mecore.gamehandler.event.UpdateSecondEvent;
import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketEvent;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.PlayerLeavePacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer.PlayerAnimatePacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer.PlayerEquipmentPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer.PlayerMovePacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer.PlayerPosePacket;
import com.buoobuoo.mesuite.melinker.util.MELoc;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VirtualPlayerManager implements Listener {
    private MECorePlugin plugin;

    public VirtualPlayerManager(MECorePlugin plugin){
        this.plugin = plugin;
    }

    private Map<UUID, VirtualPlayerEntity> virtualPlayerMap = new ConcurrentHashMap<>();

    @EventHandler
    public void onMove(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof PlayerMovePacket playerMovePacket){
            UUID uuid = playerMovePacket.getUuid();
            Player player = Bukkit.getPlayer(uuid);
            if(player != null)
                return;

            NetworkedPlayer networkedPlayer = plugin.getNetworkedPlayer(uuid);
            networkedPlayer.setLocation(playerMovePacket.getLoc());
            VirtualPlayerEntity virtualPlayerEntity = getVirtualPlayer(networkedPlayer);
            if (virtualPlayerEntity == null) return;
            virtualPlayerEntity.setLoc(meLocToBukkit(networkedPlayer.getLocation()));
            virtualPlayerEntity.teleport();
        }
    }

    @EventHandler
    public void onPose(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof PlayerPosePacket posePacket){
            UUID uuid = posePacket.getUuid();
            Player player = Bukkit.getPlayer(uuid);
            if(player != null)
                return;

            NetworkedPlayer networkedPlayer = plugin.getNetworkedPlayer(uuid);

            VirtualPlayerEntity virtualPlayerEntity = getVirtualPlayer(networkedPlayer);
            if (virtualPlayerEntity == null) return;
            virtualPlayerEntity.setPose(bukkitToNMSPose(posePacket.getPose()));
        }
    }

    private Pose bukkitToNMSPose(org.bukkit.entity.Pose bukkitPose){
        return switch (bukkitPose){
            case STANDING -> Pose.STANDING;
            case FALL_FLYING -> Pose.FALL_FLYING;
            case SLEEPING -> Pose.SLEEPING;
            case SWIMMING -> Pose.SWIMMING;
            case SPIN_ATTACK -> Pose.SPIN_ATTACK;
            case SNEAKING -> Pose.CROUCHING;

            case LONG_JUMPING -> Pose.LONG_JUMPING;
            case DYING -> Pose.DYING;
            case CROAKING -> Pose.CROAKING;
            case USING_TONGUE -> Pose.USING_TONGUE;
            case ROARING -> Pose.ROARING;
            case SNIFFING -> Pose.SNIFFING;
            case EMERGING -> Pose.EMERGING;
            case DIGGING -> Pose.DIGGING;
        };
    }

    //on animate
    @EventHandler
    public void onAnimate(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof PlayerAnimatePacket animatePacket){
            UUID uuid = animatePacket.getUuid();
            Player player = Bukkit.getPlayer(uuid);
            if(player != null)
                return;

            NetworkedPlayer networkedPlayer = plugin.getNetworkedPlayer(uuid);

            VirtualPlayerEntity virtualPlayerEntity = getVirtualPlayer(networkedPlayer);
            if (virtualPlayerEntity == null) return;
            virtualPlayerEntity.animate(animatePacket.getAnimationID());
        }
    }

    //on equip item
    @EventHandler
    public void onEquip(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if (packet instanceof PlayerEquipmentPacket equipmentPacket){
            UUID uuid = equipmentPacket.getUuid();
            Player player = Bukkit.getPlayer(uuid);
            if(player != null)
                return;

            NetworkedPlayer networkedPlayer = plugin.getNetworkedPlayer(uuid);

            VirtualPlayerEntity virtualPlayerEntity = getVirtualPlayer(networkedPlayer);
            if (virtualPlayerEntity == null) return;
            virtualPlayerEntity.setEquipment(getEquipmentSlot(equipmentPacket.getEquipmentSlot()), equipmentPacket.getItemStack());
        }
    }

    private EquipmentSlot getEquipmentSlot(String name){
        return EquipmentSlot.byName(name);
    }

    @EventHandler
    public void onJoin(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof PlayerLeavePacket playerLeavePacket){
            UUID uuid = playerLeavePacket.getUuid();
            Player player = Bukkit.getPlayer(uuid);
            if(player != null)
                return;

            NetworkedPlayer networkedPlayer = plugin.getNetworkedPlayer(uuid);
            VirtualPlayerEntity virtualPlayerEntity = getVirtualPlayer(networkedPlayer);
            if (virtualPlayerEntity == null) return;
            virtualPlayerEntity.removeEntity();
            virtualPlayerMap.remove(uuid);
        }
    }

    //update new player
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        for(VirtualPlayerEntity virtualPlayerEntity : virtualPlayerMap.values()){
            virtualPlayerEntity.spawnEntity();
        }
    }

    //update second to clear old players incase
    @EventHandler
    public void onUpdate(UpdateSecondEvent event){
        Set<UUID> networkedPlayers = plugin.networkedPlayerIDs();
        for(UUID uuid : virtualPlayerMap.keySet()){
            //remove if local
            if(!networkedPlayers.contains(uuid) || Bukkit.getPlayer(uuid) != null){
                virtualPlayerMap.get(uuid).removeEntity();
                virtualPlayerMap.remove(uuid);
            }
        }
    }

    public VirtualPlayerEntity getVirtualPlayer(NetworkedPlayer networkedPlayer){
        if (networkedPlayer == null) return null;
        return virtualPlayerMap.computeIfAbsent(networkedPlayer.getUuid(), key -> new VirtualPlayerEntity(meLocToBukkit(networkedPlayer.getLocation()), networkedPlayer));
    }

    public Location meLocToBukkit(MELoc meLoc){
        String worldName = meLoc.getWorldName();
        double x = meLoc.getX();
        double y = meLoc.getY();
        double z = meLoc.getZ();

        float pitch = meLoc.getPitch();
        float yaw = meLoc.getYaw();

        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);

    }
}
