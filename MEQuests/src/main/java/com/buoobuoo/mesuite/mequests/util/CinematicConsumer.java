package com.buoobuoo.mesuite.mequests.util;

import com.buoobuoo.mesuite.mevfx.cinematic.CinematicSequence;
import org.bukkit.entity.Player;

public interface CinematicConsumer {
    CinematicSequence accept(Player player);
}
