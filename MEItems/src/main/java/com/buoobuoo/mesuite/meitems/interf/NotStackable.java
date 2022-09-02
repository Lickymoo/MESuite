package com.buoobuoo.mesuite.meitems.interf;

import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meutils.ItemBuilder;

import java.util.UUID;

public interface NotStackable extends Modifier {

    @Override
    default void modifierCreate(MEItemsPlugin plugin, ItemBuilder ib){
        ib.nbtString(plugin, "me-rand-id", UUID.randomUUID().toString());
    }
}
