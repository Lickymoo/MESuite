package com.buoobuoo.mesuite.mecore.gamehandler.listener.virtualplayer;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.mecore.gamehandler.event.PlayerArmorEquipEvent;
import com.buoobuoo.mesuite.melinker.redis.AbsPacketManager;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer.PlayerEquipmentPacket;
import com.buoobuoo.mesuite.meutils.PacketUtils;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class PlayerEquipmentEventListener implements Listener {

    private final MECorePlugin plugin;

    public PlayerEquipmentEventListener(MECorePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
        delay(() -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offhand = player.getInventory().getItemInOffHand();
            PlayerEquipmentPacket mainHandPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.MAINHAND.getName(), mainHand);
            PlayerEquipmentPacket offHandPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.OFFHAND.getName(), offhand);

            packetManager.sendPacket(mainHandPacket);
            packetManager.sendPacket(offHandPacket);
        });
    }

    @EventHandler
    public void onHotbar(PlayerItemHeldEvent event){
        AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
        delay(() -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offhand = player.getInventory().getItemInOffHand();
            PlayerEquipmentPacket mainHandPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.MAINHAND.getName(), mainHand);
            PlayerEquipmentPacket offHandPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.OFFHAND.getName(), offhand);

            packetManager.sendPacket(mainHandPacket);
            packetManager.sendPacket(offHandPacket);
        });
    }

    @EventHandler
    public void onArmorEquip(PlayerArmorEquipEvent event){
        AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
        delay(() -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            switch (event.getType()){
                case HELMET -> {
                    ItemStack helmet = player.getInventory().getHelmet();
                    PlayerEquipmentPacket equipmentPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.HEAD.getName(), helmet);
                    packetManager.sendPacket(equipmentPacket);
                }
                case CHESTPLATE -> {
                    ItemStack chestPlate = player.getInventory().getChestplate();
                    PlayerEquipmentPacket equipmentPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.CHEST.getName(), chestPlate);
                    packetManager.sendPacket(equipmentPacket);
                }
                case LEGGINGS -> {
                    ItemStack leggings = player.getInventory().getLeggings();
                    PlayerEquipmentPacket equipmentPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.LEGS.getName(), leggings);
                    packetManager.sendPacket(equipmentPacket);
                }
                case BOOTS -> {
                    ItemStack boots = player.getInventory().getBoots();
                    PlayerEquipmentPacket equipmentPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.FEET.getName(), boots);
                    packetManager.sendPacket(equipmentPacket);
                }
            }

        });
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
        delay(() -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offhand = player.getInventory().getItemInOffHand();
            PlayerEquipmentPacket mainHandPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.MAINHAND.getName(), mainHand);
            PlayerEquipmentPacket offHandPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.OFFHAND.getName(), offhand);

            packetManager.sendPacket(mainHandPacket);
            packetManager.sendPacket(offHandPacket);
        });
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent event){
        AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
        delay(() -> {
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();

            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offhand = player.getInventory().getItemInOffHand();
            PlayerEquipmentPacket mainHandPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.MAINHAND.getName(), mainHand);
            PlayerEquipmentPacket offHandPacket = new PlayerEquipmentPacket(uuid, EquipmentSlot.OFFHAND.getName(), offhand);

            packetManager.sendPacket(mainHandPacket);
            packetManager.sendPacket(offHandPacket);
        });
    }


    public void delay(Runnable runnable){
        Bukkit.getScheduler().runTaskLater(plugin, runnable, 1);
    }

}
