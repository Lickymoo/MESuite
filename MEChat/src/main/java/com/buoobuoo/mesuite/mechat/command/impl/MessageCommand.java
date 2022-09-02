package com.buoobuoo.mesuite.mechat.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import com.buoobuoo.mesuite.mechat.MEChatPlugin;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.PlayerMessagePacket;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import org.bukkit.entity.Player;

@CommandAlias("message|msg|pm|dm")
public class MessageCommand extends BaseCommand {
    private final MEChatPlugin plugin;

    public MessageCommand(MEChatPlugin plugin){
        this.plugin = plugin;
    }

    @Default
    @CommandCompletion("@networked-players-name")
    public void msg(Player player, NetworkedPlayer target, String message){
        if(target == null){
            player.sendMessage("Could not find player");
            return;
        }
        PlayerMessagePacket packet = new PlayerMessagePacket(target.getUuid(), player.getName() + ": " + message);
        plugin.getMeLinker().getPacketManager().sendPacket(packet);

        player.sendMessage("To " + target.getName() + ": " + message);
    }
}
