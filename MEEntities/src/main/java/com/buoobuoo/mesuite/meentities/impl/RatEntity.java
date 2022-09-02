package com.buoobuoo.mesuite.meentities.impl;

import com.buoobuoo.mesuite.meentities.interf.ModelEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

public class RatEntity extends Wolf implements ModelEntity {
    public RatEntity(Location loc) {
        super(EntityType.WOLF, ((CraftWorld) loc.getWorld()).getHandle());
    }

    @Override
    public String entityID() {
        return "RAT";
    }

    @Override
    public String entityName() {
        return "Rat";
    }

    @Override
    public double maxHealth() {
        return 10;
    }

    @Override
    public double damage() {
        return 1;
    }

    @Override
    public double tagOffset() {
        return -1.5;
    }

    @Override
    public int entityLevel() {
        return 1;
    }

    @Override
    public String modelName() {
        return "rat";
    }
}


