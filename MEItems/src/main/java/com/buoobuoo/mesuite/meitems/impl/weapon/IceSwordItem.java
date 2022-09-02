package com.buoobuoo.mesuite.meitems.impl.weapon;

import com.buoobuoo.mesuite.meitems.ItemRarity;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack.BasePhysicalItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack.CriticalMultiplierItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack.CriticalStrikeItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.requirements.impl.PlayerLevelRequirement;
import com.buoobuoo.mesuite.meitems.interf.AttributedItem;
import com.buoobuoo.mesuite.meitems.interf.Cooldown;
import com.buoobuoo.mesuite.meitems.interf.ItemLevel;
import com.buoobuoo.mesuite.meitems.interf.NotStackable;
import com.buoobuoo.mesuite.meitems.interf.type.MeleeWeapon;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;

public class IceSwordItem extends AttributedItem implements NotStackable, ItemLevel, MeleeWeapon, Cooldown {
    public IceSwordItem() {
        super("ICE_SWORD", MatRepo.GREEN_TICK, "Ice Sword");

        this.setRarity(ItemRarity.ULTRA_RARE);
        this.addRequirements(new PlayerLevelRequirement(10));

        this.addAttributes(new BasePhysicalItemAttribute(10));
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
    public int cooldownTicks() {
        return 20;
    }
}
