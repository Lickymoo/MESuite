package com.buoobuoo.mesuite.meitems.gamehandler.listener;

import com.buoobuoo.mesuite.meitems.CustomItemManager;
import com.buoobuoo.mesuite.meitems.ItemUtils;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.interf.AttributedItem;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.QueryItemModifiersEvent;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meutils.model.Pair;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class QueryItemModifiersEventListener implements Listener {

    private final MEItemsPlugin plugin;

    public QueryItemModifiersEventListener(MEItemsPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void listen(QueryItemModifiersEvent event){
        CustomItemManager itemManager = plugin.getCustomItemManager();
        EntityStatInstance statInstance = event.getStatInstance();

        Player player = event.getPlayer();
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);
        statInstance.setEntity(player);
        for(Pair<ItemStack, AttributedItem> pair : ItemUtils.getEquippedAttributedItems(plugin, player)){
            AttributedItem item = pair.getRight();
            if(!item.meetsRequirement(profileData))
                continue;

            ItemStack itemStack = pair.getLeft();
            item.onCalc(plugin, itemStack, statInstance);
        }
    }
}
