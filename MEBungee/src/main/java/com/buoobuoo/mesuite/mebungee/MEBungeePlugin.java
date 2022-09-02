package com.buoobuoo.mesuite.mebungee;

import com.buoobuoo.mesuite.mebungee.gamehandler.listener.MovePlayerPacketListener;
import com.buoobuoo.mesuite.mebungee.party.PartyManager;
import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketBungeeEvent;
import com.buoobuoo.mesuite.melinker.redis.bungee.BungeeMELinker;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.PlayerDataBroadcastPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.InstanceboundPingPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.RegisterServerPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.RegisterServerResponsePacket;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

@Getter
public class MEBungeePlugin extends Plugin implements Listener {
    //local managers
    private BungeeMELinker meLinker;
    private PartyManager partyManager;
    private Set<String> connectedInstances = new ConcurrentSkipListSet<>();

    @Override
    public void onEnable() {
        this.meLinker = new BungeeMELinker(this);
        this.partyManager = new PartyManager(this);
        initListeners();
        initTimers();
    }

    public void initTimers(){
        //Send player data every second
        getProxy().getScheduler().schedule(this,
                () -> {
                    List<NetworkedPlayer> playerData = new ArrayList<>();
                    for(ProxiedPlayer player : getProxy().getPlayers()){
                        String connectedServer = player.getServer().getInfo().getName();
                        playerData.add(new NetworkedPlayer(player.getName(), player.getUniqueId(), connectedServer));
                    }
                    PlayerDataBroadcastPacket packet = new PlayerDataBroadcastPacket(playerData);
                    meLinker.getPacketManager().sendPacket(packet);
                }, 5, 1, TimeUnit.SECONDS);

        //ping instances
        getProxy().getScheduler().schedule(this,
                this::pingServers, 5, 5, TimeUnit.SECONDS);

    }

    public void initListeners(){
        registerEvents(
                this,
                partyManager,

                new MovePlayerPacketListener(this)
        );
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
                getLogger().info("Registered server " + name + "(" + address + ")");
                connectedInstances.add(name);
            }
            registerServerPacket.respond(meLinker.getPacketManager(), responsePacket);
        }
    }

    public boolean registerServer(String name, String motd, SocketAddress address, boolean restricted){
        ServerInfo serverInfo = getProxy().constructServerInfo(name, address, motd, restricted);
        ProxyServer.getInstance().getServers().put(name, serverInfo);

        return serverInfo != null;
    }

    public void pingServers(){
        for(String name : connectedInstances){
            InstanceboundPingPacket packet = new InstanceboundPingPacket(name);
            meLinker.getPacketManager().sendCallbackPacket(packet, 1, () -> {
                getLogger().warning("Could not reach server " + name);
                getLogger().warning("Removing instance " + name);
                connectedInstances.remove(name);
                getProxy().getServers().remove(name);
            });
        }
    }

    public void registerEvents(Listener... listeners){
        for(Listener listener : listeners){
            getProxy().getPluginManager().registerListener(this, listener);
        }
    }
}
