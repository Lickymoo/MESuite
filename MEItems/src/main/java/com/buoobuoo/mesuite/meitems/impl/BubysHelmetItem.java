package com.buoobuoo.mesuite.meitems.impl;

import com.buoobuoo.mesuite.meitems.ItemRarity;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.additional.attributes.impl.defense.MaximumHealthItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.attributes.impl.defense.MaximumManaItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.requirements.impl.PlayerLevelRequirement;
import com.buoobuoo.mesuite.meitems.interf.AttributedItem;
import com.buoobuoo.mesuite.meitems.interf.ItemLevel;
import com.buoobuoo.mesuite.meitems.interf.NotStackable;
import com.buoobuoo.mesuite.meitems.interf.type.Armour;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import org.bukkit.Material;

public class BubysHelmetItem extends AttributedItem implements NotStackable, ItemLevel, Armour {
    public BubysHelmetItem() {
        super("BUBYS_HELMET", Material.LEATHER_HELMET, "Buby's Helmet");

        this.setRarity(ItemRarity.ULTRA_RARE);
        this.addRequirements(new PlayerLevelRequirement(100));

        this.addAttributes(new MaximumHealthItemAttribute(100, 100));
        this.addAttributes(new MaximumManaItemAttribute(100, 100));
    }

    @Override
    public int itemLevel() {
        return 100;
    }

    @Override
    public void modifierCreate(MEItemsPlugin plugin, ItemBuilder ib) {
        NotStackable.super.modifierCreate(plugin, ib);
        ItemLevel.super.modifierCreate(plugin, ib);
    }
}
