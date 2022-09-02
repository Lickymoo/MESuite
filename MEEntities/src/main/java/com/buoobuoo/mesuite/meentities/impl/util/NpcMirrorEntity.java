package com.buoobuoo.mesuite.meentities.impl.util;

import com.buoobuoo.mesuite.meentities.MEEntitiesPlugin;
import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import com.buoobuoo.mesuite.meentities.interf.tags.HideHealthTag;
import com.buoobuoo.mesuite.meentities.interf.tags.HideNameTag;
import com.buoobuoo.mesuite.meentities.interf.tags.Invisible;
import com.buoobuoo.mesuite.meentities.interf.tags.Invulnerable;
import com.buoobuoo.mesuite.meentities.pathfinding.MoveToLocationGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.AABB;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

//Used to mirror actions onto npc
public class NpcMirrorEntity extends Zombie implements CustomEntity, Invulnerable, Invisible, HideNameTag, HideHealthTag {
    public NpcMirrorEntity(Location loc) {
        super(EntityType.ZOMBIE, ((CraftWorld) loc.getWorld()).getHandle());

        this.setAggressive(false);
        this.setInvulnerable(true);
        this.setSilent(true);
        this.setCanBreakDoors(false);
        this.setBoundingBox(new AABB(0, 0, 0, 0, 0, 0));
        this.setPersistenceRequired(true);
    }

    @Override
    public void registerGoals(){
        this.goalSelector.addGoal(0, new MoveToLocationGoal(MEEntitiesPlugin.getInstance().getEntityManager(), this, 1.5));
    }

    @Override
    public String entityID() {
        return "NPC_MIRROR";
    }

    @Override
    public String entityName() {
        return "NPC_MIRROR";
    }

}
