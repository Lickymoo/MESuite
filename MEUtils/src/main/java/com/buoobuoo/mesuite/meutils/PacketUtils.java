package com.buoobuoo.mesuite.meutils;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtils {

    public static void sendPacketGlobal(Packet<?> packet){
        sendPacket(packet, Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public static void sendPacket(Packet<?> packet, Player... players){

        for(Player p : players) {

            CraftPlayer cp = (CraftPlayer) p;
            ServerPlayer sp = cp.getHandle();

            ServerGamePacketListenerImpl ps = sp.connection;
            ps.send(packet);
        }
    }

}
