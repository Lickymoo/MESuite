package com.buoobuoo.mesuite.meareas.impl.aramore;

import com.buoobuoo.mesuite.meareas.TownArea;
import com.buoobuoo.mesuite.mecore.MECorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class AramoreArea extends TownArea {

    public AramoreArea() {
        super("Aramore",
                new Location(MECorePlugin.getMainWorld(), 167, 66, 63),
                BoundingBox.of(
                        new Location(Bukkit.getWorld(MECorePlugin.MAIN_WORLD_NAME), 207, 30, 115),
                        new Location(Bukkit.getWorld(MECorePlugin.MAIN_WORLD_NAME), 113, 300, 10)
                        )
        );
    }
}
