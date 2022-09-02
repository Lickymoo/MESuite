package com.buoobuoo.mesuite.meentities.perseistence;

import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Location;

@Getter
public class SuspendedInstance {
    private final CustomEntity entity;
    private final String serializedNBTCompound;
    private final Location location;

    public SuspendedInstance(CustomEntity entity, CompoundTag compoundTag, Location location){
        this.entity = entity;
        this.serializedNBTCompound = compoundTag.toString();
        this.location = location;
    }

}
