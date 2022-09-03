package com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Pose;

import java.util.UUID;

@Getter
public class PlayerPosePacket extends MEPacket {
    private final UUID uuid;

    private final String pose;

    public Pose getPose(){
        return Pose.valueOf(pose);
    }

    public PlayerPosePacket(UUID uuid, Pose pose){
        this.uuid = uuid;
        this.pose = pose.name();
    }
}
