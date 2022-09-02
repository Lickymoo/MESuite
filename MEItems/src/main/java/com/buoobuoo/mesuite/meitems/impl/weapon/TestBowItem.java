package com.buoobuoo.mesuite.meitems.impl.weapon;

import com.buoobuoo.mesuite.meitems.ItemRarity;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack.BasePhysicalItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack.CriticalMultiplierItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack.CriticalStrikeItemAttribute;
import com.buoobuoo.mesuite.meitems.interf.AttributedItem;
import com.buoobuoo.mesuite.meitems.interf.ItemLevel;
import com.buoobuoo.mesuite.meitems.interf.NotStackable;
import com.buoobuoo.mesuite.meitems.interf.type.RangedWeapon;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Projectile;

public class TestBowItem extends AttributedItem implements NotStackable, ItemLevel, RangedWeapon {
    public TestBowItem() {
        super("TEST_BOW", Material.BOW, "Test Bow");

        this.setRarity(ItemRarity.ULTRA_RARE);
        this.addAttributes(new BasePhysicalItemAttribute(100000));
        this.addAttributes(new CriticalStrikeItemAttribute(50, 75));
        this.addAttributes(new CriticalMultiplierItemAttribute(100, 200));
    }

    @Override
    public int itemLevel() {
        return 1;
    }

    @Override
    public void modifierCreate(MEItemsPlugin plugin, ItemBuilder ib) {
        NotStackable.super.modifierCreate(plugin, ib);
        ItemLevel.super.modifierCreate(plugin, ib);
    }

    @Override
    public Class<? extends Projectile> projectileType() {
        return Egg.class;
    }
}
