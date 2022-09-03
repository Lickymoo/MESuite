package com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.util.MELoc;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PlayerMovePacket extends MEPacket {
    private final UUID uuid;

    private final MELoc loc;
}
