package com.buoobuoo.mesuite.meareas;

import com.buoobuoo.mesuite.meareas.data.ProfileAreaData;
import com.buoobuoo.mesuite.meareas.gamehandler.event.AreaEnterEvent;
import com.buoobuoo.mesuite.meareas.gamehandler.event.AreaExitEvent;
import com.buoobuoo.mesuite.meareas.impl.aramore.AramoreAbandonedShrineArea;
import com.buoobuoo.mesuite.meareas.impl.aramore.AramoreArea;
import com.buoobuoo.mesuite.meareas.impl.aramore.AramoreWolfArea;
import com.buoobuoo.mesuite.mecore.gamehandler.event.UpdateSecondEvent;
import com.buoobuoo.mesuite.meutils.Colour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class AreaManager implements Listener {

    private final MEAreasPlugin plugin;
    private final List<Area> areaList = new ArrayList<>();

    public AreaManager(MEAreasPlugin plugin){
        this.plugin = plugin;

        registerAreas(
                //aramore area
                new AramoreArea(),
                new AramoreWolfArea(),
                new AramoreAbandonedShrineArea()

        );
    }

    public void registerAreas(Area... areas){
        areaList.addAll(List.of(areas));
    }

    public Area getArea(Location loc){
        //TODO
        Area selectedArea = null;
        for(Area area : areaList){
            if(area.contains(loc)){
                selectedArea = area;
            }
        }
        return selectedArea;
    }

    public Area getAreaByName(String name){
        for(Area area : areaList){
            if(area.getName().equalsIgnoreCase(name))
                return area;
        }
        return null;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(event.getFrom().distance(event.getTo()) == 0)
            return;

        Player player = event.getPlayer();

        if(!plugin.getPlayerDataManager().hasActive(player))
            return;

        ProfileAreaData profileAreaData = plugin.getAreaDataManager().getAreaData(player);

        Location loc = player.getLocation();
        Area area = getArea(loc);

        if(profileAreaData.getCurrentArea() == area)
            return;

        if(area == null)
            exitArea(profileAreaData, profileAreaData.getCurrentArea());
        else
            enterArea(profileAreaData, area);
    }

    @EventHandler
    public void updateSecond(UpdateSecondEvent event){
        for(Area area : areaList){
            if(!(area instanceof MobSpawningArea mobSpawningArea))
                continue;

            mobSpawningArea.updateSecond(plugin);
        }
    }

    public void enterArea(ProfileAreaData profileData, Area area){
        Player player = Bukkit.getPlayer(profileData.getOwnerID());

        profileData.setCurrentArea(area);
        if(area instanceof TownArea) {
            player.sendTitle("", Colour.format("&7&lEntering " + area.getName()));
            profileData.setLastAreaName(area.getName());
        }

        AreaEnterEvent event = new AreaEnterEvent(player, area);
        Bukkit.getPluginManager().callEvent(event);

    }

    public void exitArea(ProfileAreaData profileData, Area area){
        Player player = Bukkit.getPlayer(profileData.getOwnerID());

        profileData.setCurrentArea(null);
        if(area instanceof TownArea) {
            player.sendTitle("", Colour.format("&7&lExiting " + area.getName()));
        }

        AreaExitEvent event = new AreaExitEvent(player, area);
        Bukkit.getPluginManager().callEvent(event);
    }
}




















































