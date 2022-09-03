package com.buoobuoo.mesuite.mepermissions.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.buoobuoo.mesuite.mepermissions.MEPermissionsPlugin;
import com.buoobuoo.mesuite.mepermissions.PermissionGroup;
import com.buoobuoo.mesuite.mepermissions.PermissionManager;
import com.buoobuoo.mesuite.meutils.Colour;
import org.bukkit.command.CommandSender;

@CommandAlias("permission|perm")
public class PermissionCommand extends BaseCommand {

    private final MEPermissionsPlugin plugin;
    private final PermissionManager permissionManager;

    public PermissionCommand(MEPermissionsPlugin plugin){
        this.plugin = plugin;
        this.permissionManager = plugin.getPermissionManager();
    }

    @Subcommand("group")
    public class group extends BaseCommand{

        @Subcommand("set")
        @CommandCompletion("@players @perm-groups")
        public void set(final CommandSender sender, OnlinePlayer target, PermissionGroup group)
        {
            permissionManager.setPlayerGroup(target.player, group);
            sender.sendMessage(Colour.format(target.player.getDisplayName() + "&7's group set to " + plugin.getPlayerPermissionDataManager().getPermissionData(target.player.getUniqueId()).getGroup().getGroupID()));

        }
    }


}




