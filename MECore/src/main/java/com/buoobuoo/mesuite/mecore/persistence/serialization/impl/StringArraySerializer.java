package com.buoobuoo.mesuite.mecore.persistence.serialization.impl;

import com.buoobuoo.mesuite.mecore.persistence.serialization.VariableSerializer;
import com.buoobuoo.mesuite.meutils.JavaUtils;

public class StringArraySerializer extends VariableSerializer<String[]> {
    @Override
    public String serialize(String[] obj) {
        return JavaUtils.arrToString(obj);
    }

    @Override
    public String[] deserialize(String str) {
        return JavaUtils.stringToArr(str);
    }
}
