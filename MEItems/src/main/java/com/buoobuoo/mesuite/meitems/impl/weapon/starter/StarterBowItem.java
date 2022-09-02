package com.buoobuoo.mesuite.meitems.impl.weapon.starter;
import com.buoobuoo.mesuite.meitems.ItemRarity;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack.BaseRangedItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.requirements.impl.PlayerLevelRequirement;
import com.buoobuoo.mesuite.meitems.interf.AttributedItem;
import com.buoobuoo.mesuite.meitems.interf.Cooldown;
import com.buoobuoo.mesuite.meitems.interf.ItemLevel;
import com.buoobuoo.mesuite.meitems.interf.NotStackable;
import com.buoobuoo.mesuite.meitems.interf.type.RangedWeapon;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;

public class StarterBowItem extends AttributedItem implements NotStackable, ItemLevel, Cooldown, RangedWeapon {
    public StarterBowItem() {
        super("STARTER_BOW", MatRepo.STARTER_BOW, "Starter Bow");

        this.setRarity(ItemRarity.COMMON);
        this.addRequirements(new PlayerLevelRequirement(1));

        this.addAttributes(new BaseRangedItemAttribute(3));
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
    public Class<? extends Projectile> projectileType() {
        return Arrow.class;
    }
}
