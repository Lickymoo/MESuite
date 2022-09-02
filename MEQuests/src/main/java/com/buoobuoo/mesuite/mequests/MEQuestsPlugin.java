package com.buoobuoo.mesuite.mequests;

import com.buoobuoo.mesuite.mechat.ChatManager;
import com.buoobuoo.mesuite.mechat.MEChatPlugin;
import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.meentities.EntityManager;
import com.buoobuoo.mesuite.meentities.MEEntitiesPlugin;
import com.buoobuoo.mesuite.meinventories.CustomInventoryManager;
import com.buoobuoo.mesuite.meinventories.MEInventoriesPlugin;
import com.buoobuoo.mesuite.meitems.CustomItemManager;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.mequests.command.QuestCommand;
import com.buoobuoo.mesuite.mequests.data.ProfileQuestDataManager;
import com.buoobuoo.mesuite.mequests.gamehandler.listener.ProfileDataLoadEventListener;
import com.buoobuoo.mesuite.mequests.gamehandler.listener.ProfileDataRemoveEventListener;
import com.buoobuoo.mesuite.meutils.command.CommandManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import com.buoobuoo.mesuite.mevfx.MEVFXPlugin;
import com.buoobuoo.mesuite.mevfx.cinematic.SpectatorManager;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;

@Getter
public class MEQuestsPlugin extends MEPlugin {
    //dependencies
    private MECorePlugin meCorePlugin;
    private MongoHook mongoHook;
    private CommandManager commandManager;

    private MEPlayerDataPlugin mePlayerDataPlugin;
    private PlayerDataManager playerDataManager;

    private MEEntitiesPlugin meEntitiesPlugin;
    private EntityManager entityManager;

    private MEChatPlugin meChatPlugin;
    private ChatManager chatManager;

    private MEItemsPlugin meItemsPlugin;
    private CustomItemManager customItemManager;

    private MEVFXPlugin mevfxPlugin;
    private SpectatorManager spectatorManager;

    private MEInventoriesPlugin meInventoriesPlugin;
    private CustomInventoryManager inventoryManager;

    //local managers
    private ProfileQuestDataManager questDataManager;
    private QuestManager questManager;

    @Override
    public void initDependencies() {
        PluginManager pluginManager = getServer().getPluginManager();
        this.meCorePlugin = (MECorePlugin) pluginManager.getPlugin("MECore");
        this.mongoHook = meCorePlugin.getMongoHook();
        this.commandManager = meCorePlugin.getCommandManager();

        this.mePlayerDataPlugin = (MEPlayerDataPlugin) pluginManager.getPlugin("MEPlayerData");
        this.playerDataManager = mePlayerDataPlugin.getPlayerDataManager();

        this.meEntitiesPlugin = (MEEntitiesPlugin) pluginManager.getPlugin("MEEntities");
        this.entityManager = meEntitiesPlugin.getEntityManager();

        this.meChatPlugin = (MEChatPlugin) pluginManager.getPlugin("MEChat");
        this.chatManager = meChatPlugin.getChatManager();

        this.meItemsPlugin = (MEItemsPlugin) pluginManager.getPlugin("MEItems");
        this.customItemManager = meItemsPlugin.getCustomItemManager();

        this.mevfxPlugin = (MEVFXPlugin) pluginManager.getPlugin("MEVFX");
        this.spectatorManager = mevfxPlugin.getSpectatorManager();

        this.meInventoriesPlugin = (MEInventoriesPlugin) pluginManager.getPlugin("MEInventories");
        this.inventoryManager = meInventoriesPlugin.getInventoryManager();
    }

    @Override
    public void initManagers() {
        this.questDataManager = new ProfileQuestDataManager(this);
        this.questManager = new QuestManager(this);

    }

    @Override
    public void initListeners() {
        registerEvents(
                questDataManager,

                new ProfileDataRemoveEventListener(this),
                new ProfileDataLoadEventListener(this)
        );
    }

    @Override
    public void initTimers() {

    }

    @Override
    public void defineCommands(){
        commandManager.registerCommand(new QuestCommand(this));
        commandManager.getCommandCompletions().registerAsyncCompletion("quests", c -> getQuestManager().allQuestID());

    }
}
