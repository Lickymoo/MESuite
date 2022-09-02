package com.buoobuoo.mesuite.meitems.additional.attributes;

import com.buoobuoo.mesuite.meitems.CustomItem;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.interf.AttributedItem;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemAttributeManager {
    private final MEItemsPlugin plugin;

    public ItemAttributeManager(MEItemsPlugin plugin){
        this.plugin = plugin;
    }

    public List<ItemAttributeInstance> getAttribInstances(ItemStack item){
        CustomItem handler = plugin.getCustomItemManager().getRegistry().getHandler(item);
        if(!(handler instanceof AttributedItem))
            return null;

        AttributedItem aHandler = (AttributedItem)handler;

        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        String list = pdc.get(NamespacedKey.minecraft("me-attrib-list"), PersistentDataType.STRING);

        List<ItemAttributeInstance> instances = new ArrayList<>();
        String[] attribs = list.split(",");

        for(String attrib : attribs){
            String id = attrib.split(":")[0];
            double val = Double.parseDouble(attrib.split(":")[1]);

            ItemAttribute attribute = aHandler.getItemAttribByID(id);
            if(attribute == null)
                continue;

            ItemAttributeInstance instance = new ItemAttributeInstance(attribute, val);
            instances.add(instance);
        }
        return instances;

    }
}
