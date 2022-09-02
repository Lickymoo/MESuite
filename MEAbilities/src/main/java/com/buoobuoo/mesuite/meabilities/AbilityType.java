package com.buoobuoo.mesuite.meabilities;

import com.buoobuoo.mesuite.meutils.MatRepo;
import lombok.Getter;

@Getter
public enum AbilityType {
    STRENGTH(MatRepo.STRENGTH_ORB),
    INTELLIGENCE(MatRepo.INTELLIGENCE_ORB),
    DEXTERITY(MatRepo.DEXTERITY_ORB);

    private MatRepo mat;

    AbilityType(MatRepo mat){
        this.mat = mat;
    }
}
