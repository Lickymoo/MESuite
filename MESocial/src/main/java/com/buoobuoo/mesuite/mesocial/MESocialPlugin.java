package com.buoobuoo.mesuite.mesocial;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.melinker.redis.spigot.SpigotMELinker;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.mesocial.command.impl.FriendCommand;
import com.buoobuoo.mesuite.mesocial.command.impl.PartyCommand;
import com.buoobuoo.mesuite.mesocial.friend.FriendManager;
import com.buoobuoo.mesuite.mesocial.party.PartyManager;
import com.buoobuoo.mesuite.meutils.command.CommandManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import lombok.Getter;

import java.util.UUID;

@Getter
public class MESocialPlugin extends MEPlugin {
    //dependencies
    private MECorePlugin meCorePlugin;
    private MongoHook mongoHook;
    private CommandManager commandManager;
    private SpigotMELinker meLinker;

    //local managers
    private PartyManager partyManager;
    private FriendManager friendManager;

    @Override
    public void initManagers(){
        this.partyManager = new PartyManager(this);
        this.friendManager = new FriendManager(this);

        //register packets
    }

    @Override
    public void initDependencies(){
        this.meCorePlugin = getPlugin(MECorePlugin.class);
        this.mongoHook = meCorePlugin.getMongoHook();
        this.meLinker = meCorePlugin.getMeLinker();

        this.commandManager = meCorePlugin.getCommandManager();
        defineCommands();
    }

    @Override
    public void defineCommands(){
        commandManager.registerCommand(new FriendCommand(this));
        commandManager.registerCommand(new PartyCommand(this));
    }

    @Override
    public void initListeners(){
        registerEvents(
                this,
                friendManager,
                partyManager
        );
    }

    @Override
    public void initTimers() {

    }

    public NetworkedPlayer getNetworkedPlayer(UUID uuid){
        for(NetworkedPlayer networkedPlayer : meCorePlugin.getNetworkPlayerData()){
            if(networkedPlayer.getUuid().equals(uuid))
                return networkedPlayer;
        }
        return null;
    }
}
