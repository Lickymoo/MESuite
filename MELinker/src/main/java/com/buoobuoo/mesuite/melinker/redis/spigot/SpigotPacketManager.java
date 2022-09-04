package com.buoobuoo.mesuite.melinker.redis.spigot;

import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketEvent;
import com.buoobuoo.mesuite.melinker.redis.AbsPacketManager;
import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackResponsePacket;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer.PlayerPosePacket;
import com.buoobuoo.mesuite.melinker.util.Util;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SpigotPacketManager implements AbsPacketManager, Listener {
    private final JavaPlugin plugin;
    private final SpigotRedisConnection redisConnection;
    private final Map<String, Class<? extends MEPacket>> packetRegistry = new HashMap<>();

    public SpigotPacketManager(JavaPlugin plugin, SpigotRedisConnection redisConnection){
        this.plugin = plugin;
        this.redisConnection = redisConnection;
        Util.registerDefaultPackets(this);
        Util.registerSpigotDefaultPackets(this);
        //spigot specific
        plugin.getLogger().info("Registered " + packetRegistry.size() + " packets");
    }

    public void registerPacket(String packetID, Class<? extends MEPacket> packetClass){
        this.packetRegistry.putIfAbsent(packetID, packetClass);
    }

    public String getPacketID(MEPacket packet){
        for(Map.Entry<String, Class<? extends MEPacket>> entry : packetRegistry.entrySet()){
            String id = entry.getKey();
            Class<? extends MEPacket> packetClass = entry.getValue();

            if(packetClass.equals(packet.getClass()))
                return id;
        }
        return null;
    }

    public void handleMessage(String message){
        String id = message.split(MEPacket.ID_DELIMITER)[0];
        String data = message.split(MEPacket.ID_DELIMITER)[1];

        Class<? extends MEPacket> packetClass = packetRegistry.get(id);
        if (packetClass == null) {
            //plugin.getLogger().warning("Received packet " + id + " which has not been registered. Ignoring packet.");
            return;
        }

        MEPacket packet = new Gson().fromJson(data, packetClass);
        MEPacketEvent mePacketEvent = new MEPacketEvent(packet);

        //run event sync
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.getPluginManager().callEvent(mePacketEvent);
        });
    }

    public void sendPacket(MEPacket packet){
        String packetID = getPacketID(packet);
        String packetData = packet.toString();
        this.redisConnection.executeAsync(jedis -> jedis.publish(SpigotMELinker.ME_SUITE_PUBLIC_CHANNEL, packetID + MEPacket.ID_DELIMITER + packetData));
    }

    @Override
    public void sendCallbackPacket(MECallbackPacket<?> packet, int timeout, Runnable timeOutRunnable) {
        String packetID = getPacketID(packet);
        String packetData = packet.toString();
        this.redisConnection.executeAsync(jedis -> jedis.publish(SpigotMELinker.ME_SUITE_PUBLIC_CHANNEL, packetID + MEPacket.ID_DELIMITER + packetData));
        activeCallbackPackets.put(packet.getTarget(), (MECallbackPacket<MECallbackResponsePacket>) packet);

        if(timeOutRunnable != null)
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                packet.setTimeout(true);
                if(activeCallbackPackets.containsKey(packet.getTarget()))
                    timeOutRunnable.run();
            }, timeout * 20L);
    }

    private final Map<UUID, MECallbackPacket<MECallbackResponsePacket>> activeCallbackPackets = new HashMap<>();
    @EventHandler
    public void onReceive(MEPacketEvent event){
        //check if is callback
        MEPacket packet = event.getPacket();
        if(packet instanceof MECallbackResponsePacket responsePacket){
            UUID uuid = responsePacket.getTarget();
            MECallbackPacket<MECallbackResponsePacket> callbackPacket = activeCallbackPackets.get(uuid);
            if(callbackPacket == null) return;
            activeCallbackPackets.remove(uuid);
            callbackPacket.onPreRespond(responsePacket);
        }
    }
}
