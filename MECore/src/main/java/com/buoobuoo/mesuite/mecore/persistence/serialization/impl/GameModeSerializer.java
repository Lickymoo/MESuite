package com.buoobuoo.mesuite.mecore.persistence.serialization.impl;

import com.buoobuoo.mesuite.mecore.persistence.serialization.VariableSerializer;
import org.bukkit.GameMode;

public class GameModeSerializer extends VariableSerializer<GameMode> {

    @Override
    public String serialize(GameMode obj) {
        return obj.name();
    }

    @Override
    public GameMode deserialize(String str) {
        return GameMode.valueOf(str);
    }
}
