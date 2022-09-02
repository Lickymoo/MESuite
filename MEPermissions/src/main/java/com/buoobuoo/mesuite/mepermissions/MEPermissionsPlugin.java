package com.buoobuoo.mesuite.mepermissions;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.mepermissions.command.impl.PermissionCommand;
import com.buoobuoo.mesuite.mepermissions.data.PlayerPermissionDataManager;
import com.buoobuoo.mesuite.mepermissions.gamehandler.listener.PrefixQueryEventListener;
import com.buoobuoo.mesuite.mepermissions.serializer.PermissionGroupSerializer;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.meutils.JavaUtils;
import com.buoobuoo.mesuite.meutils.command.CommandManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;

@Getter
public class MEPermissionsPlugin extends MEPlugin {

    //dependencies
    private MECorePlugin meCorePlugin;
    private MongoHook mongoHook;
    private CommandManager commandManager;

    private MEPlayerDataPlugin mePlayerDataPlugin;
    private PlayerDataManager playerDataManager;

    //local managers
    private PlayerPermissionDataManager playerPermissionDataManager;
    private PermissionManager permissionManager;

    @Override
    public void initDependencies() {
        PluginManager pluginManager = getServer().getPluginManager();
        this.meCorePlugin = (MECorePlugin) pluginManager.getPlugin("MECore");
        this.commandManager = meCorePlugin.getCommandManager();
        this.mongoHook = meCorePlugin.getMongoHook();
        this.mongoHook.registerSerializer(PermissionGroup.class, new PermissionGroupSerializer());

        this.mePlayerDataPlugin = (MEPlayerDataPlugin) pluginManager.getPlugin("MEPlayerData");
        this.playerDataManager = mePlayerDataPlugin.getPlayerDataManager();


    }

    @Override
    public void initManagers() {
        this.playerPermissionDataManager = new PlayerPermissionDataManager(this);
        this.permissionManager = new PermissionManager(this);

    }

    @Override
    public void initListeners() {
        registerEvents(
                playerPermissionDataManager,

                new PrefixQueryEventListener(this)
        );
    }

    @Override
    public void initTimers() {

    }

    @Override
    public void defineCommands(){
        commandManager.registerCommand(new PermissionCommand(this));
        commandManager.getCommandCompletions().registerAsyncCompletion("perm-groups",  c -> JavaUtils.getAllList(PermissionGroup.class));


    }
}
