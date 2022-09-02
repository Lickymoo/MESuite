package com.buoobuoo.mesuite.meplayerdata.stats;

import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.QueryItemModifiersEvent;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerStatInstance extends EntityStatInstance {

    public PlayerStatInstance(ProfileData profileData){
        //DEFAULTS
        maxHealth = ProfileData.BASE_HEALTH;
        maxMana = ProfileData.BASE_MANA;

        manaRegenPS = ProfileData.BASE_MANA_REGEN_PER_SECOND;
        healthRegenPS = ProfileData.BASE_HEALTH_REGEN_PER_SECOND;

        walkSpeed = ProfileData.BASE_WALK_SPEED;

        Player player = Bukkit.getPlayer(profileData.getOwnerID());
        QueryItemModifiersEvent event = new QueryItemModifiersEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);

        damageDealt = Math.max(1, damageDealt);
    }
}
