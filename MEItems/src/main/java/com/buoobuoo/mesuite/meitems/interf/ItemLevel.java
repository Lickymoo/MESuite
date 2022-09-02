package com.buoobuoo.mesuite.meitems.interf;

import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meutils.ItemBuilder;

public interface ItemLevel extends Modifier {

    int itemLevel();

    @Override
    default void modifierCreate(MEItemsPlugin plugin, ItemBuilder ib){
        ib.nbtInt(plugin, "me-item-level", itemLevel());
        ib.lore(1, "&r&7Item Level &f" + itemLevel());
    }
}
