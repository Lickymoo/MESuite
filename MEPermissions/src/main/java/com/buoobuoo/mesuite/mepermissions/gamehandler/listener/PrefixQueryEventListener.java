package com.buoobuoo.mesuite.mepermissions.gamehandler.listener;

import com.buoobuoo.mesuite.mepermissions.MEPermissionsPlugin;
import com.buoobuoo.mesuite.mepermissions.PermissionGroup;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.display.PrefixQueryEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PrefixQueryEventListener implements Listener {

    private final MEPermissionsPlugin plugin;

    public PrefixQueryEventListener(MEPermissionsPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(PrefixQueryEvent event){
        Player player = event.getPlayer();
        PermissionGroup group = plugin.getPlayerPermissionDataManager().getPermissionData(player).getGroup();

        if(group == null)
            return;

        String prefix = group.getGroupPrefix();
        event.setPrefix(event.getPrefix() + prefix);

    }
}
