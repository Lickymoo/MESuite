package com.buoobuoo.mesuite.meitems.interf;

import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meutils.ItemBuilder;

public interface QuestItem extends Modifier {

    @Override
    default void modifierCreate(MEItemsPlugin plugin, ItemBuilder ib){
        ib.nbtInteger(plugin, "me-quest-item", 1);
    }
}
