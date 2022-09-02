package com.buoobuoo.mesuite.meitems;

import com.buoobuoo.mesuite.meitems.interf.AttributedItem;
import com.buoobuoo.mesuite.meitems.interf.type.Weapon;
import com.buoobuoo.mesuite.meutils.model.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static List<Pair<ItemStack, AttributedItem>> getEquippedAttributedItems(MEItemsPlugin plugin, Player player){
        CustomItemManager customItemManager = plugin.getCustomItemManager();
        List<ItemStack> items = new ArrayList<>();
        List<Pair<ItemStack, AttributedItem>> attributedItems = new ArrayList<>();

        ItemStack hand = player.getInventory().getItemInMainHand();
        CustomItem handHandler = customItemManager.getRegistry().getHandler(hand);
        if(!(handHandler instanceof Weapon))
            hand = null;

        ItemStack helm = player.getInventory().getHelmet();
        ItemStack chest = player.getInventory().getChestplate();
        ItemStack legs = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        items.add(hand);
        items.add(helm);
        items.add(chest);
        items.add(legs);
        items.add(boots);

        for(ItemStack item : items){
            if(item == null)
                continue;

            CustomItem handler = customItemManager.getRegistry().getHandler(item);
            if(!(handler instanceof AttributedItem))
                continue;
            attributedItems.add(Pair.of(item, (AttributedItem) handler));
        }

        return attributedItems;
    }
}
