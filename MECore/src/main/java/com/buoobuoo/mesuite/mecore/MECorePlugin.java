package com.buoobuoo.mesuite.mecore;

import com.buoobuoo.mesuite.mecore.command.impl.ServerCommand;
import com.buoobuoo.mesuite.mecore.gamehandler.event.UpdateSecondEvent;
import com.buoobuoo.mesuite.mecore.gamehandler.event.UpdateTickEvent;
import com.buoobuoo.mesuite.mecore.gamehandler.listener.AnvilRenamePacketListener;
import com.buoobuoo.mesuite.mecore.gamehandler.listener.ProjectileHitEventListener;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.meinventories.CustomInventoryManager;
import com.buoobuoo.mesuite.meinventories.MEInventoriesPlugin;
import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketEvent;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.PlayerDataBroadcastPacket;
import com.buoobuoo.mesuite.melinker.redis.spigot.SpigotMELinker;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.meutils.command.CommandManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
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
    private List<NetworkedPlayer> networkPlayerData = new ArrayList<>();

    @Override
    public void initDependencies(){
        this.meInventoriesPlugin = (MEInventoriesPlugin) Bukkit.getPluginManager().getPlugin("MEInventories");
        this.inventoryManager = meInventoriesPlugin.getInventoryManager();
    }

    @Override
    public void initManagers(){
        this.meLinker = new SpigotMELinker(this);
        this.mongoHook = new MongoHook(this);
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.commandManager = new CommandManager(this);
    }

    @Override
    public void initListeners(){
        registerEvents(
                new ProjectileHitEventListener(this)
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
    }

    public static World getMainWorld(){
        return Bukkit.getWorld(MAIN_WORLD_NAME);
    }


    @EventHandler
    public void onPacket(MEPacketEvent event){
        MEPacket packet = event.getPacket();
        if(packet instanceof PlayerDataBroadcastPacket playerDataBroadcastPacket){
            networkPlayerData = playerDataBroadcastPacket.getPlayerData();
            for(NetworkedPlayer np : networkPlayerData){
            }
        }
    }

    public NetworkedPlayer getNetworkedPlayer(UUID uuid){
        for(NetworkedPlayer networkedPlayer : networkPlayerData){
            if(networkedPlayer.getUuid().equals(uuid))
                return networkedPlayer;
        }
        return null;
    }
}
