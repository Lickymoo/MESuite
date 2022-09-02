package com.buoobuoo.mesuite.mebungee.party;

import com.buoobuoo.mesuite.mebungee.MEBungeePlugin;
import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketBungeeEvent;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.PlayerMessagePacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.ChangePlayerServerPacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.*;
import com.buoobuoo.mesuite.melinker.util.PartyData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PartyManager implements Listener {

    private final MEBungeePlugin plugin;
    private final Map<UUID, PartyData> partyDataMap = new HashMap<>();

    public PartyManager(MEBungeePlugin plugin){
        this.plugin = plugin;
    }

    public PartyData createParty(UUID ownerID){
        PartyData partyData = new PartyData();
        UUID partyID = UUID.randomUUID();
        partyData.setPartyLeader(ownerID);
        partyData.setPartyID(partyID);
        partyDataMap.put(partyID, partyData);
        return partyData;
    }

    public PartyData getPartyByID(UUID uuid){
        return partyDataMap.get(uuid);
    }

    public PartyData getPartyByPlayer(UUID playerID){
        for(PartyData partyData : partyDataMap.values()){
            if(partyData.getPartyLeader().equals(playerID))
                return partyData;

            if(partyData.getPartyMembers().contains(playerID))
                return partyData;
        }
        return null;
    }

    public PartyDataRequestResponsePacket getPartyAsPacket(PartyData data){
        PartyDataRequestResponsePacket packet = new PartyDataRequestResponsePacket();
        packet.setPartyData(data);
        return packet;
    }

    @EventHandler
    public void packetListener(MEPacketBungeeEvent event){
        MEPacket packet = event.getPacket();

        //data by id
        if(packet instanceof PartyDataRequestPacket partyDataRequestPacket){
            UUID partyID = partyDataRequestPacket.getPartyID();
            PartyData partyData = getPartyByID(partyID);

            PartyDataRequestResponsePacket responsePacket = getPartyAsPacket(partyData);
            partyDataRequestPacket.respond(plugin.getMeLinker().getPacketManager(), responsePacket);
            return;
        }

        //data by player
        if(packet instanceof PartyDataByPlayerRequest partyDataByPlayerRequest){
            UUID playerID = partyDataByPlayerRequest.getPlayerID();
            PartyData partyData = getPartyByPlayer(playerID);

            PartyDataRequestResponsePacket responsePacket = getPartyAsPacket(partyData);
            partyDataByPlayerRequest.respond(plugin.getMeLinker().getPacketManager(), responsePacket);
            return;
        }

        //create party
        if(packet instanceof PartyCreatePacket createPartyPacket){
            PartyData partyData = createParty(createPartyPacket.getOwnerID());

            PartyDataRequestResponsePacket responsePacket = getPartyAsPacket(partyData);
            createPartyPacket.respond(plugin.getMeLinker().getPacketManager(), responsePacket);
            return;
        }

        //disband
        if(packet instanceof PartyDisbandPacket partyDisbandPacket){
            partyDataMap.remove(partyDisbandPacket.getPartyID());
            return;
        }

        //leave
        if(packet instanceof PartyPlayerLeavePacket partyPlayerLeavePacket){
            PartyData partyData = getPartyByID(partyPlayerLeavePacket.getPartyID());
            partyData.getPartyMembers().remove(partyPlayerLeavePacket.getMemberID());
            return;
        }

        //invite
        if(packet instanceof PartyPlayerInvitePacket partyPlayerInvitePacket){
            PartyData partyData = getPartyByID(partyPlayerInvitePacket.getPartyID());
            partyData.getInvitedPlayers().add(partyPlayerInvitePacket.getMemberID());
            return;
        }

        //join
        if(packet instanceof PartyPlayerJoinPacket partyPlayerJoinEvent){
            UUID player = partyPlayerJoinEvent.getMemberID();
            PartyData partyData = getPartyByID(partyPlayerJoinEvent.getPartyID());
            partyData.getInvitedPlayers().remove(player);
            partyData.getPartyMembers().add(player);

            ProxiedPlayer owner = plugin.getProxy().getPlayer(partyData.getPartyLeader());

            //move player to owner server
            PlayerMessagePacket messagePacket = new PlayerMessagePacket(player, "Moving you to party leader's server");
            plugin.getMeLinker().getPacketManager().sendPacket(messagePacket);

            ChangePlayerServerPacket changePlayerServerPacket = new ChangePlayerServerPacket(player, owner.getServer().getInfo().getName());
            plugin.getMeLinker().getPacketManager().sendPacket(changePlayerServerPacket);

            return;
        }

        //kick
        if(packet instanceof PartyPlayerKickPacket partyPlayerKickPacket){
            UUID player = partyPlayerKickPacket.getMemberID();
            PartyData partyData = getPartyByID(partyPlayerKickPacket.getPartyID());
            partyData.getInvitedPlayers().remove(player);
            partyData.getPartyMembers().remove(player);
            return;
        }

        //promote
        if(packet instanceof PartyPlayerPromotePacket partyPlayerPromotePacket){
            UUID player = partyPlayerPromotePacket.getMemberID();
            PartyData partyData = getPartyByID(partyPlayerPromotePacket.getPartyID());

            UUID cachedLeader = partyData.getPartyLeader();
            partyData.setPartyLeader(player);
            partyData.getPartyMembers().add(cachedLeader);
            partyData.getPartyMembers().remove(player);
            return;
        }

        //message
        if(packet instanceof PartyMessagePacket partyMessagePacket){
            PartyData partyData = getPartyByID(partyMessagePacket.getPartyID());
            for(UUID uuid : partyData.getPartyMembers()){
                PlayerMessagePacket playerMessagePacket = new PlayerMessagePacket(uuid, partyMessagePacket.getMessage());
                plugin.getMeLinker().getPacketManager().sendPacket(playerMessagePacket);
            }

            PlayerMessagePacket playerMessagePacket = new PlayerMessagePacket(partyData.getPartyLeader(), partyMessagePacket.getMessage());
            plugin.getMeLinker().getPacketManager().sendPacket(playerMessagePacket);
            return;
        }

        //teleport
        if(packet instanceof PartyTeleportPacket partyTeleportPacket){
            PartyData partyData = getPartyByID(partyTeleportPacket.getPartyID());
            String server = plugin.getProxy().getPlayer(partyData.getPartyLeader()).getServer().getInfo().getName();

            for(UUID uuid : partyData.getPartyMembers()){
                ChangePlayerServerPacket playerMessagePacket = new ChangePlayerServerPacket(uuid, server);
                plugin.getMeLinker().getPacketManager().sendPacket(playerMessagePacket);
            }
            return;
        }

        //teleport singular
        if(packet instanceof PartyTeleportSingularPacket partyTeleportSingularPacket){
            PartyData partyData = getPartyByID(partyTeleportSingularPacket.getPartyID());
            String server = plugin.getProxy().getPlayer(partyData.getPartyLeader()).getServer().getInfo().getName();

            ChangePlayerServerPacket playerMessagePacket = new ChangePlayerServerPacket(partyTeleportSingularPacket.getPlayerID(), server);
            plugin.getMeLinker().getPacketManager().sendPacket(playerMessagePacket);
            return;
        }
    }
}
































