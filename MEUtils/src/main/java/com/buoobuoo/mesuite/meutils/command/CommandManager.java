package com.buoobuoo.mesuite.meutils.command;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager extends PaperCommandManager{

    public CommandManager(JavaPlugin plugin){
        super(plugin);
    }

    public void registerCommand(BaseCommand... cmds) {
        for(BaseCommand cmd : cmds) {
            registerCommand(cmd);
        }
    }
}
