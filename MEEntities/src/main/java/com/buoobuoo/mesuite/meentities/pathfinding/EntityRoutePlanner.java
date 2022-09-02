package com.buoobuoo.mesuite.meentities.pathfinding;

import com.buoobuoo.mesuite.mecore.gamehandler.event.UpdateTickEvent;
import com.buoobuoo.mesuite.meentities.EntityManager;
import com.buoobuoo.mesuite.meentities.MEEntitiesPlugin;
import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import com.buoobuoo.mesuite.meutils.navigation.Route;
import com.buoobuoo.mesuite.meutils.navigation.RouteFinishEvent;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayDeque;
import java.util.HashMap;

public class EntityRoutePlanner implements Listener {
    private final MEEntitiesPlugin plugin;
    private final EntityManager entityManager;
    private final HashMap<PathfinderMob, ArrayDeque<Location>> routeMap = new HashMap<>();
    private final HashMap<PathfinderMob, Route> routeHandlerMap = new HashMap<>();

    public EntityRoutePlanner(MEEntitiesPlugin plugin, EntityManager entityManager){
        this.plugin = plugin;
        this.entityManager = entityManager;
    }

    public void setRoute(PathfinderMob mob, Route route){
        routeMap.put(mob, plugin.getRouteManager().getRoutePoints(route));
        routeHandlerMap.put(mob, route);
    }

    @EventHandler
    public void onTick(UpdateTickEvent event){
        for(CustomEntity ent : entityManager.getRegisteredEntities()){
            PathfinderMob mob = ent.getPathfinderMob();
            if(!routeMap.containsKey(mob)) continue;

            ArrayDeque<Location> deque = routeMap.get(mob);
            Location loc = entityManager.getNavigationMap().getOrDefault(mob, null);

            if(deque.isEmpty()){
                Route route = routeHandlerMap.get(mob);
                Runnable onComplete = route.getOnComplete();
                routeHandlerMap.remove(mob);
                if(onComplete != null)
                    onComplete.run();

                RouteFinishEvent ev = new RouteFinishEvent(route);
                Bukkit.getPluginManager().callEvent(ev);
                routeMap.remove(mob);
            }
            if(loc == null){
                entityManager.getNavigationMap().put(mob, deque.pop());
            }

        }
    }
}













































