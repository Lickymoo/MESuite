package com.buoobuoo.mesuite.mesocial.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.buoobuoo.mesuite.melinker.redis.AbsPacketManager;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.PartyDataByPlayerRequest;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.melinker.util.PartyData;
import com.buoobuoo.mesuite.mesocial.MESocialPlugin;
import com.buoobuoo.mesuite.mesocial.inventory.EmptyPartyInventory;
import com.buoobuoo.mesuite.mesocial.inventory.PartyInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("party|parties|p")
public class PartyCommand extends BaseCommand {
    private final MESocialPlugin plugin;

    public PartyCommand(MESocialPlugin plugin){
        this.plugin = plugin;
    }

    @Default
    public void def(Player player){
        async(() -> {
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
            PartyData partyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3).getPartyData();

            sync(() -> {
                if (partyData == null) {
                    new EmptyPartyInventory(plugin, player).openInventory();
                } else {
                    new PartyInventory(plugin, player, partyData).openInventory();
                }
            });
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

    public void async(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public void sync(Runnable runnable){
        Bukkit.getScheduler().runTask(plugin, runnable);
    }
}
