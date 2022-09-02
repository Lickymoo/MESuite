package com.buoobuoo.mesuite.meareas.data;

import com.buoobuoo.mesuite.meareas.Area;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProfileAreaData {

    private UUID ownerID;
    private UUID profileID;
    private String lastAreaName;
    private transient Area currentArea;
}
