package com.buoobuoo.mesuite.melinker.redis.packet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public abstract class MECallbackResponsePacket extends MEPacket{
    private UUID target;
}
