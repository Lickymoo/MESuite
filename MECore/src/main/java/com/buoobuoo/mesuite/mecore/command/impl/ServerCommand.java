package com.buoobuoo.mesuite.mecore.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.buoobuoo.mesuite.mecore.MECorePlugin;
import org.bukkit.entity.Player;

@CommandAlias("whereami")
public class ServerCommand extends BaseCommand {

    private final MECorePlugin plugin;

    public ServerCommand(MECorePlugin plugin){
        this.plugin = plugin;
    }

    @Default
    public void def(Player player){
        String serverName = plugin.getNetworkedPlayer(player.getUniqueId()).getConnectedServer();
        player.sendMessage("You are currently connected to " + serverName);
    }
}



