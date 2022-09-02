package com.buoobuoo.mesuite.mecore.persistence.serialization.impl;

import com.buoobuoo.mesuite.mecore.persistence.serialization.VariableSerializer;
import org.bukkit.Material;

public class MaterialSerializer extends VariableSerializer<Material> {
    @Override
    public String serialize(Material obj) {
        return obj.name();
    }

    @Override
    public Material deserialize(String str) {
        return Material.valueOf(str);
    }
}
