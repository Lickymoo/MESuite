package com.buoobuoo.mesuite.mechat;

import com.buoobuoo.mesuite.mechat.command.impl.DialogueCommand;
import com.buoobuoo.mesuite.mechat.command.impl.MessageCommand;
import com.buoobuoo.mesuite.mechat.gamehandler.listener.PlayerChatPacketListener;
import com.buoobuoo.mesuite.mechat.gamehandler.listener.PlayerMessagePacketListener;
import com.buoobuoo.mesuite.mechat.gamehandler.listener.StaffMessagePacketListener;
import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.melinker.redis.spigot.SpigotMELinker;
import com.buoobuoo.mesuite.mepermissions.MEPermissionsPlugin;
import com.buoobuoo.mesuite.mepermissions.PermissionManager;
import com.buoobuoo.mesuite.mepermissions.data.PlayerPermissionDataManager;
import com.buoobuoo.mesuite.meplayerdata.MEPlayerDataPlugin;
import com.buoobuoo.mesuite.meplayerdata.PlayerDataManager;
import com.buoobuoo.mesuite.mesocial.MESocialPlugin;
import com.buoobuoo.mesuite.mesocial.party.PartyManager;
import com.buoobuoo.mesuite.meutils.command.CommandManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;

@Getter
public class MEChatPlugin extends MEPlugin {

    //dependencies
    private MECorePlugin meCorePlugin;
    private CommandManager commandManager;
    private SpigotMELinker meLinker;

    private MESocialPlugin meSocialPlugin;
    private PartyManager partyManager;

    private MEPlayerDataPlugin mePlayerDataPlugin;
    private PlayerDataManager playerDataManager;

    private MEPermissionsPlugin mePermissionsPlugin;
    private PlayerPermissionDataManager playerPermissionDataManager;
    private PermissionManager permissionManager;

    //local managers
    private ChatManager chatManager;
    private ProtocolManager protocolManager;

    @Override
    public void initDependencies() {
        PluginManager pluginManager = getServer().getPluginManager();
        this.meCorePlugin = (MECorePlugin) pluginManager.getPlugin("MECore");
        this.commandManager = meCorePlugin.getCommandManager();
        this.meLinker = meCorePlugin.getMeLinker();

        this.meSocialPlugin = (MESocialPlugin) pluginManager.getPlugin("MESocial");
        this.partyManager = meSocialPlugin.getPartyManager();

        this.mePlayerDataPlugin = (MEPlayerDataPlugin) pluginManager.getPlugin("MEPlayerData");
        this.playerDataManager = mePlayerDataPlugin.getPlayerDataManager();

        this.mePermissionsPlugin = (MEPermissionsPlugin) pluginManager.getPlugin("MEPermissions");
        this.playerPermissionDataManager = mePermissionsPlugin.getPlayerPermissionDataManager();
        this.permissionManager = mePermissionsPlugin.getPermissionManager();
    }

    @Override
    public void initManagers() {
        this.chatManager = new ChatManager(this);
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void initListeners() {
        registerEvents(
                chatManager,

                new PlayerMessagePacketListener(this),
                new StaffMessagePacketListener(this)
        );

        new PlayerChatPacketListener(this);
    }

    @Override
    public void defineCommands(){
        commandManager.registerCommand(new MessageCommand(this));
        commandManager.registerCommand(new DialogueCommand(this));
    }

    @Override
    public void initTimers() {

    }
}
