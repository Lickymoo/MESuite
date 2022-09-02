package com.buoobuoo.mesuite.mepermissions.serializer;


import com.buoobuoo.mesuite.mecore.persistence.serialization.VariableSerializer;
import com.buoobuoo.mesuite.mepermissions.PermissionGroup;

public class PermissionGroupSerializer extends VariableSerializer<PermissionGroup> {
    @Override
    public String serialize(PermissionGroup obj) {
        return (obj).getGroupID();

    }

    @Override
    public PermissionGroup deserialize(String str) {
        return PermissionGroup.getGroup(str);
    }


}