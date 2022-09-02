package com.buoobuoo.mesuite.melinker.redis.bungee;

import com.buoobuoo.mesuite.melinker.redis.AbsRedisConnection;
import net.md_5.bungee.api.plugin.Plugin;
import redis.clients.jedis.Jedis;

import java.util.function.Consumer;

public final class BungeeRedisConnection implements AbsRedisConnection {

    private final Plugin plugin;

    private final String address;
    private final int port;

    private final String username;
    private final String password;

    public BungeeRedisConnection(Plugin plugin, String address, int port, String username, String password){
        this.plugin = plugin;

        this.address = address;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public void execute(Consumer<Jedis> consumer){
        Jedis tmpJedis = new Jedis(address, port);
        tmpJedis.auth(username, password);
        consumer.accept(tmpJedis);
        tmpJedis.close();
    }

    public void executeAsync(Consumer<Jedis> consumer){
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            //use local variable to reduce async clash
            Jedis tmpJedis = new Jedis(address, port);
            tmpJedis.auth(username, password);
            consumer.accept(tmpJedis);
            tmpJedis.close();
        });
    }
}
