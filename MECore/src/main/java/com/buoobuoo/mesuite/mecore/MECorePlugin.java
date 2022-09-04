package com.buoobuoo.mesuite.mecore;

import com.buoobuoo.mesuite.mecore.command.impl.ServerCommand;
import com.buoobuoo.mesuite.mecore.gamehandler.event.UpdateSecondEvent;
import com.buoobuoo.mesuite.mecore.gamehandler.event.UpdateTickEvent;
import com.buoobuoo.mesuite.mecore.gamehandler.listener.*;
import com.buoobuoo.mesuite.mecore.gamehandler.listener.virtualplayer.PlayerAnimateEventListener;
import com.buoobuoo.mesuite.mecore.gamehandler.listener.virtualplayer.PlayerEquipmentEventListener;
import com.buoobuoo.mesuite.mecore.gamehandler.listener.virtualplayer.PlayerMoveEventListener;
import com.buoobuoo.mesuite.mecore.gamehandler.listener.virtualplayer.PlayerTogglePoseEventListener;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.mecore.virtualplayers.VirtualPlayerManager;
import com.buoobuoo.mesuite.meinventories.CustomInventoryManager;
import com.buoobuoo.mesuite.meinventories.MEInventoriesPlugin;
import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketEvent;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.InstanceboundPlayerDataPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.ProxyboundPlayerDataPacket;
import com.buoobuoo.mesuite.melinker.redis.spigot.SpigotMELinker;
import com.buoobuoo.mesuite.melinker.util.MELoc;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.meutils.command.CommandManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class MECorePlugin extends MEPlugin {

    public static final String MAIN_WORLD_NAME = "world";
    public static final String BRAND_CHANNEL = "minecraft:brand";

    //dependencies
    private MEInventoriesPlugin meInventoriesPlugin;
    private CustomInventoryManager inventoryManager;

    //local managers
    private SpigotMELinker meLinker;
    private MongoHook mongoHook;
    private ProtocolManager protocolManager;
    private CommandManager commandManager;

    //cache
    private VirtualPlayerManager virtualPlayerManager;
    private Set<NetworkedPlayer> networkPlayerData = new HashSet<>();

    @Override
    public void initDependencies(){
        this.meInventoriesPlugin = getPlugin(MEInventoriesPlugin.class);
        this.inventoryManager = meInventoriesPlugin.getInventoryManager();
    }

    @Override
    public void initManagers(){
        this.meLinker = new SpigotMELinker(this);
        this.mongoHook = new MongoHook(this);
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.commandManager = new CommandManager(this);
        this.virtualPlayerManager = new VirtualPlayerManager(this);

        try {
            Method registerMethod = this.getServer().getMessenger().getClass().getDeclaredMethod("addToOutgoing", Plugin.class, String.class);
            registerMethod.setAccessible(true);
            registerMethod.invoke(this.getServer().getMessenger(), this, MECorePlugin.BRAND_CHANNEL);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Error while attempting to register plugin message channel", e);
        }
    }

    @Override
    public void initListeners(){
        registerEvents(
                virtualPlayerManager,

                new ProjectileHitEventListener(this),
                new MoistureChangeEventListener(this),
                new ServerBrandListener(this),
                new BlockGrowEventListener(this),
                new PlayerJoinEventListener(this),
                new PlayerLeaveEventListener(this),
                new PlayerArmorEquipListener(new ArrayList<>()),

                //virtual player
                new PlayerMoveEventListener(this),
                new PlayerTogglePoseEventListener(this),
                new PlayerAnimateEventListener(this),
                new PlayerEquipmentEventListener(this)
        );

        new AnvilRenamePacketListener(this);
    }

    @Override
    public void defineCommands(){
        commandManager.getCommandCompletions().registerAsyncCompletion("networked-players-name", c -> getNetworkPlayerData().stream().map(NetworkedPlayer::getName).collect(Collectors.toList()));
        commandManager.getCommandContexts().registerContext(NetworkedPlayer.class, c -> {
            String name = c.popFirstArg();
            for(NetworkedPlayer networkedPlayer : getNetworkPlayerData()){
                if(networkedPlayer.getName().equalsIgnoreCase(name))
                    return networkedPlayer;
            }
            return null;
        });
        commandManager.registerCommand(new ServerCommand(this));
    }

    @Override
    public void initTimers(){
        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getPluginManager().callEvent(new UpdateTickEvent()), 0, 1);
        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getPluginManager().callEvent(new UpdateSecondEvent()), 0, 20);
        Bukkit.getScheduler().runTaskTimer(this, this::broadcastPlayerData, 0, 20);
    }

    public static World getMainWorld(){
        return Bukkit.getWorld(MAIN_WORLD_NAME);
    }


    @EventHandler
    public void onPacket(MEPacketEvent event){
        MEPacket packet = event.getPacket();
        if(packet instanceof InstanceboundPlayerDataPacket instanceboundPlayerDataPacket){
            networkPlayerData = instanceboundPlayerDataPacket.getPlayerData();
        }
    }

    public void broadcastPlayerData(){
        Set<NetworkedPlayer> playerData = new HashSet<>();
        for(Player player : Bukkit.getOnlinePlayers()){
            NetworkedPlayer networkedPlayer = new NetworkedPlayer(player.getDisplayName(), player.getUniqueId());
            networkedPlayer.setConnectedServer(meLinker.getServerID());
            playerData.add(networkedPlayer);

            //skin
            ServerPlayer serverPlayer = ((CraftPlayer)player).getHandle();
            Property property = serverPlayer.getGameProfile().getProperties().get("textures").iterator().next();

            String texture = property.getValue();
            String signature = property.getSignature();
            networkedPlayer.setSkinSignature(signature);
            networkedPlayer.setSkinValue(texture);

            Location loc = player.getLocation();

            String name = loc.getWorld().getName();
            double x = loc.getX();
            double y = loc.getY();
            double z = loc.getZ();

            float pitch = loc.getPitch();
            float yaw = loc.getYaw();

            MELoc location = new MELoc(name, x, y, z, pitch, yaw);
            networkedPlayer.setLocation(location);
        }

        ProxyboundPlayerDataPacket dataPacket = new ProxyboundPlayerDataPacket(meLinker.getServerID(), playerData);
        meLinker.getPacketManager().sendPacket(dataPacket);
    }

    public NetworkedPlayer getNetworkedPlayer(UUID uuid){
        for(NetworkedPlayer networkedPlayer : networkPlayerData){
            if(networkedPlayer.getUuid().equals(uuid))
                return networkedPlayer;
        }
        return null;
    }

    public Set<UUID> networkedPlayerIDs(){
        Set<UUID> ids = new HashSet<>();
        for(NetworkedPlayer networkedPlayer : networkPlayerData){
            ids.add(networkedPlayer.getUuid());
        }
        return ids;
    }
}
