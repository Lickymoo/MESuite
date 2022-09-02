package com.buoobuoo.mesuite.meitems.impl.weapon.starter;

import com.buoobuoo.mesuite.meitems.ItemRarity;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack.BaseMagicItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.requirements.impl.PlayerLevelRequirement;
import com.buoobuoo.mesuite.meitems.interf.AttributedItem;
import com.buoobuoo.mesuite.meitems.interf.Cooldown;
import com.buoobuoo.mesuite.meitems.interf.ItemLevel;
import com.buoobuoo.mesuite.meitems.interf.NotStackable;
import com.buoobuoo.mesuite.meitems.interf.type.MagicWeapon;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class StarterStaffItem extends AttributedItem implements NotStackable, ItemLevel, MagicWeapon, Cooldown {
    public StarterStaffItem() {
        super("STARTER_STAFF", MatRepo.STARTER_STAFF, "Starter Staff");

        this.setRarity(ItemRarity.COMMON);
        this.addRequirements(new PlayerLevelRequirement(1));

        this.addAttributes(new BaseMagicItemAttribute(3));
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
    public void spawnParticle(Location loc){
        Particle.DustOptions dustR = new Particle.DustOptions(Color.WHITE, 1F);
        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 1, 50, dustR);
    }
}
