package com.buoobuoo.mesuite.mesocial.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.mesocial.MESocialPlugin;
import org.bukkit.entity.Player;

@CommandAlias("friend|friends|friendship|friendships")
public class FriendCommand extends BaseCommand {
    private final MESocialPlugin plugin;

    public FriendCommand(MESocialPlugin plugin){
        this.plugin = plugin;
    }

    @Subcommand("add")
    @CommandCompletion("@networked-players-name")
    public void add(Player player, NetworkedPlayer target){
        if(target == null){
            player.sendMessage("Could not find player");
            return;
        }
        plugin.getFriendManager().sendFriendRequest(player.getUniqueId(), target.getUuid());
    }

    @Subcommand("remove")
    @CommandCompletion("@networked-players-name")
    public void remove(Player player, NetworkedPlayer target){
        if(target == null){
            player.sendMessage("Could not find player");
            return;
        }
        plugin.getFriendManager().removeFriends(player.getUniqueId(), target.getUuid());
    }
}
