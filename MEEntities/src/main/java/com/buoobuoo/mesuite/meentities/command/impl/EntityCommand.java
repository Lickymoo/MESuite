package com.buoobuoo.mesuite.meentities.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import com.buoobuoo.mesuite.meentities.EntityManager;
import com.buoobuoo.mesuite.meentities.MEEntitiesPlugin;
import com.buoobuoo.mesuite.meentities.interf.CustomEntity;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@CommandAlias("enh")
public class EntityCommand extends BaseCommand {

    private final MEEntitiesPlugin plugin;

    public EntityCommand(MEEntitiesPlugin plugin){
        this.plugin = plugin;
    }


    @Subcommand("debug")
    public class debug extends BaseCommand{

        @Subcommand("spawn")
        @CommandCompletion("@custom-entities")
        public void spawn(Player player, String entityClass){
            EntityManager entityManager = plugin.getEntityManager();
            Class<? extends CustomEntity> clazz = entityManager.getHandlerClassByName(entityClass);
            CustomEntity ent = entityManager.spawnEntity(clazz, player.getLocation());

            Attributable attr = (Attributable) ent.asEntity().getBukkitEntity();
            attr.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(.3);
        }

        @Subcommand("clear")
        @CommandCompletion("@vanilla-entity-type")
        public void type(Player player, EntityType type){
            for(Entity ent : player.getLocation().getWorld().getEntitiesByClass(type.getEntityClass())){
                ent.remove();
            }
        }
    }
}



