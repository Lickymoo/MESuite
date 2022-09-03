package com.buoobuoo.mesuite.meitems;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.meitems.additional.attributes.ItemAttributeManager;
import com.buoobuoo.mesuite.meitems.command.impl.ItemCommand;
import com.buoobuoo.mesuite.meitems.gamehandler.listener.ItemRequirementEventListener;
import com.buoobuoo.mesuite.meitems.gamehandler.listener.PlayerCreativeInteractEventListener;
import com.buoobuoo.mesuite.meitems.gamehandler.listener.QueryItemModifiersEventListener;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.meutils.command.CommandManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import lombok.Getter;

@Getter
public class MEItemsPlugin extends MEPlugin {

    //dependencies
    private MECorePlugin meCorePlugin;
    private CommandManager commandManager;

    private MEPlayerDataPlugin mePlayerDataPlugin;
    private PlayerDataManager playerDataManager;

    //local managers
    private CustomItemManager customItemManager;
    private ItemAttributeManager itemAttributeManager;

    @Override
    public void initDependencies() {
        this.meCorePlugin = getPlugin(MECorePlugin.class);
        this.commandManager = meCorePlugin.getCommandManager();

        this.mePlayerDataPlugin = getPlugin(MEPlayerDataPlugin.class);
        this.playerDataManager = mePlayerDataPlugin.getPlayerDataManager();
    }

    @Override
    public void initManagers() {
        this.customItemManager = new CustomItemManager(this);
        this.itemAttributeManager = new ItemAttributeManager(this);
    }

    @Override
    public void initListeners() {
        registerEvents(
                new QueryItemModifiersEventListener(this),

                new ItemRequirementEventListener(this),
                new PlayerCreativeInteractEventListener(this)
        );

    }

    @Override
    public void defineCommands(){
        this.commandManager.getCommandCompletions().registerAsyncCompletion("custom-items", c -> CustomItems.getNames());
        this.commandManager.registerCommand(new ItemCommand(this));
    }

    @Override
    public void initTimers() {

    }
}
