package com.buoobuoo.mesuite.meentities.interf;

import com.buoobuoo.mesuite.meentities.gamehandler.event.PlayerInteractNpcEvent;

public interface NpcEntity extends CustomEntity {

    String textureSignature();
    String textureBase64();

    default void onInteract(PlayerInteractNpcEvent event){

    }
}


























