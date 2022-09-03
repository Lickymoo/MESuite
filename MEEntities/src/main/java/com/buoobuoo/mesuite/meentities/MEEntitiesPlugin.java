package com.buoobuoo.mesuite.meentities;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.meentities.command.impl.EntityCommand;
import com.buoobuoo.mesuite.meentities.gamehandler.listener.*;
import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import com.buoobuoo.mesuite.meutils.JavaUtils;
import com.buoobuoo.mesuite.meutils.command.CommandManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import com.buoobuoo.mesuite.meutils.navigation.RouteManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

@Getter
public class MEEntitiesPlugin extends MEPlugin {
    @Getter
    private static MEEntitiesPlugin instance;

    //dependencies
    private MECorePlugin meCorePlugin;
    private CommandManager commandManager;

    //local managers
    private EntityManager entityManager;
    private RouteManager routeManager;
    private ProtocolManager protocolManager;

    @Override
    public void initDependencies() {
        this.meCorePlugin = getPlugin(MECorePlugin.class);
        this.commandManager = meCorePlugin.getCommandManager();
    }

    @Override
    public void initManagers(){
        instance = this;
        this.routeManager = new RouteManager(this);
        this.entityManager = new EntityManager(this);
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void initListeners() {
        registerEvents(
                entityManager,

                new EntityCombustEventListener(this),
                new EntityDamageEventListener(this),
                new EntityRegainHealthEventListener(this),
                new EntitySpawnEventListener(this)
        );

        new PlayerInteractNpcPacketListener(this);
    }

    @Override
    public void defineCommands(){

        commandManager.getCommandCompletions().registerAsyncCompletion("custom-entities", c -> getEntityManager().getAllHandlerNames());
        commandManager.getCommandContexts().registerContext(CustomEntity.class, c -> getEntityManager().getHandlerByName(c.popFirstArg()));

        commandManager.getCommandCompletions().registerAsyncCompletion("vanilla-entity-type", c -> JavaUtils.getAllList(EntityType.class));
        commandManager.getCommandContexts().registerContext(EntityType.class, c -> EntityType.valueOf(c.popFirstArg().toUpperCase()));

        this.commandManager.registerCommand(new EntityCommand(this));
    }

    @Override
    public void initTimers() {
        Bukkit.getScheduler().runTaskLater(this, () -> entityManager.initFixtures(), 40);
    }
}
