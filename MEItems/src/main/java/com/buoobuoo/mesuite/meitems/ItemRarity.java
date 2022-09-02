package com.buoobuoo.mesuite.meitems;

import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import lombok.Getter;

@Getter
public enum ItemRarity {
    COMMON("&f", CharRepo.RARITY_COMMON),
    MAGIC("&9", CharRepo.RARITY_UNCOMMON),
    RARE("&e", CharRepo.RARITY_RARE),
    ULTRA_RARE("&d", CharRepo.RARITY_ULTRA_RARE),
    UNIQUE("&6", null);

    private String color;
    private CharRepo icon;

    ItemRarity(String color, CharRepo icon){
        this.color = color;
        this.icon = icon;
    }
}
