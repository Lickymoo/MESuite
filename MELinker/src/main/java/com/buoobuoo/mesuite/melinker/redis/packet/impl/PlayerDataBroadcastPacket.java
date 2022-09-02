package com.buoobuoo.mesuite.melinker.redis.packet.impl;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayerDataBroadcastPacket extends MEPacket {
    private List<NetworkedPlayer> playerData;

    public PlayerDataBroadcastPacket(List<NetworkedPlayer>  playerData) {
        this.playerData = playerData;
    }
}
