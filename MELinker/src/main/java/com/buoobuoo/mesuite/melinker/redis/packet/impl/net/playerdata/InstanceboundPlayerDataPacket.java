package com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class InstanceboundPlayerDataPacket extends MEPacket {
    private Set<NetworkedPlayer> playerData;

    public InstanceboundPlayerDataPacket(Set<NetworkedPlayer>  playerData) {
        this.playerData = playerData;
    }
}
