package com.buoobuoo.mesuite.meitems.impl.weapon.starter;


import com.buoobuoo.mesuite.meitems.ItemRarity;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack.BasePhysicalItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.requirements.impl.PlayerLevelRequirement;
import com.buoobuoo.mesuite.meitems.interf.*;
import com.buoobuoo.mesuite.meitems.interf.type.MeleeWeapon;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;

public class StarterAxeItem extends AttributedItem implements NotStackable, ItemLevel, MeleeWeapon, Cooldown, AlternateHeldItem {
    public StarterAxeItem() {
        super("STARTER_AXE", MatRepo.STARTER_AXE_ICON, "Starter Axe");

        this.setRarity(ItemRarity.COMMON);
        this.addRequirements(new PlayerLevelRequirement(1));

        this.addAttributes(new BasePhysicalItemAttribute(5));
    }

    @Override
    public int cooldownTicks() {
        return 20;
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
    public MatRepo swapoutItem() {
        return MatRepo.STARTER_AXE;
    }
}
