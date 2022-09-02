package com.buoobuoo.mesuite.meentities.impl.util;

import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import com.buoobuoo.mesuite.meentities.interf.tags.HideHealthTag;
import com.buoobuoo.mesuite.meentities.interf.tags.HideNameTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

//Used to mirror actions onto npc
public class EmptyEntity extends ArmorStand implements CustomEntity, HideHealthTag, HideNameTag {

    public EmptyEntity(Location loc) {
        super(EntityType.ARMOR_STAND, ((CraftWorld) loc.getWorld()).getHandle());

        this.setInvulnerable(true);
        this.setSilent(true);
        this.setMarker(true);
        this.setInvisible(true);
        this.setCustomNameVisible(false);
        this.setDestroyOnUnload(true);
    }

    @Override
    public String entityID() {
        return "EMPTY_ENTITY";
    }

    @Override
    public String entityName() {
        return "EMPTY_ENTITY";
    }

}
