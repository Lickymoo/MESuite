package com.buoobuoo.mesuite.melinker.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class NetworkedPlayer {
    private final String name;
    private final UUID uuid;
    private final String connectedServer;
}
