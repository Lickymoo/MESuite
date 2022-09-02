package com.buoobuoo.mesuite.melinker.util;

import com.buoobuoo.mesuite.melinker.redis.AbsPacketManager;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.*;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.*;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.*;

import java.util.Random;

public class Util {
    public static String randomString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    public static void registerDefaultPackets(AbsPacketManager packetManager){
        packetManager.registerPacket("PLAYER_JOIN", PlayerJoinMEPacket.class);
        packetManager.registerPacket("PLAYER_LEAVE", PlayerLeaveMEPacket.class);
        packetManager.registerPacket("PLAYER_DATA_GLOBAL", PlayerDataBroadcastPacket.class);
        packetManager.registerPacket("PLAYER_MESSAGE", PlayerMessagePacket.class);
        packetManager.registerPacket("STAFF_MESSAGE", StaffMessagePacket.class);

        packetManager.registerPacket("REGISTER_SERVER", RegisterServerPacket.class);
        packetManager.registerPacket("REGISTER_SERVER_RESPONSE", RegisterServerResponsePacket.class);

        packetManager.registerPacket("INSTANCE_BOUND_PING", InstanceboundPingPacket.class);
        packetManager.registerPacket("INSTANCE_BOUND_PING_RESPONSE", InstanceboundPingResponsePacket.class);

        packetManager.registerPacket("CHANGE_PLAYER_SERVER", ChangePlayerServerPacket.class);

        packetManager.registerPacket("PARTY_DATA_BY_PLAYER", PartyDataByPlayerRequest.class);
        packetManager.registerPacket("PARTY_MESSAGE", PartyMessagePacket.class);
        packetManager.registerPacket("PARTY_DATA_REQUEST", PartyDataRequestPacket.class);
        packetManager.registerPacket("PARTY_DATA_REQUEST_RESPONSE", PartyDataRequestResponsePacket.class);
        packetManager.registerPacket("PARTY_CREATE", PartyCreatePacket.class);
        packetManager.registerPacket("PARTY_DISBAND", PartyDisbandPacket.class);
        packetManager.registerPacket("PARTY_LEAVE", PartyPlayerLeavePacket.class);
        packetManager.registerPacket("PARTY_INVITE", PartyPlayerInvitePacket.class);
        packetManager.registerPacket("PARTY_PROMOTE", PartyPlayerPromotePacket.class);
        packetManager.registerPacket("PARTY_KICK", PartyPlayerKickPacket.class);
        packetManager.registerPacket("PARTY_JOIN", PartyPlayerJoinPacket.class);
        packetManager.registerPacket("PARTY_TELEPORT", PartyTeleportPacket.class);
        packetManager.registerPacket("PARTY_TELEPORT_SINGULAR", PartyTeleportSingularPacket.class);
    }
}
