package com.buoobuoo.mesuite.melinker.redis.packet.impl.net;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChangePlayerServerPacket extends MEPacket {

    private final UUID playerID;
    private final String serverName;

}
