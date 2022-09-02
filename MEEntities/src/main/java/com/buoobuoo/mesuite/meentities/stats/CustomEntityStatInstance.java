package com.buoobuoo.mesuite.meentities.stats;

import com.buoobuoo.mesuite.meentities.MEEntitiesPlugin;
import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomEntityStatInstance extends EntityStatInstance {

    public CustomEntityStatInstance(MEEntitiesPlugin plugin, CustomEntity customEntity){
        //DEFAULTS
        maxHealth = customEntity.maxHealth();

        entity = customEntity.asEntity().getBukkitEntity();

        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        currentHealth = pdc.get(NamespacedKey.minecraft("me-health"), PersistentDataType.DOUBLE);
        damageDealt = pdc.get(NamespacedKey.minecraft("me-damage"), PersistentDataType.DOUBLE);
    }
}
