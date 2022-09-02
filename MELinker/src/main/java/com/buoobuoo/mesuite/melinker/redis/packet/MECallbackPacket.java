package com.buoobuoo.mesuite.melinker.redis.packet;

import com.buoobuoo.mesuite.melinker.redis.AbsPacketManager;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Getter
@Setter
public abstract class MECallbackPacket<T extends MECallbackResponsePacket> extends MEPacket{
    private final UUID target = UUID.randomUUID();
    private T response;
    private boolean timeout = false;

    public void respond(AbsPacketManager packetManager, T responsePacket){
        responsePacket.setTarget(target);
        packetManager.sendPacket(responsePacket);
    }

    public void onPreRespond(T packet){
        response = packet;
        onRespond(packet);
    }

    public abstract void onRespond(T packet);

    public T await(AbsPacketManager packetManager, int timeoutSeconds){
        packetManager.sendCallbackPacket(this, timeoutSeconds);
        try {
            CompletableFuture<T> future = CompletableFuture.supplyAsync(() -> {
                while (response == null && !timeout){
                    Thread.yield();
                }
                return response;
            });
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

}
