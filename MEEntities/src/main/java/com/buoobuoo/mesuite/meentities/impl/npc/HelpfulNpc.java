package com.buoobuoo.mesuite.meentities.impl.npc;

import com.buoobuoo.mesuite.meentities.interf.AbstractNpc;
import com.buoobuoo.mesuite.meentities.interf.tags.AdditionalTag;
import com.buoobuoo.mesuite.meentities.interf.tags.HideHealthTag;
import com.buoobuoo.mesuite.meentities.interf.tags.HideNameTag;
import com.buoobuoo.mesuite.meentities.interf.tags.Invulnerable;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import org.bukkit.Location;

public class HelpfulNpc extends AbstractNpc implements Invulnerable, HideNameTag, HideHealthTag, AdditionalTag {

    public HelpfulNpc(Location loc) {
        super(loc);
    }

    @Override
    public String entityID() {
        return "NPC_HELPFUL_NPC";
    }

    @Override
    public String entityName() {
        return "???";
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
        return CharRepo.SPEECH + "";
    }
}
