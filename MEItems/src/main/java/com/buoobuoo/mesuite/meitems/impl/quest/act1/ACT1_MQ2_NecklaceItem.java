package com.buoobuoo.mesuite.meitems.impl.quest.act1;

import com.buoobuoo.mesuite.meitems.CustomItem;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.interf.NotStackable;
import com.buoobuoo.mesuite.meitems.interf.QuestItem;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import org.bukkit.Material;

public class ACT1_MQ2_NecklaceItem extends CustomItem implements QuestItem, NotStackable {
    public ACT1_MQ2_NecklaceItem() {
        super("ACT1_MQ3_NECKLACE", Material.EMERALD, "Expeditioners necklace");
    }

    @Override
    public void modifierCreate(MEItemsPlugin plugin, ItemBuilder ib) {
        NotStackable.super.modifierCreate(plugin, ib);
        QuestItem.super.modifierCreate(plugin, ib);
    }
}
