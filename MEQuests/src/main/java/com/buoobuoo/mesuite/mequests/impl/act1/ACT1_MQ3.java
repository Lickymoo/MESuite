package com.buoobuoo.mesuite.mequests.impl.act1;

import com.buoobuoo.mesuite.meareas.impl.aramore.AramoreAbandonedShrineArea;
import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.meentities.impl.util.EmptyEntity;
import com.buoobuoo.mesuite.mequests.MEQuestsPlugin;
import com.buoobuoo.mesuite.mequests.QuestLine;
import com.buoobuoo.mesuite.mequests.gamehandler.event.ACT1_MQ2PickItemEvent;
import com.buoobuoo.mesuite.mevfx.cinematic.CinematicFrame;
import com.buoobuoo.mesuite.mevfx.cinematic.CinematicSequence;
import com.buoobuoo.mesuite.mevfx.cinematic.LocationUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;

public class ACT1_MQ3 extends QuestLine {
    public ACT1_MQ3(MEQuestsPlugin plugin) {
        super(plugin, "Anomaly", "ACT1_MQ3", "");

        whenEnterArea(AramoreAbandonedShrineArea.class);

        cinematic(player -> {
            Location startLoc = new Location(MECorePlugin.getMainWorld(), -43, 118, -103, 100, 40);
            Location endLoc = new Location(MECorePlugin.getMainWorld(), -53, 118, -92, 150, 44);
            EmptyEntity stand = plugin.getSpectatorManager().viewLoc(player, startLoc);
            CinematicFrame[] move1 = LocationUtil.lerp(startLoc, endLoc, 100, stand);
            return new CinematicSequence(plugin.getMevfxPlugin(), true, CinematicSequence.mergeArrays(move1));
        });
        finish();


    }

    @EventHandler
    public void pickItem(ACT1_MQ2PickItemEvent event){
        setDeterminant(event.getPlayer(), "PICKED_ITEM", true);
    }
}
