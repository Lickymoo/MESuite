package com.buoobuoo.mesuite.mecore.virtualplayers;

import com.buoobuoo.mesuite.mecore.virtualplayers.net.NetworkManager;
import com.buoobuoo.mesuite.mecore.virtualplayers.net.ServerGamePacketListener;
import com.buoobuoo.mesuite.melinker.util.MELoc;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.meutils.PacketUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

import java.util.UUID;

@Getter
@Setter
public class VirtualPlayerEntity extends ServerPlayer {
    private final UUID playerID;
    private final String playerName;

    private final String skinValue;
    private final String skinSignature;

    private Location loc;

    public VirtualPlayerEntity(Location loc, NetworkedPlayer networkedPlayer) {
        super(((CraftWorld) loc.getWorld()).getHandle().getServer(), ((CraftWorld) loc.getWorld()).getHandle().getLevel(), new GameProfile(UUID.randomUUID(), networkedPlayer.getName()), null);

        this.playerID = networkedPlayer.getUuid();
        this.playerName = networkedPlayer.getName();

        this.skinSignature = networkedPlayer.getSkinSignature();
        this.skinValue = networkedPlayer.getSkinValue();
        this.loc = loc;

        MinecraftServer minecraftServer = ((CraftWorld) loc.getWorld()).getHandle().getServer();
        this.connection = new ServerGamePacketListener(minecraftServer, new NetworkManager(PacketFlow.CLIENTBOUND), this);

        this.getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0xFF);
        this.getGameProfile().getProperties().put("textures", new Property("textures", skinValue, skinSignature));

        spawnEntity();
    }

    public void teleport(){
        double mirrorX = loc.getX();
        double mirrorY = loc.getY();
        double mirrorZ = loc.getZ();

        byte yaw = (byte) ((loc.getYaw() % 360) * 256 / 360);
        byte pitch = (byte) ((loc.getPitch() % 360) * 256 / 360);

        moveTo(mirrorX, mirrorY, mirrorZ, yaw, pitch);
        PacketUtils.sendPacketGlobal(new ClientboundRotateHeadPacket(this, yaw));
        PacketUtils.sendPacketGlobal(new ClientboundTeleportEntityPacket(this));
    }

    public void spawnEntity(){
        setPos(loc.getX(), loc.getY(), loc.getZ());

        PacketUtils.sendPacketGlobal(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, this));
        PacketUtils.sendPacketGlobal(new ClientboundAddPlayerPacket(this));
        PacketUtils.sendPacketGlobal(new ClientboundSetEntityDataPacket(getBukkitEntity().getEntityId(), getEntityData(), true));
        update();
    }

    public void removeEntity(){
        PacketUtils.sendPacketGlobal(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, this));
        PacketUtils.sendPacketGlobal(new ClientboundRemoveEntitiesPacket(getBukkitEntity().getEntityId()));
    }

    public void animate(int animationID){
        PacketUtils.sendPacketGlobal(new ClientboundAnimatePacket(this, animationID));
    }

    public void setPose(Pose pose){
        super.setPose(pose);
        update();
    }

    public void update(){

        PacketUtils.sendPacketGlobal(new ClientboundSetEntityDataPacket(getBukkitEntity().getEntityId(), getEntityData(), true));

    }
}
