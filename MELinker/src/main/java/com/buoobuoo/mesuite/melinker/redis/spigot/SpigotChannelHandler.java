package com.buoobuoo.mesuite.melinker.redis.spigot;

import com.buoobuoo.mesuite.melinker.redis.AbsChannelHandler;
import com.buoobuoo.mesuite.melinker.redis.AbsPacketManager;
import com.buoobuoo.mesuite.melinker.redis.AbsRedisConnection;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPubSub;

@Getter
public final class SpigotChannelHandler implements AbsChannelHandler {

    private final String channel;
    private final JavaPlugin plugin;
    private final AbsPacketManager packetManager;
    private final AbsRedisConnection redisConnection;

    private JedisPubSub pubSub;

    public SpigotChannelHandler(JavaPlugin plugin, AbsRedisConnection redisConnection, AbsPacketManager packetManager, String channel) {
        this.plugin = plugin;
        this.channel = channel;
        this.packetManager = packetManager;
        this.redisConnection = redisConnection;

        subscribe();
    }

    public void subscribe(){
        //subscribe async
        redisConnection.executeAsync(jedis -> {
            this.pubSub = new JedisPubSub() {

                @Override
                public void onMessage(String channel, String message) {
                    packetManager.handleMessage(message);
                }

                @Override
                public void onSubscribe(String channel, int subscribedChannels) {
                    plugin.getLogger().info("Channel Handler subscribed to channel: " + channel);
                }

                @Override
                public void onUnsubscribe(String channel, int subscribedChannels) {
                    plugin.getLogger().info("Channel Handler unsubscribed to channel: " + channel);
                }
            };
            jedis.subscribe(pubSub, channel);
        });
    }
}
