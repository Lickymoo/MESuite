package com.buoobuoo.mesuite.meplayerdata;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.meinventories.CustomInventoryManager;
import com.buoobuoo.mesuite.meinventories.MEInventoriesPlugin;
import com.buoobuoo.mesuite.melinker.redis.spigot.SpigotMELinker;
import com.buoobuoo.mesuite.meplayerdata.command.impl.ProfileCommand;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.listener.ProfileInventoryEnforceListener;
import com.buoobuoo.mesuite.meplayerdata.packet.ResumePlayerDataPacket;
import com.buoobuoo.mesuite.meplayerdata.packet.SuspendPlayerDataPacket;
import com.buoobuoo.mesuite.meutils.command.CommandManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import lombok.Getter;

@Getter
public class MEPlayerDataPlugin extends MEPlugin {
    //dependencies
    private MECorePlugin meCorePlugin;
    private MEInventoriesPlugin meInventoriesPlugin;
    private MongoHook mongoHook;
    private CustomInventoryManager inventoryManager;
    private CommandManager commandManager;

    //local managers
    private PlayerDataManager playerDataManager;
    private SpigotMELinker meLinker;

    @Override
    public void initDependencies(){
        this.meCorePlugin = getPlugin(MECorePlugin.class);
        this.meInventoriesPlugin = getPlugin(MEInventoriesPlugin.class);
        this.mongoHook = meCorePlugin.getMongoHook();
        this.inventoryManager = meInventoriesPlugin.getInventoryManager();

        this.meLinker = meCorePlugin.getMeLinker();
        this.meLinker.getPacketManager().registerPacket("RESUME_DATA", ResumePlayerDataPacket.class);
        this.meLinker.getPacketManager().registerPacket("SUSPEND_DATA", SuspendPlayerDataPacket.class);

        this.commandManager = meCorePlugin.getCommandManager();
    }

    @Override
    public void initManagers(){
        this.playerDataManager = new PlayerDataManager(this);
    }

    @Override
    public void initListeners(){
        registerEvents(
                playerDataManager,
                inventoryManager,

                new ProfileInventoryEnforceListener(this)
                );
    }

    @Override
    public void defineCommands(){
        commandManager.registerCommand(new ProfileCommand(this));
    }

    @Override
    public void initTimers() {

    }
}
