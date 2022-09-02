package com.buoobuoo.mesuite.mepermissions.data;

import com.buoobuoo.mesuite.mepermissions.PermissionGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlayerPermissionData {

    private UUID ownerID;
    private PermissionGroup group = PermissionGroup.MEMBER;
}
