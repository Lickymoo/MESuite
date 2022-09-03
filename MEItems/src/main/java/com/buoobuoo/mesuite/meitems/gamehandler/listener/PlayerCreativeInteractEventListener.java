package com.buoobuoo.mesuite.meitems.gamehandler.listener;

import com.buoobuoo.mesuite.meitems.CustomItem;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.interf.type.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerCreativeInteractEventListener implements Listener {

    private final MEItemsPlugin plugin;

    public PlayerCreativeInteractEventListener(MEItemsPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(BlockBreakEvent event){
        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();
        CustomItem handler = plugin.getCustomItemManager().getRegistry().getHandler(item);

        if(!(handler instanceof Weapon))
            return;

        event.setCancelled(true);


    }
}
