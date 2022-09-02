package com.buoobuoo.mesuite.meabilities.data;

import com.buoobuoo.mesuite.meabilities.AbilityCastType;
import com.buoobuoo.mesuite.mecore.persistence.serialization.DoNotSerialize;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class ProfileAbilityData {

    private UUID ownerID;
    private UUID profileID;
    private String[] abilityIDs = new String[4];
    private AbilityCastType[] abilityCastTypes = new AbilityCastType[4];

    @DoNotSerialize
    private HashMap<String, Integer> cooldowns;
}
