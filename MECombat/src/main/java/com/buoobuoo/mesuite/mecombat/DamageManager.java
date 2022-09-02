package com.buoobuoo.mesuite.mecombat;

import com.buoobuoo.mesuite.mecombat.gamehandler.event.CustomEntityKillByPlayerEvent;
import com.buoobuoo.mesuite.meentities.EntityManager;
import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import com.buoobuoo.mesuite.meentities.interf.tags.Invulnerable;
import com.buoobuoo.mesuite.meentities.stats.CustomEntityStatInstance;
import com.buoobuoo.mesuite.meitems.CustomItem;
import com.buoobuoo.mesuite.meitems.CustomItemManager;
import com.buoobuoo.mesuite.meitems.interf.type.MeleeWeapon;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meplayerdata.stats.PlayerStatInstance;
import com.buoobuoo.mesuite.meutils.JavaUtils;
import com.buoobuoo.mesuite.meutils.gamehandler.event.PlayerCritEvent;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;
import com.buoobuoo.mesuite.meutils.stats.TemporaryStatModifier;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DamageManager implements Listener {

    private final MECombatPlugin plugin;

    public DamageManager(MECombatPlugin plugin){
        this.plugin = plugin;
    }

    //P2P = player -> player
    //P2E = player -> entity
    //E2P = entity -> player

    public void handleDamageP2P(Player damager, Player damagee){
        //TODO
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(damager);
        EntityStatInstance statInstance = new PlayerStatInstance(profileData);

        ProfileData damageeData = plugin.getPlayerDataManager().getProfile(damagee);
        EntityStatInstance damageeStatInstance = new PlayerStatInstance(damageeData);

        String damagePrefix = "&f";

        //on hit stat modifires
        if(profileData.getOnHitStatModifier() != null) {
            for (TemporaryStatModifier statModifier : profileData.getOnHitStatModifier()) {
                statModifier.getInstanceConsumer().accept(statInstance);
                profileData.getOnHitStatModifier().remove(statModifier);
            }
        }

        double critMulti = statInstance.getCritStrikeMulti();
        double critChance = statInstance.getCritStrikeChance();
        double damage = statInstance.getDamageDealt();

        int chance = JavaUtils.randomInt(0, 100);
        if(chance <= critChance) {
            double multi = critMulti/100;

            statInstance.setDamageDealt(multi * damage);
            damagePrefix = "&c&l";
            Bukkit.getPluginManager().callEvent(new PlayerCritEvent(damager, damagee));
        }


        double damageDealt = statInstance.getDamageDealt();
        damageeData.setHealth(damageeStatInstance.getCurrentHealth()  - damageDealt);

        spawnDamageIndicator(damagePrefix, damagee.getLocation(), damageDealt);
    }

    public void handleDamageP2E(Player damager, CustomEntity damagee, double effectiveness){
        if(damagee instanceof Invulnerable)
            return;

        ProfileData profileData = plugin.getPlayerDataManager().getProfile(damager);
        EntityStatInstance statInstance = new PlayerStatInstance(profileData);

        String damagePrefix = "&f";

        //on hit stat modifires
        if(profileData.getOnHitStatModifier() != null) {
            for (TemporaryStatModifier statModifier : profileData.getOnHitStatModifier()) {
                statModifier.getInstanceConsumer().accept(statInstance);
                profileData.getOnHitStatModifier().remove(statModifier);
            }
        }

        double critMulti = statInstance.getCritStrikeMulti();
        double critChance = statInstance.getCritStrikeChance();
        double damage = statInstance.getDamageDealt();

        int chance = JavaUtils.randomInt(0, 100);
        if(chance <= critChance) {
            double multi = critMulti/100;

            statInstance.setDamageDealt(multi * damage);
            damagePrefix = "&c&l";
            Bukkit.getPluginManager().callEvent(new PlayerCritEvent(damager, damagee.asEntity().getBukkitEntity()));
        }

        damagee.addDamager(damager.getUniqueId());

        double damageDealt = statInstance.getDamageDealt() * effectiveness;
        damageEntity(damager, damagee.asEntity().getBukkitEntity(), damageDealt);

        spawnDamageIndicator(damagePrefix, damagee.asEntity().getBukkitEntity().getLocation(), damageDealt);
    }

    public void handleDamageE2P(CustomEntity damager, Player damagee){
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(damagee);
        EntityStatInstance statInstance = new CustomEntityStatInstance(plugin.getMeEntitiesPlugin(), damager);

        double damageDealt = statInstance.getDamageDealt();
        profileData.setHealth(profileData.getHealth() - damageDealt);
    }

    private void damageEntity(Entity damgaer, Player damagee, double damage){
        ProfileData profileData = plugin.getPlayerDataManager().getProfile(damagee);

        double health = profileData.getHealth();
        profileData.setHealth(health - damage);
    }

    private void spawnDamageIndicator(String prefix, Location loc, double damageAmount){
        EntityManager entityManager = plugin.getEntityManager();

        double xOffset = JavaUtils.randomDouble(-1, 1);
        double zOffset = JavaUtils.randomDouble(-1, 1);
        loc = loc.clone().add(xOffset, 0, zOffset);

        String damageText = JavaUtils.formatDouble(damageAmount);
        entityManager.spawnHologram(loc, 20, UnicodeSpaceUtil.getNeg(8 * damageText.length()) + prefix + damageText);

    }

    private void damageEntity(Entity damager, Entity damagee, double damage) {
        Damageable entity = (Damageable) damagee;
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        String id = pdc.get(NamespacedKey.minecraft("me-ent-id"), PersistentDataType.STRING);

        if (id == null)
            return;

        double health = pdc.get(NamespacedKey.minecraft("me-health"), PersistentDataType.DOUBLE);
        health -= damage;
        pdc.set(NamespacedKey.minecraft("me-health"), PersistentDataType.DOUBLE, health);

        CustomEntity ent = plugin.getEntityManager().getHandlerByEntity(damagee);
        if (ent != null)
            ent.onHit(damager);

        if (health <= 0) {
            entity.setHealth(0);
            CustomEntityKillByPlayerEvent ev = new CustomEntityKillByPlayerEvent((Player)damager, ent);
            Bukkit.getPluginManager().callEvent(ev);
        }
    }

    //physical hits
    @EventHandler(priority = EventPriority.HIGHEST)
    public void listen(EntityDamageByEntityEvent event) {
        boolean isProjectile = false;
        if(event.isCancelled())
            return;

        event.setDamage(0);

        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if(damager instanceof Projectile projectile){
            damager = (Entity) projectile.getShooter();
            isProjectile = true;
        }

        boolean damagerIsPlayer = (damager instanceof Player);
        boolean damageeIsPlayer = (damagee instanceof Player);

        EntityManager entityManager = plugin.getEntityManager();

        if(damagerIsPlayer){

            ItemStack item = ((Player)damager).getInventory().getItemInMainHand();
            if( item.getType() != Material.AIR) {
                CustomItemManager customItemManager = plugin.getCustomItemManager();
                if (!isProjectile) {
                    CustomItem itemHandler = customItemManager.getRegistry().getHandler(item);
                    if (event.getCause() != EntityDamageEvent.DamageCause.CUSTOM)
                        if (!(itemHandler instanceof MeleeWeapon)) {
                            event.setCancelled(true);
                            return;
                        }
                }

                if (!customItemManager.canAttack((Player) damager) && !isProjectile) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if(damagerIsPlayer && damageeIsPlayer){
            Player damagerPlayer = (Player)damager;
            Player damageePlayer = (Player)damagee;

            handleDamageP2P(damagerPlayer, damageePlayer);
        }else if(damagerIsPlayer){
            Player damagerPlayer = (Player)damager;
            CustomEntity damageeEntity = entityManager.getHandlerByEntity(damagee);

            if(damageeEntity == null) {
                damagee.remove();
                return;
            }

            handleDamageP2E(damagerPlayer, damageeEntity, 1);

        }else if(damageeIsPlayer){
            CustomEntity damagerEntity = entityManager.getHandlerByEntity(damager);
            Player damageePlayer = (Player)damagee;

            if(damagerEntity == null) {
                damager.remove();
                return;
            }

            handleDamageE2P(damagerEntity, damageePlayer);
        }
    }
}









































