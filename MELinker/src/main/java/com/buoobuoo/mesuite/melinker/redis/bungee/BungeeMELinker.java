package com.buoobuoo.mesuite.melinker.redis.bungee;

import com.buoobuoo.mesuite.melinker.util.Util;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class BungeeMELinker {

    private final Plugin plugin;

    public static final String ME_SUITE_PUBLIC_CHANNEL = "MEC1";
    public static final String REDIS_ADDRESS = "redis-19075.c296.ap-southeast-2-1.ec2.cloud.redislabs.com";
    public static final int REDIS_PORT = 19075;
    public static final String REDIS_USERNAME = "default";
    public static final String REDIS_PASSWORD = "1C1ntiQGvhnO3tBip3G7Az695y0myTFN";

    //local managers
    private BungeeRedisConnection redisConnection;
    private BungeeChannelHandler channelHandler;
    private BungeePacketManager packetManager;

    private String serverID;

    public BungeeMELinker(Plugin plugin){
        this.plugin = plugin;
        serverID = Util.randomString();
        plugin.getLogger().info("Registered server with ID: " + serverID);

        this.redisConnection = new BungeeRedisConnection(plugin, REDIS_ADDRESS, REDIS_PORT, REDIS_USERNAME, REDIS_PASSWORD);
        this.packetManager = new BungeePacketManager(plugin, redisConnection);
        this.channelHandler = new BungeeChannelHandler(plugin, redisConnection, packetManager, ME_SUITE_PUBLIC_CHANNEL);

        plugin.getProxy().getPluginManager().registerListener(plugin, packetManager);
    }
}
