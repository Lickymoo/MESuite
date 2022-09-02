package com.buoobuoo.mesuite.mequests.gamehandler.listener;

import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.ProfileDataLoadEvent;
import com.buoobuoo.mesuite.mequests.MEQuestsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ProfileDataLoadEventListener implements Listener {

    private final MEQuestsPlugin plugin;

    public ProfileDataLoadEventListener(MEQuestsPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(ProfileDataLoadEvent event){
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
        plugin.getQuestManager().loadQuests(event.getData());
        }, 10);
    }

}
