package com.buoobuoo.mesuite.melinker.redis.spigot;

import com.buoobuoo.mesuite.melinker.redis.AbsRedisConnection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.function.Consumer;

public final class SpigotRedisConnection implements AbsRedisConnection {

    private final JavaPlugin plugin;

    private final String address;
    private final int port;

    private final String username;
    private final String password;

    public SpigotRedisConnection(JavaPlugin plugin, String address, int port, String username, String password){
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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            //use local variable to reduce async clash
            Jedis tmpJedis = new Jedis(address, port);
            tmpJedis.auth(username, password);
            consumer.accept(tmpJedis);
            tmpJedis.close();
        });
    }
}
