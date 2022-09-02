package com.buoobuoo.mesuite.melinker.redis.packet.impl.net;

import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;


@Getter
@Setter
@AllArgsConstructor
public class RegisterServerPacket extends MECallbackPacket<RegisterServerResponsePacket> {

    private String address;
    private int port;
    private String name;
    private String motd;
    private boolean restricted;

    private transient Consumer<RegisterServerResponsePacket> responsePacketConsumer;

    @Override
    public void onRespond(RegisterServerResponsePacket packet) {
        if(responsePacketConsumer != null)
            responsePacketConsumer.accept(packet);
    }
}
