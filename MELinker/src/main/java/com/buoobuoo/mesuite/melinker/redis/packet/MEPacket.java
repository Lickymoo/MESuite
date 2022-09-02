package com.buoobuoo.mesuite.melinker.redis.packet;

import com.google.gson.Gson;
import lombok.Getter;

@Getter
public abstract class MEPacket {
    public static final String ID_DELIMITER = "\uFFFF";

    public String toString(){
        return new Gson().toJson(this);
    }
}
