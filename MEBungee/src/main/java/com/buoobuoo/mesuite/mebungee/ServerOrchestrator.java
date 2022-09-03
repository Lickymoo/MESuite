package com.buoobuoo.mesuite.mebungee;

import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketBungeeEvent;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.InstanceboundPingPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.RegisterServerPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.RegisterServerResponsePacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.InstanceboundPlayerDataPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.ProxyboundPlayerDataPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer.PlayerMovePacket;
import com.buoobuoo.mesuite.melinker.util.MELoc;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

public class ServerOrchestrator implements Listener {
    private final MEBungeePlugin plugin;

    public ServerOrchestrator(MEBungeePlugin plugin){
        this.plugin = plugin;

        //broadcast player data
        plugin.getProxy().getScheduler().schedule(plugin, this::broadcastPlayerData, 5, 200, TimeUnit.MILLISECONDS);

        //keepalive timer
        plugin.getProxy().getScheduler().schedule(plugin, this::keepAlivePing, 5, 5, TimeUnit.SECONDS);
    }

    private final Set<String> connectedInstances = new ConcurrentSkipListSet<>();
    private final Map<String, Set<NetworkedPlayer>> playerData = new HashMap<>();

    //Broadcast networked player data
    public void broadcastPlayerData(){
        InstanceboundPlayerDataPacket broadcastPacket = new InstanceboundPlayerDataPacket(getAllPlayers());
        plugin.getMeLinker().getPacketManager().sendPacket(broadcastPacket);
    }

    public Set<NetworkedPlayer> getAllPlayers(){
        Set<NetworkedPlayer> networkedPlayers = new HashSet<>();
        for(Set<NetworkedPlayer> instancePlayers : playerData.values()){
            networkedPlayers.addAll(instancePlayers);
        }
        return networkedPlayers;
    }

    //Keep alive ping
    public void keepAlivePing(){
        for(String name : connectedInstances){
            InstanceboundPingPacket packet = new InstanceboundPingPacket(name);
            plugin.getMeLinker().getPacketManager().sendCallbackPacket(packet, 1, () -> {
                plugin.getLogger().warning("Could not reach server " + name);
                plugin.getLogger().warning("Removing instance " + name);
                plugin.getProxy().getServers().remove(name);
                connectedInstances.remove(name);
            });
        }
    }

    //Registration
    public boolean registerServer(String name, String motd, SocketAddress address, boolean restricted){
        ServerInfo serverInfo = plugin.getProxy().constructServerInfo(name, address, motd, restricted);
        ProxyServer.getInstance().getServers().put(name, serverInfo);

        return serverInfo != null;
    }

    @EventHandler
    public void onRegisterPacket(MEPacketBungeeEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof RegisterServerPacket registerServerPacket){
            UUID target = registerServerPacket.getTarget();
            RegisterServerResponsePacket responsePacket = new RegisterServerResponsePacket();

            String name = registerServerPacket.getName();
            String motd = registerServerPacket.getMotd();
            SocketAddress address = new InetSocketAddress(registerServerPacket.getAddress(), registerServerPacket.getPort());
            boolean restricted = registerServerPacket.isRestricted();

            boolean accepted = registerServer(name, motd, address, restricted);
            responsePacket.setAccepted(accepted);

            if(accepted) {
                plugin.getLogger().info("Registered server " + name + "(" + address + ")");
                connectedInstances.add(name);
            }
            registerServerPacket.respond(plugin.getMeLinker().getPacketManager(), responsePacket);
        }
    }

    @EventHandler
    public void onReceivePlayerData(MEPacketBungeeEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof ProxyboundPlayerDataPacket proxyboundPlayerDataPacket){
            Set<NetworkedPlayer> connectedPlayers = proxyboundPlayerDataPacket.getConnecetedPlayers();
            String serverName = proxyboundPlayerDataPacket.getServerName();
            playerData.put(serverName, connectedPlayers);
        }
    }
}
