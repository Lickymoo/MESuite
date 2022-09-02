package com.buoobuoo.mesuite.meareas.impl.aramore;

import com.buoobuoo.mesuite.meareas.MobSpawningArea;
import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.meentities.impl.ViciousWolfEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class AramoreWolfArea extends MobSpawningArea {

    public AramoreWolfArea() {
        super("Aramore",
                BoundingBox.of(
                        new Location(Bukkit.getWorld(MECorePlugin.MAIN_WORLD_NAME), 107, 81, -79),
                        new Location(Bukkit.getWorld(MECorePlugin.MAIN_WORLD_NAME), 149, 67, -52)
                        )
        );

        addMobSpawnPoint(new Location(Bukkit.getWorld(MECorePlugin.MAIN_WORLD_NAME), 140, 67, -65));
        //addMobSpawnPoint(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 121, 67, -61));
        //addMobSpawnPoint(new Location(Bukkit.getWorld(MinecraftEnhanced.MAIN_WORLD_NAME), 135, 68, -80));

        addEntityType(ViciousWolfEntity.class);
    }
}
