package com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.util.MELoc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

import java.util.UUID;

@Getter
public class PlayerAnimatePacket extends MEPacket {
    private final UUID uuid;
    private final int animationID;

    public PlayerAnimatePacket(UUID uuid, PlayerAnimation animation){
        this.uuid = uuid;
        this.animationID = animation.getVal();
    }


    @Getter
    public enum PlayerAnimation{
        SWING_MAIN_HAND(0),
        HURT(1),
        WAKE_UP(2),
        SWING_OFF_HAND(3),
        CRITICAL_HIT(4),
        MAGIC_CRITICAL_HIT(5);

        private int val;

        PlayerAnimation(int val){
            this.val = val;
        }
    }
}
