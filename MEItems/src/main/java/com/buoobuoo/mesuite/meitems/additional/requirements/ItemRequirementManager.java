package com.buoobuoo.mesuite.meitems.additional.requirements;

import com.buoobuoo.mesuite.meitems.CustomItem;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.interf.AttributedItem;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class ItemRequirementManager {
    private final MEItemsPlugin plugin;

    public ItemRequirementManager(MEItemsPlugin plugin){
        this.plugin = plugin;
    }

    public List<ItemRequirement> getAttribInstances(ItemStack item){
        CustomItem handler = plugin.getCustomItemManager().getRegistry().getHandler(item);
        if(!(handler instanceof AttributedItem))
            return null;

        AttributedItem aHandler = (AttributedItem)handler;
        return aHandler.getRequirements();

    }
}
