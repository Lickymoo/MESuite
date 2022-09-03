package com.buoobuoo.mesuite.mecombat;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.meentities.EntityManager;
import com.buoobuoo.mesuite.meentities.MEEntitiesPlugin;
import com.buoobuoo.mesuite.meitems.CustomItemManager;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import lombok.Getter;

@Getter
public class MECombatPlugin extends MEPlugin {

    //dependencies
    private MECorePlugin meCorePlugin;

    private MEPlayerDataPlugin mePlayerDataPlugin;
    private PlayerDataManager playerDataManager;

    private MEEntitiesPlugin meEntitiesPlugin;
    private EntityManager entityManager;

    private MEItemsPlugin meItemsPlugin;
    private CustomItemManager customItemManager;

    //local managers
    private DamageManager damageManager;

    @Override
    public void initDependencies() {
        this.meCorePlugin = getPlugin(MECorePlugin.class);

        this.mePlayerDataPlugin = getPlugin(MEPlayerDataPlugin.class);
        this.playerDataManager = mePlayerDataPlugin.getPlayerDataManager();

        this.meEntitiesPlugin = getPlugin(MEEntitiesPlugin.class);
        this.entityManager = meEntitiesPlugin.getEntityManager();

        this.meItemsPlugin = getPlugin(MEItemsPlugin.class);
        this.customItemManager = meItemsPlugin.getCustomItemManager();
    }

    @Override
    public void initManagers() {
        this.damageManager = new DamageManager(this);

    }

    @Override
    public void initListeners() {
        registerEvents(
                damageManager
        );
    }

    @Override
    public void initTimers() {

    }
}
