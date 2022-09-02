package com.buoobuoo.mesuite.melinker.redis.spigot;

import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketEvent;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.InstanceboundPingPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.InstanceboundPingResponsePacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.RegisterServerPacket;
import com.buoobuoo.mesuite.melinker.util.Util;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SpigotMELinker implements Listener {

    private final JavaPlugin plugin;

    public static final String ME_SUITE_PUBLIC_CHANNEL = "MEC1";
    public static final String REDIS_ADDRESS = "redis-19075.c296.ap-southeast-2-1.ec2.cloud.redislabs.com";
    public static final int REDIS_PORT = 19075;
    public static final String REDIS_USERNAME = "default";
    public static final String REDIS_PASSWORD = "1C1ntiQGvhnO3tBip3G7Az695y0myTFN";

    //local managers
    private SpigotRedisConnection redisConnection;
    private SpigotChannelHandler channelHandler;
    private SpigotPacketManager packetManager;

    private String serverID;

    public SpigotMELinker(JavaPlugin plugin){
        this(plugin, false);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    public SpigotMELinker(JavaPlugin plugin, boolean headless){

        this.plugin = plugin;

        this.redisConnection = new SpigotRedisConnection(plugin, REDIS_ADDRESS, REDIS_PORT, REDIS_USERNAME, REDIS_PASSWORD);
        this.packetManager = new SpigotPacketManager(plugin, redisConnection);
        this.channelHandler = new SpigotChannelHandler(plugin, redisConnection, packetManager, ME_SUITE_PUBLIC_CHANNEL);

        plugin.getServer().getPluginManager().registerEvents(packetManager, plugin);

        if(!headless) {
            serverID = Util.randomString();
            plugin.getLogger().info("Registered server with ID: " + serverID);
            attemptRegister();
        }
    }

    public void attemptRegister(){
        int port = plugin.getServer().getPort();
        RegisterServerPacket packet = new RegisterServerPacket("localhost", port, serverID, "", false, responsePacket -> plugin.getLogger().info("Successfully connected to proxy"));
        packetManager.sendCallbackPacket(packet, 3, () -> {
            plugin.getLogger().warning("Could not register server");
            plugin.getLogger().warning("Reattempting...");
            attemptRegister();
        });
    }

    @EventHandler
    public void onPing(MEPacketEvent event){
        MEPacket packet = event.getPacket();
        if(packet instanceof InstanceboundPingPacket instanceboundPingPacket){
            if(instanceboundPingPacket.getServerName().equals(serverID)){
                InstanceboundPingResponsePacket responsePacket = new InstanceboundPingResponsePacket();

                instanceboundPingPacket.respond(packetManager, responsePacket);
            }
        }
    }


}
