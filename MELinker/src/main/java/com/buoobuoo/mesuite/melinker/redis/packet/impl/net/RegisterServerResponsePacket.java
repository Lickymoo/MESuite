package com.buoobuoo.mesuite.melinker.redis.packet.impl.net;

import com.buoobuoo.mesuite.melinker.redis.packet.MECallbackResponsePacket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterServerResponsePacket extends MECallbackResponsePacket {

    private boolean accepted = false;
}
