package com.buoobuoo.mesuite.meareas.impl.aramore;

import com.buoobuoo.mesuite.meareas.Area;
import com.buoobuoo.mesuite.mecore.MECorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class AramoreAbandonedShrineArea extends Area {
    public AramoreAbandonedShrineArea() {
        super("Abandoned Shrine",
                BoundingBox.of(
                        new Location(Bukkit.getWorld(MECorePlugin.MAIN_WORLD_NAME), -33, 90, -81),
                        new Location(Bukkit.getWorld(MECorePlugin.MAIN_WORLD_NAME), -86, 131, -133)
                ));
    }
}
