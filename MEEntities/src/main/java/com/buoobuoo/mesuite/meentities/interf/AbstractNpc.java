package com.buoobuoo.mesuite.meentities.interf;

import com.buoobuoo.mesuite.meentities.MEEntitiesPlugin;
import com.buoobuoo.mesuite.meentities.impl.util.NpcMirrorEntity;
import com.buoobuoo.mesuite.meentities.interf.net.NetworkManager;
import com.buoobuoo.mesuite.meentities.interf.net.ServerGamePacketListener;
import com.buoobuoo.mesuite.meentities.interf.tags.Invisible;
import com.buoobuoo.mesuite.meutils.PacketUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public abstract class AbstractNpc extends ServerPlayer implements NpcEntity {

    public AbstractNpc(Location loc) {
        super(((CraftWorld) loc.getWorld()).getHandle().getServer(), ((CraftWorld) loc.getWorld()).getHandle().getLevel(), new GameProfile(UUID.randomUUID(), ""), null);

        MinecraftServer minecraftServer = ((CraftWorld) loc.getWorld()).getHandle().getServer();
        this.connection = new ServerGamePacketListener(minecraftServer, new NetworkManager(PacketFlow.CLIENTBOUND), this);

        this.getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0xFF);
    }

    @Override
    public void updateTick(){

        NpcMirrorEntity mirrorEntity = getChild("MIRROR_ENTITY");
        Location loc = mirrorEntity.asEntity().getBukkitEntity().getLocation();
        if (loc.distance(asEntity().getBukkitEntity().getLocation()) == 0) {
            for(Entity entity : loc.getWorld().getNearbyEntities(loc, 10, 10 ,10)) {
                if (!(entity instanceof Player))
                    continue;
                loc.setDirection(entity.getLocation().subtract(loc).toVector());
            }
        }

        double mirrorX = loc.getX();
        double mirrorY = loc.getY();
        double mirrorZ = loc.getZ();

        byte yaw = (byte) ((loc.getYaw() % 360) * 256 / 360);
        byte pitch = (byte) ((loc.getPitch() % 360) * 256 / 360);

        asEntity().moveTo(mirrorX, mirrorY, mirrorZ, yaw, pitch);
        PacketUtils.sendPacketGlobal(new ClientboundRotateHeadPacket(this, yaw));
        PacketUtils.sendPacketGlobal(new ClientboundTeleportEntityPacket(asEntity()));
    }

    @Override
    public void spawnEntity(MEEntitiesPlugin plugin, Location loc){
        setDead(false);
        setIsReady(true);
        net.minecraft.world.entity.Entity ent = asEntity();
        ((ServerPlayer)this).getGameProfile().getProperties().put("textures", new Property("textures", textureBase64(), textureSignature()));

        ent.setPos(loc.getX(), loc.getY(), loc.getZ());

        PersistentDataContainer pdc = ent.getBukkitEntity().getPersistentDataContainer();
        pdc.set(NamespacedKey.minecraft("me-ent-id"), PersistentDataType.STRING, entityID());
        pdc.set(NamespacedKey.minecraft("me-health"), PersistentDataType.DOUBLE, maxHealth());
        pdc.set(NamespacedKey.minecraft("me-damage"), PersistentDataType.DOUBLE, damage());

        onSpawn();

        plugin.getEntityManager().registerEntities(this);

        NpcMirrorEntity mirrorEntity = (NpcMirrorEntity) plugin.getEntityManager().spawnEntity(NpcMirrorEntity.class, loc);
        addChild("MIRROR_ENTITY", mirrorEntity);

        hideScoreboard(plugin);
    }

    @Override
    public void destroyEntity(){
        isDestroyed.put(this, true);
        getChildren().forEach(CustomEntity::destroyEntity);
        PacketUtils.sendPacketGlobal(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, (ServerPlayer) asEntity()));
        PacketUtils.sendPacketGlobal(new ClientboundRemoveEntitiesPacket(asEntity().getBukkitEntity().getEntityId()));
    }

    @Override
    public void onDeath(){
        NpcEntity.super.onDeath();
        NpcMirrorEntity mirrorEntity = getChild("MIRROR_ENTITY");
        MEEntitiesPlugin.getInstance().getEntityManager().removeEntity(mirrorEntity);
    }

    @Override
    public PathfinderMob getPathfinderMob(){
        NpcMirrorEntity mirrorEntity = getChild("MIRROR_ENTITY");
        return mirrorEntity.getPathfinderMob();
    }

    private void hideScoreboard(MEEntitiesPlugin plugin){
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("");

        if(team == null)
            team = scoreboard.registerNewTeam("");
        team.addEntry("");
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    @Override
    public void showEntity(Player player) {
        getChildren().forEach(e -> e.showEntity(player));

        if(this instanceof Invisible){
            hideEntity(player);
            return;
        }

        if(!canShow(player))
            return;

        PacketUtils.sendPacket(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, (ServerPlayer) asEntity()), player);
        PacketUtils.sendPacket(new ClientboundAddPlayerPacket((ServerPlayer) asEntity()), player);
        PacketUtils.sendPacket(new ClientboundSetEntityDataPacket(asEntity().getBukkitEntity().getEntityId(), asEntity().getEntityData(), true), player);

        addShownPlayer(player.getUniqueId());
    }


    @Override
    public void hideEntity(Player player) {
        removeShownPlayer(player.getUniqueId());
        getChildren().forEach(e -> e.hideEntity(player));

        PacketUtils.sendPacket(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, (ServerPlayer) asEntity()), player);
        PacketUtils.sendPacket(new ClientboundRemoveEntitiesPacket(asEntity().getBukkitEntity().getEntityId()), player);
    }
}