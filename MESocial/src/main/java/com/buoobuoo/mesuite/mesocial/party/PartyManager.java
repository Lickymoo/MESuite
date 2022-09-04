package com.buoobuoo.mesuite.mesocial.party;

import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketEvent;
import com.buoobuoo.mesuite.melinker.redis.AbsPacketManager;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.PlayerMessagePacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.*;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.melinker.util.PartyData;
import com.buoobuoo.mesuite.mesocial.MESocialPlugin;
import com.buoobuoo.mesuite.mesocial.gamehandler.event.PartyInviteUpdateEvent;
import com.buoobuoo.mesuite.mesocial.gamehandler.event.PartyUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PartyManager implements Listener {
    private final MESocialPlugin plugin;

    /*
    use async to wait for redis response
     */
    public PartyManager(MESocialPlugin plugin){
        this.plugin = plugin;
    }

    public void createParty(Player player){
        async(() -> {
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
            PartyDataRequestResponsePacket getPartyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3);

            PartyData partyData = getPartyData.getPartyData();
            if(partyData != null){
                player.sendMessage("You are already in a party");
                return;
            }


            PartyDataRequestResponsePacket createParty = new PartyCreatePacket(player.getUniqueId(), null).await(packetManager, 3);
            PartyData newParty = createParty.getPartyData();
            player.sendMessage("Created new party " + newParty.getPartyID());
        });
    }

    public PartyData createPartySync(Player player){
        AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
        PartyDataRequestResponsePacket getPartyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3);

        PartyData partyData = getPartyData.getPartyData();
        if(partyData != null){
            player.sendMessage("You are already in a party");
            return null;
        }


        PartyDataRequestResponsePacket createParty = new PartyCreatePacket(player.getUniqueId(), null).await(packetManager, 3);
        PartyData newParty = createParty.getPartyData();
        player.sendMessage("Created new party " + newParty.getPartyID());
        return newParty;
    }

    public void disband(Player player){
        async(() -> {
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
            PartyDataRequestResponsePacket getPartyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3);

            PartyData partyData = getPartyData.getPartyData();
            if(partyData == null){
                player.sendMessage("You are not in a party");
                return;
            }
            if(!partyData.isLeader(player)){
                player.sendMessage("You are not the leader of this party");
                return;
            }

            partyData.messageParty(packetManager, "The party has been disbanded");

            PartyDisbandPacket disbandPacket = new PartyDisbandPacket(partyData.getPartyID());
            packetManager.sendPacket(disbandPacket);

        });
    }

    public void leave(Player player){
        async(() -> {
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
            PartyDataRequestResponsePacket getPartyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3);

            PartyData partyData = getPartyData.getPartyData();
            if(partyData == null){
                player.sendMessage("You are not in a party");
                return;
            }
            if(partyData.isLeader(player)){
                player.sendMessage("You cannot leave as the leader. Use /party disband");
                return;
            }

            partyData.messageParty(packetManager, player.getName() + " has left the party");

            PartyPlayerLeavePacket partyPlayerLeavePacket = new PartyPlayerLeavePacket(partyData.getPartyID(), player.getUniqueId());
            packetManager.sendPacket(partyPlayerLeavePacket);
        });
    }

    public void join(Player player, NetworkedPlayer target){
        async(() -> {
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
            PartyDataRequestResponsePacket getPartyData = new PartyDataByPlayerRequest(target.getUuid(), null).await(packetManager, 3);

            PartyData partyData = getPartyData.getPartyData();
            if(partyData == null){
                player.sendMessage("Player is not in a party");
                return;
            }
            if(player.getUniqueId().equals(target.getUuid())){
                player.sendMessage("You cannot join your own party");
                return;
            }

            if(!partyData.isInvited(player.getUniqueId())){
                player.sendMessage("You are not invited to this party");
                return;
            }


            PartyDataRequestResponsePacket getPlayerPartyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3);
            PartyData playerPartyData = getPlayerPartyData.getPartyData();

            if(playerPartyData != null){
                player.sendMessage("You are already in a party");
                return;
            }

            PartyPlayerJoinPacket joinPacket = new PartyPlayerJoinPacket(partyData.getPartyID(), player.getUniqueId());
            packetManager.sendPacket(joinPacket);
            partyData.messageParty(packetManager, player.getName() + " has joined the party");
        });
    }

    public void invite(Player player, NetworkedPlayer target){
        async(() -> {
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
            PartyDataRequestResponsePacket getPartyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3);

            PartyData partyData = getPartyData.getPartyData();
            if(partyData == null){
                player.sendMessage("You are not in a party");
                return;
            }
            if(player.getUniqueId().equals(target.getUuid())){
                player.sendMessage("You cannot invite yourself");
                return;
            }
            if(!partyData.isLeader(player)){
                player.sendMessage("You are not the leader of this party");
                return;
            }
            if(partyData.isInvited(target.getUuid())){
                player.sendMessage(target.getName() + " Is already invited to the party");
                return;
            }


            PartyDataRequestResponsePacket getTargetParty = new PartyDataByPlayerRequest(target.getUuid(), null).await(packetManager, 3);
            PartyData targetParty = getTargetParty.getPartyData();
            if(targetParty != null){
                player.sendMessage("Player is already in a party");
                return;
            }

            UUID targetID = target.getUuid();
            PartyPlayerInvitePacket invitePacket = new PartyPlayerInvitePacket(partyData.getPartyID(), targetID);
            PlayerMessagePacket messagePacket = new PlayerMessagePacket(targetID, player.getName() + " has invited you to their party");
            partyData.messageParty(packetManager, target.getName() + " has been invited to the party");
            packetManager.sendPacket(invitePacket);
            packetManager.sendPacket(messagePacket);
        });
    }

    public void kick(Player player, NetworkedPlayer target){
        async(() -> {
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
            PartyDataRequestResponsePacket getPartyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3);

            PartyData partyData = getPartyData.getPartyData();
            if(partyData == null){
                player.sendMessage("You are not in a party");
                return;
            }
            if(player.getUniqueId().equals(target.getUuid())){
                player.sendMessage("You cannot kick yourself");
                return;
            }
            if(!partyData.isLeader(player)){
                player.sendMessage("You are not the leader of this party");
                return;
            }
            if(!partyData.isInParty(target.getUuid())){
                player.sendMessage("Player is not in party");
                return;
            }

            UUID targetID = target.getUuid();
            PartyPlayerKickPacket kickPacket = new PartyPlayerKickPacket(partyData.getPartyID(), targetID);
            packetManager.sendPacket(kickPacket);

            PlayerMessagePacket messagePacket = new PlayerMessagePacket(targetID, "You have been kicked from the party");
            partyData.messageParty(packetManager, target.getName() + " has been kicked from the party");
            packetManager.sendPacket(messagePacket);
        });
    }

    public void promote(Player player, NetworkedPlayer target){
        async(() -> {
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
            PartyDataRequestResponsePacket getPartyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3);

            PartyData partyData = getPartyData.getPartyData();
            if(partyData == null){
                player.sendMessage("You are not in a party");
                return;
            }
            if(player.getUniqueId().equals(target.getUuid())){
                player.sendMessage("You cannot promote yourself");
                return;
            }
            if(!partyData.isLeader(player)){
                player.sendMessage("You are not the leader of this party");
                return;
            }
            if(!partyData.isInParty(target.getUuid())){
                player.sendMessage("Player is not in party");
                return;
            }

            UUID targetID = target.getUuid();
            PartyPlayerPromotePacket promotePacket = new PartyPlayerPromotePacket(partyData.getPartyID(), targetID);
            packetManager.sendPacket(promotePacket);

            partyData.messageParty(packetManager, target.getName() + " has been promoted");
        });
    }

    public void teleport(Player player){
        async(() -> {
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
            PartyDataRequestResponsePacket getPartyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3);

            PartyData partyData = getPartyData.getPartyData();
            if(partyData == null){
                player.sendMessage("You are not in a party");
                return;
            }
            if(!partyData.isLeader(player)){
                player.sendMessage("You are not the leader of this party");
                return;
            }

            PartyMessagePacket partyMessagePacket = new PartyMessagePacket(partyData.getPartyID(), "Teleporting you to leader's server");
            packetManager.sendPacket(partyMessagePacket);

            PartyTeleportPacket partyTeleportPacket = new PartyTeleportPacket(partyData.getPartyID());
            packetManager.sendPacket(partyTeleportPacket);
        });
    }

    public void async(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        async(() -> {
            Player player = event.getPlayer();
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
            PartyDataRequestResponsePacket getPartyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3);

            PartyData partyData = getPartyData.getPartyData();
            if(partyData == null){
                return;
            }

            if(partyData.isLeader(player)){
                return;
            }

            PartyTeleportSingularPacket partyTeleportSingularPacket = new PartyTeleportSingularPacket(partyData.getPartyID(), player.getUniqueId());
            packetManager.sendPacket(partyTeleportSingularPacket);
        });
    }

    @EventHandler
    public void onPartyUpdate(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof PartyUpdatePacket updatePacket){
            PartyUpdateEvent updateEvent = new PartyUpdateEvent(updatePacket.getPartyID());
            Bukkit.getPluginManager().callEvent(updateEvent);
        }
    }

    @EventHandler
    public void onInviteUpdate(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof PartyPlayerInvitePacket invitePacket){
            PartyInviteUpdateEvent inviteUpdateEvent = new PartyInviteUpdateEvent(invitePacket.getMemberID());
            Bukkit.getPluginManager().callEvent(inviteUpdateEvent);
        }
    }
}
