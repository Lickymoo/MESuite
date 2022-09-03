package com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class ProxyboundPlayerDataPacket extends MEPacket {
    private final String serverName;
    private final Set<NetworkedPlayer> connecetedPlayers;
}
