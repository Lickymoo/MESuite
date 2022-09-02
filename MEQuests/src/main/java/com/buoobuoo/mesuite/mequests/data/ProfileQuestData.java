package com.buoobuoo.mesuite.mequests.data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProfileQuestData {

    private UUID ownerID;
    private UUID profileID;
    private List<String> completedQuest = new ArrayList<>();
    private List<String> activeQuests = new ArrayList<>();
}
