package com.buoobuoo.mesuite.melinker.util;

import com.buoobuoo.mesuite.melinker.redis.AbsPacketManager;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.PartyMessagePacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PartyData {
    private UUID partyID;
    private UUID partyLeader;
    private List<UUID> partyMembers = new ArrayList<>();
    private List<UUID> invitedPlayers = new ArrayList<>();

    public boolean isLeader(Player player){
        return partyLeader.equals(player.getUniqueId());
    }

    public boolean isInParty(Player player){
        return isInParty(player.getUniqueId());
    }

    public boolean isInParty(UUID uuid){
        if(partyMembers.contains(uuid))
            return true;

        if(partyLeader.equals(uuid))
            return true;

        return false;
    }

    public boolean isInvited(UUID uuid){
        return invitedPlayers.contains(uuid);
    }

    public void messageParty(AbsPacketManager packetManager, String message){
        PartyMessagePacket partyMessagePacket = new PartyMessagePacket(partyID, message);
        packetManager.sendPacket(partyMessagePacket);
    }
}
