package com.buoobuoo.mesuite.meentities.interf.net;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;

public class NetworkManager extends Connection {

    public NetworkManager(PacketFlow enumprotocoldirection) {

        super(enumprotocoldirection);
    }
}