package com.buoobuoo.mesuite.melinker.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class NetworkedPlayer {
    private final String name;
    private final UUID uuid;

    private String skinValue;
    private String skinSignature;

    private String connectedServer;

    //this is not kept track of by proxy
    private MELoc location;

    public NetworkedPlayer(String name, UUID uuid){
        this.name = name;
        this.uuid = uuid;
    }


}
