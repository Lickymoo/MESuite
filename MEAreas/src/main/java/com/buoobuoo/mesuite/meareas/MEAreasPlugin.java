package com.buoobuoo.mesuite.meareas;

import com.buoobuoo.mesuite.meareas.data.ProfileAreaDataManager;
import com.buoobuoo.mesuite.meareas.gamehandler.listener.PlayerDeathEventListener;
import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.meentities.EntityManager;
import com.buoobuoo.mesuite.meentities.MEEntitiesPlugin;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import lombok.Getter;

@Getter
public class MEAreasPlugin extends MEPlugin {

    //dependencies
    private MECorePlugin meCorePlugin;
    private MongoHook mongoHook;

    private MEPlayerDataPlugin mePlayerDataPlugin;
    private PlayerDataManager playerDataManager;

    private MEEntitiesPlugin meEntitiesPlugin;
    private EntityManager entityManager;

    //local managers
    private ProfileAreaDataManager areaDataManager;
    private AreaManager areaManager;

    @Override
    public void initDependencies() {
        this.meCorePlugin = getPlugin(MECorePlugin.class);
        this.mongoHook = meCorePlugin.getMongoHook();

        this.mePlayerDataPlugin = getPlugin(MEPlayerDataPlugin.class);
        this.playerDataManager = mePlayerDataPlugin.getPlayerDataManager();

        this.meEntitiesPlugin = getPlugin(MEEntitiesPlugin.class);
        this.entityManager = meEntitiesPlugin.getEntityManager();

    }

    @Override
    public void initManagers() {
        this.areaDataManager = new ProfileAreaDataManager(this);
        this.areaManager = new AreaManager(this);

    }

    @Override
    public void initListeners() {
        registerEvents(
                areaManager,
                areaDataManager,

                new PlayerDeathEventListener(this)
        );
    }

    @Override
    public void initTimers() {

    }
}
