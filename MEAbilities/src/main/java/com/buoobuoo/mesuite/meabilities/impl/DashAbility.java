package com.buoobuoo.mesuite.meabilities.impl;

import com.buoobuoo.mesuite.meabilities.Ability;
import com.buoobuoo.mesuite.meabilities.AbilityCastType;
import com.buoobuoo.mesuite.meabilities.AbilityType;
import com.buoobuoo.mesuite.meabilities.MEAbilitiesPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DashAbility extends Ability {

    private final double dashBlocks = 3;

    public DashAbility() {
        super(AbilityType.DEXTERITY, 10, 40, "DASH", "&fDash", "&r&7Dash &f3 &7blocks forward");
    }

    @Override
    public void onCast(MEAbilitiesPlugin plugin, Player player, Entity ent, double effectiveness) {
        player.setVelocity(player.getEyeLocation().getDirection().clone().setY(0).multiply(dashBlocks * effectiveness));
    }

    @Override
    public String[] getLore(AbilityCastType type){
        double effectiveness = type.getEffectiveness();
        double val = dashBlocks * effectiveness;
        String[] lore = new String[]{
            "&r&7Dash &f" + String.format("%.2f", val) + " &7blocks forward"
        };

        return super.getLore(type, lore);
    }
}
