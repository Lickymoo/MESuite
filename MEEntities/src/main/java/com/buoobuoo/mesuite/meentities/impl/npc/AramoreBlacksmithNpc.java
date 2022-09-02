package com.buoobuoo.mesuite.meentities.impl.npc;

import com.buoobuoo.mesuite.meentities.interf.AbstractNpc;
import com.buoobuoo.mesuite.meentities.interf.tags.AdditionalTag;
import com.buoobuoo.mesuite.meentities.interf.tags.HideHealthTag;
import com.buoobuoo.mesuite.meentities.interf.tags.HideNameTag;
import com.buoobuoo.mesuite.meentities.interf.tags.Invulnerable;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import org.bukkit.Location;

public class AramoreBlacksmithNpc extends AbstractNpc implements Invulnerable, HideNameTag, HideHealthTag, AdditionalTag {


    public AramoreBlacksmithNpc(Location loc) {
        super(loc);

        //TODO
        /*
        this.showIf(player -> {
            MEEntitiesPlugin plugin = MEEntitiesPlugin.getInstance();
            PlayerManager playerManager = plugin.getPlayerManager();
            QuestManager questManager = plugin.getQuestManager();
            ProfileData profileData = playerManager.getProfile(player);

            if(profileData == null)
                return false;

            return questManager.hasCompletedQuest(ACT1_MQ1.class, profileData);
        });
        */
    }

    @Override
    public String entityID() {
        return "NPC_ARAMORE_BLACKSMITH";
    }

    @Override
    public String entityName() {
        return "Blacksmith";
    }

    @Override
    public String textureSignature() {
        return "";
    }

    @Override
    public String textureBase64() {
        return "";
    }

    @Override
    public String overrideTag() {
        return "Blacksmith" + " \n " + CharRepo.SPEECH;
    }
}
