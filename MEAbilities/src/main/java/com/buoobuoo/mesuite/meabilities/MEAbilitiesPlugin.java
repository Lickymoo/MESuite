package com.buoobuoo.mesuite.meabilities;

import com.buoobuoo.mesuite.meabilities.command.impl.AbilityCommand;
import com.buoobuoo.mesuite.meabilities.data.ProfileAbilityDataManager;
import com.buoobuoo.mesuite.meabilities.item.AbilityGemItem;
import com.buoobuoo.mesuite.meabilities.serializer.AbilityCastTypeArraySerializer;
import com.buoobuoo.mesuite.meabilities.serializer.AbilityCastTypeSerializer;
import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.meitems.CustomItemManager;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.meutils.command.CommandManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import lombok.Getter;

@Getter
public class MEAbilitiesPlugin extends MEPlugin {

    //dependencies
    private MECorePlugin meCorePlugin;
    private CommandManager commandManager;
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
        this.meCorePlugin = getPlugin(MECorePlugin.class);
        this.commandManager = meCorePlugin.getCommandManager();
        this.mongoHook = meCorePlugin.getMongoHook();
        this.mongoHook.registerSerializer(AbilityCastType.class, new AbilityCastTypeSerializer());
        this.mongoHook.registerSerializer(AbilityCastType[].class, new AbilityCastTypeArraySerializer());

        this.mePlayerDataPlugin = getPlugin(MEPlayerDataPlugin.class);
        this.playerDataManager = mePlayerDataPlugin.getPlayerDataManager();

        this.meItemsPlugin = getPlugin(MEItemsPlugin.class);
        this.customItemManager = meItemsPlugin.getCustomItemManager();
        this.customItemManager.getRegistry().registerItem(new AbilityGemItem(null));
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

    @Override
    public void defineCommands(){
        commandManager.registerCommand(new AbilityCommand(this));
        commandManager.getCommandCompletions().registerAsyncCompletion("abilities", c -> getAbilityManager().allAbilityID());

    }
}
