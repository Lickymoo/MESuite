package com.buoobuoo.mesuite.meutils.stats;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;

@Getter
@Setter
public abstract class EntityStatInstance {

    protected double maxHealth = 0;
    protected double currentHealth = 0; // for entities
    protected double maxMana = 0;
    protected double defense;

    protected double manaRegenPS = 1;
    protected double healthRegenPS = 1;

    protected double walkSpeed = 0;


    protected double damageDealt = 0;
    protected double critStrikeChance = 0;
    protected double critStrikeMulti = 100;

    protected Entity entity;

    public void increaseMaxHealth(double amt){
        this.maxHealth += amt;
    }

    public void increaseMaxMana(double amt){
        this.maxMana += amt;
    }

    public void increaseDefense(double amt){
        this.defense += amt;
    }

    public void increaseManaRegenPS(double amt){
        this.manaRegenPS += amt;
    }

    public void increaseHealthRegenPS(double amt){
        this.healthRegenPS += amt;
    }

    public void increaseWalkSpeed(double amt){
        this.walkSpeed += amt;
    }

    public void increaseDamageDealt(double amt){
        damageDealt += amt;
    }

    public void increaseCritStrikeChance(double amt){
        critStrikeChance += amt;
    }

    public void increaseCritStrikeMulti(double amt){
        critStrikeMulti += amt;
    }
}
