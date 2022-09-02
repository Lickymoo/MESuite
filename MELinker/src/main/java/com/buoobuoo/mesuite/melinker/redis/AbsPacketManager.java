package com.buoobuoo.mesuite.melinker.redis;

import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;

public interface AbsPacketManager {

    public void registerPacket(String packetID, Class<? extends MEPacket> packetClass);

    public String getPacketID(MEPacket packet);

    public void handleMessage(String message);

    public void sendPacket(MEPacket packet);

    /**
     * Send a packet which requires a response
     * @param packet
     * @param timeout timeout time in seconds
     */
    public default void sendCallbackPacket(MECallbackPacket<?> packet, int timeout){
        sendCallbackPacket(packet, timeout, null);
    }

    /**
     * Send a packet which requires a response
     * @param packet
     * @param timeout timeout time in seconds
     * @param timeOutRunnable runnable if timeout is reached
     */
    public void sendCallbackPacket(MECallbackPacket<?> packet, int timeout, Runnable timeOutRunnable);

}
