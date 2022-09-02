package com.buoobuoo.mesuite.meareas.gamehandler.listener;

import com.buoobuoo.mesuite.meareas.Area;
import com.buoobuoo.mesuite.meareas.AreaManager;
import com.buoobuoo.mesuite.meareas.MEAreasPlugin;
import com.buoobuoo.mesuite.meareas.TownArea;
import com.buoobuoo.mesuite.meareas.data.ProfileAreaData;
import com.buoobuoo.mesuite.mecore.MECorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathEventListener implements Listener {

    private final MEAreasPlugin plugin;

    public PlayerDeathEventListener(MEAreasPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(PlayerDeathEvent event){
        event.setDeathMessage(null);
    }

    @EventHandler
    public void respawnEvent(PlayerRespawnEvent event){
        Location defaultRespawn = new Location(Bukkit.getWorld(MECorePlugin.MAIN_WORLD_NAME), 195, 51, 282, -180, 0);

        Player player = event.getPlayer();
        ProfileAreaData areaData = plugin.getAreaDataManager().getAreaData(player);

        AreaManager areaManager = plugin.getAreaManager();

        String areaName = areaData.getLastAreaName();

        Area area = areaManager.getAreaByName(areaName);

        if(area instanceof TownArea townArea){
            event.setRespawnLocation(townArea.getRespawnPoint());
        }else{
            event.setRespawnLocation(defaultRespawn);
        }

    }
}
