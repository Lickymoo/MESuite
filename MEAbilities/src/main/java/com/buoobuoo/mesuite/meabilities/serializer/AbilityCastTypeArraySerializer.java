package com.buoobuoo.mesuite.meabilities.serializer;


import com.buoobuoo.mesuite.meabilities.AbilityCastType;
import com.buoobuoo.mesuite.mecore.persistence.serialization.VariableSerializer;
import com.buoobuoo.mesuite.meutils.JavaUtils;

public class AbilityCastTypeArraySerializer extends VariableSerializer<AbilityCastType[]> {

    @Override
    public String serialize(AbilityCastType[] obj) {
        AbilityCastTypeSerializer singularSerializer = new AbilityCastTypeSerializer();
        String[] types = new String[obj.length];

        for(int i = 0; i < obj.length; i++){
            String str = singularSerializer.serialize(obj[i]);
            types[i] = str;
        }
        return JavaUtils.arrToString(types);

    }

    @Override
    public AbilityCastType[] deserialize(String str) {
        AbilityCastTypeSerializer singularSerializer = new AbilityCastTypeSerializer();
        String[] types = JavaUtils.stringToArr(str);

        AbilityCastType[] abs = new AbilityCastType[types.length];

        for(int i = 0; i < types.length; i++){
            abs[i] = singularSerializer.deserialize(types[i]);
        }

        return abs;
    }
}
