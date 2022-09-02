package com.buoobuoo.mesuite.mesocial.friend;

import com.buoobuoo.mesuite.melinker.redis.packet.impl.PlayerMessagePacket;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.mesocial.MESocialPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FriendManager implements Listener {

    private final MESocialPlugin plugin;
    private final Map<UUID, PlayerFriendData> friendDataHashMap = new HashMap<>();

    /*
    MANIFESTO
    Do not cache any friend data- query on demand to allow up-to-date data
    Allows adding online players

    Send friend request
    -Add player to friend request list

    Add friends
    -Add both players to each other's friend list
     */

    public FriendManager(MESocialPlugin plugin){
        this.plugin = plugin;
    }

    public void sendFriendRequest(UUID sender, UUID target){
        Player localPlayer = Bukkit.getPlayer(sender);

        NetworkedPlayer senderNetworked = plugin.getNetworkedPlayer(sender);
        NetworkedPlayer targetNetworked = plugin.getNetworkedPlayer(target);

        PlayerFriendData targetData = loadFriendData(target);
        PlayerFriendData senderData = loadFriendData(sender);

        if(targetData.getFriendList().contains(sender)){
            localPlayer.sendMessage("You are already friends with " + targetNetworked.getName());
            return;
        }

        if(senderData.getFriendRequests().contains(target)){
            addFriends(sender, target);
            return;
        }

        if(targetData.getFriendRequests().contains(sender)){
            localPlayer.sendMessage("You have already sent a friend request to " + targetNetworked.getName());
            return;
        }

        localPlayer.sendMessage("Friend request sent to " + targetNetworked.getName());
        targetData.getFriendRequests().add(sender);
        saveFriendData(target, targetData);

        PlayerMessagePacket packet = new PlayerMessagePacket(target,
                "You have received a friend request from " + localPlayer.getName(),
                "Type /friend add " + localPlayer.getName() + " to accept");
        plugin.getMeLinker().getPacketManager().sendPacket(packet);
    }

    public void addFriends(UUID a, UUID b){
        PlayerFriendData aData = loadFriendData(a);
        PlayerFriendData bData = loadFriendData(b);

        aData.getFriendRequests().remove(b);
        bData.getFriendRequests().remove(a);

        aData.getFriendList().add(b);
        bData.getFriendList().add(a);

        saveFriendData(a, aData);
        saveFriendData(b, bData);

        NetworkedPlayer aNetworked = plugin.getNetworkedPlayer(a);
        NetworkedPlayer bNetworked = plugin.getNetworkedPlayer(b);

        PlayerMessagePacket aMsg = new PlayerMessagePacket(a,
                "You are now friends with " + bNetworked.getName());

        PlayerMessagePacket bMsg = new PlayerMessagePacket(b,
                "You are now friends with " + aNetworked.getName());

        plugin.getMeLinker().getPacketManager().sendPacket(bMsg);
        plugin.getMeLinker().getPacketManager().sendPacket(aMsg);
    }

    public void removeFriends(UUID a, UUID b){
        PlayerFriendData aData = loadFriendData(a);
        PlayerFriendData bData = loadFriendData(b);

        if(!aData.getFriendList().contains(b))
            return;

        aData.getFriendRequests().remove(b);
        bData.getFriendRequests().remove(a);

        aData.getFriendList().remove(b);
        bData.getFriendList().remove(a);

        saveFriendData(a, aData);
        saveFriendData(b, bData);

        NetworkedPlayer aNetworked = plugin.getNetworkedPlayer(a);
        NetworkedPlayer bNetworked = plugin.getNetworkedPlayer(b);

        PlayerMessagePacket aMsg = new PlayerMessagePacket(a,
                "You are no longer friends with " + bNetworked.getName());

        PlayerMessagePacket bMsg = new PlayerMessagePacket(b,
                "You are no longer friends with " + aNetworked.getName());

        plugin.getMeLinker().getPacketManager().sendPacket(bMsg);
        plugin.getMeLinker().getPacketManager().sendPacket(aMsg);
    }

    public PlayerFriendData loadFriendData(UUID uuid){
        return plugin.getMongoHook().loadObject(uuid.toString(), PlayerFriendData.class, "friendData");
    }

    public void saveFriendData(UUID uuid, PlayerFriendData data){
        plugin.getMongoHook().saveObject(uuid.toString(), data, "friendData");
    }
}


































