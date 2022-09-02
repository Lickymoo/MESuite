package com.buoobuoo.mesuite.mesocial.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.PartyDataByPlayerRequest;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.mesocial.MESocialPlugin;
import org.bukkit.entity.Player;

@CommandAlias("party|parties|p")
public class PartyCommand extends BaseCommand {
    private final MESocialPlugin plugin;

    public PartyCommand(MESocialPlugin plugin){
        this.plugin = plugin;
    }

    @Default
    public void def(Player player){
        PartyDataByPlayerRequest packet = new PartyDataByPlayerRequest(player.getUniqueId(), p -> {

        });
        plugin.getMeLinker().getPacketManager().sendCallbackPacket(packet, 3, () -> {
            player.sendMessage("NNONONONON");
        });
    }

    @Subcommand("create")
    public void create(Player player){
        plugin.getPartyManager().createParty(player);
    }

    @Subcommand("disband")
    public void disband(Player player){
        plugin.getPartyManager().disband(player);
    }

    @Subcommand("leave")
    public void leave(Player player){
        plugin.getPartyManager().leave(player);
    }

    @Subcommand("kick")
    @CommandCompletion("@networked-players-name")
    public void kick(Player player, NetworkedPlayer target){
        if(target == null){
            player.sendMessage("Could not find player");
            return;
        }
        plugin.getPartyManager().kick(player, target);

    }

    @Subcommand("invite")
    @CommandCompletion("@networked-players-name")
    public void invite(Player player, NetworkedPlayer target){
        if(target == null){
            player.sendMessage("Could not find player");
            return;
        }
        plugin.getPartyManager().invite(player, target);

    }

    @Subcommand("join")
    @CommandCompletion("@networked-players-name")
    public void join(Player player, NetworkedPlayer target){
        if(target == null){
            player.sendMessage("Could not find player");
            return;
        }
        plugin.getPartyManager().join(player, target);
    }

    @Subcommand("promote")
    @CommandCompletion("@networked-players-name")
    public void promote(Player player, NetworkedPlayer target){
        if(target == null){
            player.sendMessage("Could not find player");
            return;
        }
        plugin.getPartyManager().promote(player, target);
    }

    @Subcommand("teleport")
    public void teleport(Player player){
        plugin.getPartyManager().teleport(player);
    }
}
