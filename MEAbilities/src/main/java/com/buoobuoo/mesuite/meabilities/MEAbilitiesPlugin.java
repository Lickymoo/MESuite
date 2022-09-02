package com.buoobuoo.mesuite.meabilities;

import com.buoobuoo.mesuite.meabilities.data.ProfileAbilityDataManager;
import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.meitems.CustomItemManager;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;

@Getter
public class MEAbilitiesPlugin extends MEPlugin {

    //dependencies
    private MECorePlugin meCorePlugin;
    private MongoHook mongoHook;

    private MEPlayerDataPlugin mePlayerDataPlugin;
    private PlayerDataManager playerDataManager;

    private MEItemsPlugin meItemsPlugin;
    private CustomItemManager customItemManager;

    //local managers
    private ProfileAbilityDataManager abilityDataManager;
    private AbilityManager abilityManager;

    @Override
    public void initDependencies() {
        PluginManager pluginManager = getServer().getPluginManager();
        this.meCorePlugin = (MECorePlugin) pluginManager.getPlugin("MECore");
        this.mongoHook = meCorePlugin.getMongoHook();

        this.mePlayerDataPlugin = (MEPlayerDataPlugin) pluginManager.getPlugin("MEPlayerData");
        this.playerDataManager = mePlayerDataPlugin.getPlayerDataManager();

        this.meItemsPlugin = (MEItemsPlugin) pluginManager.getPlugin("MeItems");
        this.customItemManager = meItemsPlugin.getCustomItemManager();
    }

    @Override
    public void initManagers() {
        this.abilityDataManager = new ProfileAbilityDataManager(this);
        this.abilityManager = new AbilityManager(this);
    }

    @Override
    public void initListeners() {
        registerEvents(
                abilityDataManager,
                abilityManager
        );
    }

    @Override
    public void initTimers() {

    }
}
