package com.buoobuoo.mesuite.mequests.gamehandler.listener;

import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.ProfileDataLoadEvent;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.ProfileDataRemoveEvent;
import com.buoobuoo.mesuite.mequests.MEQuestsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ProfileDataRemoveEventListener implements Listener {

    private final MEQuestsPlugin plugin;

    public ProfileDataRemoveEventListener(MEQuestsPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(ProfileDataRemoveEvent event){
        Player player = Bukkit.getPlayer(event.getData().getOwnerID());
        plugin.getQuestManager().clearPlayer(player);
    }

}
