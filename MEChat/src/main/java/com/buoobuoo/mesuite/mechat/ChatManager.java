package com.buoobuoo.mesuite.mechat;

import com.buoobuoo.mesuite.melinker.redis.AbsPacketManager;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.StaffMessagePacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.PartyDataByPlayerRequest;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.PartyDataRequestResponsePacket;
import com.buoobuoo.mesuite.melinker.util.PartyData;
import com.buoobuoo.mesuite.mepermissions.data.PlayerPermissionData;
import com.buoobuoo.mesuite.meplayerdata.model.PlayerData;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.JavaUtils;
import com.buoobuoo.mesuite.meutils.MEUtils;
import com.buoobuoo.mesuite.meutils.PacketUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class ChatManager implements Listener {

    public static final String OVERRIDE_TAG = "@$OR$@";
    private static final String CLEAR_STRING_TEXT = StringUtils.repeat(OVERRIDE_TAG + " \n", 100);

    private final MEChatPlugin plugin;
    private static final Map<UUID, ChatRestrictionMode> chatMode = new HashMap<>();
    private static final Map<UUID, LinkedList<BaseComponent[]>> previousChat = new HashMap<>();

    public ChatManager(MEChatPlugin plugin){
        this.plugin = plugin;
    }

    public void receiveMessage(Player player, BaseComponent[] baseComponents){
        UUID uuid = player.getUniqueId();
        previousChat.putIfAbsent(uuid, new LinkedList<>());
        previousChat.get(uuid).add(baseComponents);
        if(previousChat.get(uuid).size() > 100){
            previousChat.get(uuid).pop();
        }
    }

    public ChatRestrictionMode getChatMode(Player player){
        return chatMode.getOrDefault(player.getUniqueId(), ChatRestrictionMode.UNRESTRICTED);
    }

    public void setChatMode(Player player, ChatRestrictionMode mode){
        UUID uuid = player.getUniqueId();
        List<BaseComponent[]> chats = previousChat.get(uuid);

        chatMode.put(player.getUniqueId(), mode);
        switch (mode){
            case UNRESTRICTED -> {
                player.sendMessage(OVERRIDE_TAG + CLEAR_STRING_TEXT);
                for(BaseComponent[] components : previousChat.getOrDefault(uuid, new LinkedList<>())){
                    BaseComponent[] sendComponents = JavaUtils.concatenateArrays(TextComponent.fromLegacyText(OVERRIDE_TAG), components);

                    player.spigot().sendMessage(sendComponents);
                }
            }
            case DIALOGUE_RESTRICTION -> {
                player.sendMessage(OVERRIDE_TAG + CLEAR_STRING_TEXT);

                for(BaseComponent[] components : chats){
                    BaseComponent[] newComponents = new BaseComponent[components.length];

                    //grayscale
                    for(int i = 0; i < components.length; i++){
                        TextComponent component = new TextComponent(components[i].toLegacyText());
                        String text = component.getText();
                        text = MEUtils.grayScaleString(text);
                        component.setText(text);
                        newComponents[i] = component;
                    }

                    BaseComponent[] sendComponents = JavaUtils.concatenateArrays(TextComponent.fromLegacyText(OVERRIDE_TAG), newComponents);
                    player.spigot().sendMessage(sendComponents);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event){
        String message = event.getMessage();

        //color coding
        message = Colour.format(message);
        event.setMessage(message);

        String prefix = message.split(" ")[0];
        ChatContext context = ChatContext.matchContext(prefix);
        Player player = event.getPlayer();

        PlayerData senderData = plugin.getPlayerDataManager().getData(player);

        event.setCancelled(true);

        if(getChatMode(player) != ChatRestrictionMode.UNRESTRICTED){
            return;
        }

        switch (context){
            case PARTY:
                String finalMessage = message;
                async(() -> {
                    AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();

                    PartyDataRequestResponsePacket getPartyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3);
                    PartyData partyData = getPartyData.getPartyData();

                    if(partyData == null){
                        player.sendMessage(Colour.format("&7You are not in a party"));
                        return;
                    }

                    event.setMessage(finalMessage.replaceFirst(prefix, ""));
                    partyData.messageParty(packetManager, "&d@party &f" + player.getDisplayName() + ":&7" + event.getMessage());
                });
                break;
            case STAFF:
                String finalMessage1 = message;
                async(() -> {
                    AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
                    PlayerPermissionData permissionData = plugin.getPlayerPermissionDataManager().getPermissionData(player);
                    if(!permissionData.getGroup().hasPermission("me.admin")){
                        player.sendMessage(Colour.format("&7You are not a staff member"));
                        return;
                    }
                    event.setMessage(finalMessage1.replaceFirst(prefix, ""));
                    StaffMessagePacket messagePacket = new StaffMessagePacket(Colour.format("&b@staff &f" + player.getDisplayName() + ":&7" + event.getMessage()));
                    packetManager.sendPacket(messagePacket);
                });
                break;
            case GLOBAL:
                //replace message with system chat makes life easier
                Component component = Component.literal(Colour.format("&f" + player.getDisplayName() + ":&7 " + event.getMessage()));

                ClientboundSystemChatPacket packet = new ClientboundSystemChatPacket(component, 1);
                PacketUtils.sendPacketGlobal(packet);
                break;
            default:
                break;
        }
    }

    public void async(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }



}
