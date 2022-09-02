package com.buoobuoo.mesuite.meareas;

import com.buoobuoo.mesuite.meentities.EntityManager;
import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import com.buoobuoo.mesuite.meutils.MEUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MobSpawningArea extends Area{
    protected final List<Location> mobSpawnPoint = new ArrayList<>();
    protected final List<Class<? extends CustomEntity>> entityTypes = new ArrayList<>();

    protected final Map<Location, CustomEntity> attributedEntity = new HashMap<>();

    public MobSpawningArea(String name, BoundingBox... boundingBoxes){
        super(name, boundingBoxes);
    }

    public void addMobSpawnPoint(Location loc){
        mobSpawnPoint.add(loc);
    }

    public void addEntityType(Class<? extends CustomEntity> type){
        entityTypes.add(type);
    }

    public void updateSecond(MEAreasPlugin plugin){
        EntityManager entityManager = plugin.getEntityManager();

        for(Location loc : mobSpawnPoint){
            if(!MEUtils.isChunkLoaded(loc))
                continue;

            CustomEntity entity = attributedEntity.getOrDefault(loc, null);
            if(entity == null || entity.isDead()){
                CustomEntity newEntity = entityManager.spawnEntity(entityTypes.get(0), loc);
                newEntity.setOriginPoint(loc, 12);
                newEntity.setDestroyOnUnload(true);
                attributedEntity.put(loc, newEntity);
            }
        }
    }
}
