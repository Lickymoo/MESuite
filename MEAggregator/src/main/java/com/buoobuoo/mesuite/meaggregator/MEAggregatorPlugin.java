package com.buoobuoo.mesuite.meaggregator;

import com.buoobuoo.mesuite.meabilities.AbilityManager;
import com.buoobuoo.mesuite.meabilities.MEAbilitiesPlugin;
import com.buoobuoo.mesuite.meabilities.data.ProfileAbilityDataManager;
import com.buoobuoo.mesuite.meaggregator.gamehandler.listener.PlayerManagerItemListener;
import com.buoobuoo.mesuite.meinventories.CustomInventoryManager;
import com.buoobuoo.mesuite.meinventories.MEInventoriesPlugin;
import com.buoobuoo.mesuite.meitems.CustomItemManager;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.mequests.MEQuestsPlugin;
import com.buoobuoo.mesuite.mequests.QuestManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import lombok.Getter;

@Getter
public class MEAggregatorPlugin extends MEPlugin {

    //depdencies
    private MEInventoriesPlugin meInventoriesPlugin;
    private CustomInventoryManager inventoryManager;

    private MEPlayerDataPlugin mePlayerDataPlugin;
    private PlayerDataManager playerDataManager;

    private MEAbilitiesPlugin meAbilitiesPlugin;
    private ProfileAbilityDataManager profileAbilityDataManager;
    private AbilityManager abilityManager;

    private MEItemsPlugin meItemsPlugin;
    private CustomItemManager customItemManager;

    private MEQuestsPlugin meQuestsPlugin;
    private QuestManager questManager;

    @Override
    public void initDependencies() {
        this.meInventoriesPlugin = getPlugin(MEInventoriesPlugin.class);
        this.inventoryManager = meInventoriesPlugin.getInventoryManager();

        this.mePlayerDataPlugin = getPlugin(MEPlayerDataPlugin.class);
        this.playerDataManager = mePlayerDataPlugin.getPlayerDataManager();

        this.meAbilitiesPlugin = getPlugin(MEAbilitiesPlugin.class);
        this.abilityManager = meAbilitiesPlugin.getAbilityManager();
        this.profileAbilityDataManager = meAbilitiesPlugin.getAbilityDataManager();

        this.meItemsPlugin = getPlugin(MEItemsPlugin.class);
        this.customItemManager = meAbilitiesPlugin.getCustomItemManager();

        this.meQuestsPlugin = getPlugin(MEQuestsPlugin.class);
        this.questManager = meQuestsPlugin.getQuestManager();
    }

    @Override
    public void initManagers() {

    }

    @Override
    public void initListeners() {
        registerEvents(
                new PlayerManagerItemListener(this)
        );
    }

    @Override
    public void initTimers() {

    }
}
