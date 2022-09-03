package com.buoobuoo.mesuite.meabilities.serializer;

import com.buoobuoo.mesuite.meabilities.AbilityCastType;
import com.buoobuoo.mesuite.mecore.persistence.serialization.VariableSerializer;

public class AbilityCastTypeSerializer extends VariableSerializer<AbilityCastType> {

    @Override
    public String serialize(AbilityCastType obj) {
        if(obj == null)
            return "";
        return obj.name();
    }

    @Override
    public AbilityCastType deserialize(String str) {
        try {
            return AbilityCastType.valueOf(str);
        } catch (Exception ex) {
            return AbilityCastType.NONE;
        }
    }
}
