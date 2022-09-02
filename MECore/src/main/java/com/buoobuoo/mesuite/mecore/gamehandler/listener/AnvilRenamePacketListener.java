package com.buoobuoo.mesuite.mecore.gamehandler.listener;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.mecore.gamehandler.event.AnvilRenameEvent;
import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AnvilRenamePacketListener {
    private final MECorePlugin pluginE;

    public AnvilRenamePacketListener(MECorePlugin pluginE){
        this.pluginE = pluginE;
        pluginE.getProtocolManager().addPacketListener(new PacketAdapter(
                pluginE,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.ITEM_NAME
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();

                Inventory inv = player.getOpenInventory().getTopInventory();
                CustomInventory handler = pluginE.getInventoryManager().getHandler(inv);

                PacketContainer packet = event.getPacket();
                String name = packet.getStrings().getValues().get(0);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    AnvilRenameEvent sendEvent = new AnvilRenameEvent(player, handler, name);
                    Bukkit.getServer().getPluginManager().callEvent(sendEvent);
                });
            }
        });
    }
}
