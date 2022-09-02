package com.buoobuoo.mesuite.melinker.redis.packet.impl.net;

import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class InstanceboundPingPacket extends MECallbackPacket<InstanceboundPingResponsePacket> {

    private String serverName;

    @Override
    public void onRespond(InstanceboundPingResponsePacket packet) {

    }
}
