package com.buoobuoo.mesuite.mecore.gamehandler.listener;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.meutils.ByteBufData;
import com.buoobuoo.mesuite.meutils.Colour;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Field;
import java.util.Set;

public class ServerBrandListener implements Listener {

    private final MECorePlugin plugin;
    private static Field playerChannelsField;
    String channel = MECorePlugin.BRAND_CHANNEL;

    public ServerBrandListener(MECorePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (playerChannelsField == null) {
            try {
                playerChannelsField = event.getPlayer().getClass().getDeclaredField("channels");
                playerChannelsField.setAccessible(true);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        try {
            Set<String> channels = (Set<String>) playerChannelsField.get(event.getPlayer());
            channels.add(channel);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        updateBrand(event.getPlayer());
    }

    private void updateBrand(Player player) {
        ByteBuf byteBuf = Unpooled.buffer();
        ByteBufData.writeString(Colour.format("&7Buby is so cool&r"), byteBuf);
        player.sendPluginMessage(plugin, channel, ByteBufData.toArray(byteBuf));
        byteBuf.release();
    }
}
