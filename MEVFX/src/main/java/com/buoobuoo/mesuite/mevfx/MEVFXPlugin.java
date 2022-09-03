package com.buoobuoo.mesuite.mevfx;

import com.buoobuoo.mesuite.meentities.EntityManager;
import com.buoobuoo.mesuite.meentities.MEEntitiesPlugin;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import com.buoobuoo.mesuite.mevfx.cinematic.SpectatorManager;
import lombok.Getter;

@Getter
public class MEVFXPlugin extends MEPlugin {

    //dependencies
    private MEEntitiesPlugin meEntitiesPlugin;
    private EntityManager entityManager;

    //local managers
    private SpectatorManager spectatorManager;

    @Override
    public void initDependencies() {
        this.meEntitiesPlugin = getPlugin(MEEntitiesPlugin.class);
        this.entityManager = meEntitiesPlugin.getEntityManager();
    }

    @Override
    public void initManagers() {
        this.spectatorManager = new SpectatorManager(this);
    }

    @Override
    public void initListeners() {
        registerEvents(
            spectatorManager
        );
    }

    @Override
    public void initTimers() {

    }
}
