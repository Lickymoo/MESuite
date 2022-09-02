package com.buoobuoo.mesuite.meabilities;

import com.buoobuoo.mesuite.meabilities.data.ProfileAbilityData;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meutils.JavaUtils;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@Getter
public abstract class Ability {
    private final AbilityType type;
    private final String name;
    private final String id;
    private final String[] lore;
    private final double manaCost;
    private final int abilityCooldown;

    private final double radius = 5;

    public Ability(AbilityType type, double manaCost, int abilityCooldown, String id, String name, String... lore){
        this.type = type;
        this.name = name;
        this.id = id;
        this.lore = lore;
        this.manaCost = manaCost;
        this.abilityCooldown = abilityCooldown;
    }

    public String[] getLore(AbilityCastType type, String[] lore){
        return JavaUtils.addToArr(lore, "", "&r&f" + String.format("%.2f", getAbilityCooldown()/20d) + "s &7Cooldown", "&7Consumes &f" + getManaCost() + " &7mana on use");
    }

    public String[] getLore(AbilityCastType type){
        return JavaUtils.addToArr(lore, "", "&r&f" + String.format("%.2f", getAbilityCooldown()/20d) + "s &7Cooldown", "&7Consumes &f" + getManaCost() + " &7mana on use");
    }

    public String[] getLore(){
        return JavaUtils.addToArr(lore, "", "&r&f" + String.format("%.2f", getAbilityCooldown()/20d) + "s &7Cooldown", "&7Consumes &f" + getManaCost() + " &7mana on use");
    }

    public void cast(MEAbilitiesPlugin plugin, Player player, Entity target, double effectiveness) {
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(player);
        ProfileAbilityData abilityData = plugin.getAbilityDataManager().getAbilityData(player);
        AbilityManager abilityManager = plugin.getAbilityManager();
        double profileMana = profileData.getMana();

        if (abilityManager.getAbilityCoolDown(abilityData, this) > 0)
            return;

        if(profileMana <= getManaCost())
            return;

        if(!canCast(profileData, effectiveness))
            return;

        onCast(plugin, player, target, effectiveness);

        profileData.setMana(profileMana - getManaCost());
        abilityManager.setAbilityCoolDown(abilityData, this);
    }

    public abstract void onCast(MEAbilitiesPlugin plugin, Player player, Entity target, double effectiveness);

    public boolean canCast(ProfileData profileData, double effectiveness){
        return true;
    }
}

























