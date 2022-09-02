package com.buoobuoo.mesuite.meentities;

import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import com.buoobuoo.mesuite.meentities.perseistence.SuspendedInstance;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChunkWatcher implements Listener {

    /*
    When chunk is unloaded, suspend the entity and keep it's data

     */

    private final MEEntitiesPlugin plugin;
    private final EntityManager entityManager;

    @Getter
    private static final Map<String, Set<SuspendedInstance>> suspendedChunks = new HashMap<>();

    public ChunkWatcher(MEEntitiesPlugin plugin, EntityManager entityManager){
        this.plugin = plugin;
        this.entityManager = entityManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkUnload(ChunkUnloadEvent event){
        Chunk chunk = event.getChunk();
        String chunkID = chunk.getX() + ":" + chunk.getZ();

        Set<CustomEntity> superParents = new HashSet<>();
        Set<SuspendedInstance> suspendedInstances = new HashSet<>();

        for (Entity ent : event.getChunk().getEntities()) {
            CustomEntity entity = entityManager.getHandlerByEntity(ent);
            if (entity == null)
                continue;

            //will overlap if super parent is same
            superParents.add(entityManager.getSuperParent(entity));
        }

        for (CustomEntity superParent : superParents) {
            Entity ent = superParent.asEntity().getBukkitEntity();
            PersistentDataContainer pdc = ent.getPersistentDataContainer();
            Location loc = ent.getLocation();

            if (superParent.destroyOnUnload()) {
                entityManager.removeEntity(superParent);
            } else {
                net.minecraft.world.entity.Entity nbtEntity = superParent.asEntity();
                CompoundTag compound = new CompoundTag();
                nbtEntity.saveWithoutId(compound);

                SuspendedInstance suspendedInstance = new SuspendedInstance(superParent, compound, loc);

                entityManager.suspendEntity(superParent);
                suspendedInstances.add(suspendedInstance);
            }
        }
        if (suspendedInstances.size() > 0) {
            suspendedChunks.put(chunkID, suspendedInstances);
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){
        Chunk chunk = event.getChunk();
        String chunkID = chunk.getX() + ":" + chunk.getZ();

        if(!suspendedChunks.containsKey(chunkID))
            return;

        Set<SuspendedInstance> suspendedInstances = suspendedChunks.get(chunkID);
        for(SuspendedInstance instance : suspendedInstances){
            CustomEntity ent = instance.getEntity();
            Location loc = instance.getLocation();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                CustomEntity newEnt = ent.instantiateClone(plugin);
                newEnt.setDoNotDestroy(true);
                newEnt.setSuspended(true);
                ent.copyTo(newEnt);
                Entity bEntity = newEnt.asEntity().getBukkitEntity();
                net.minecraft.world.entity.Entity nbtEntity = newEnt.asEntity();

                try{
                    CompoundTag compoundTag = NbtUtils.snbtToStructure(instance.getSerializedNBTCompound());

                    compoundTag.putUUID("UUID", newEnt.asEntity().getUUID());
                    nbtEntity.load(compoundTag);
                    //update hide players and invert hide so it goes to children

                    entityManager.spawnInstance(newEnt, loc);

                    newEnt.setSuspended(false);
                    newEnt.setDoNotDestroy(false);

                    entityManager.removeEntity(ent);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }, 1);
        }
    }
}
