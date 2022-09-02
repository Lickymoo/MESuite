package com.buoobuoo.mesuite.mesocial.friend;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class PlayerFriendData {
    private List<UUID> friendList = new ArrayList<>();
    private List<UUID> friendRequests = new ArrayList<>();
}
