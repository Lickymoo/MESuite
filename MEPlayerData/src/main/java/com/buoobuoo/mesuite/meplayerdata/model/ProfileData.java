package com.buoobuoo.mesuite.meplayerdata.model;

import com.buoobuoo.mesuite.mecore.persistence.serialization.DoNotSerialize;
import com.buoobuoo.mesuite.meplayerdata.stats.PlayerStatInstance;
import com.buoobuoo.mesuite.meutils.stats.TemporaryStatModifier;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Setter
public class ProfileData {
    public transient static final double BASE_HEALTH = 20;
    public transient static final double BASE_MANA = 20;

    public transient static final double BASE_MANA_REGEN_PER_SECOND = .5;
    public transient static final double BASE_HEALTH_REGEN_PER_SECOND = .5;

    public transient static final double BASE_WALK_SPEED = 0.2f;

    private UUID profileID;
    private UUID ownerID;

    private Material profileIcon = Material.WOODEN_SWORD;
    private String profileName;

    private int level = 1;
    private int experience = 0;
    private double health;
    private double mana;

    //meta vars
    private ItemStack[] inventoryContents;
    private ItemStack[] armorContents;
    private Location location;
    private int selectedSlot;

    private GameMode gameMode;
    private boolean flying;

    private transient ConcurrentLinkedQueue<TemporaryStatModifier> temporaryStatModifiers = new ConcurrentLinkedQueue<>();
    private transient ConcurrentLinkedQueue<TemporaryStatModifier> onHitStatModifier = new ConcurrentLinkedQueue<>();

    private transient PlayerStatInstance statInstance;

    public void preSave(){
        Player player = Bukkit.getPlayer(ownerID);
        inventoryContents = player.getInventory().getContents();
        armorContents = player.getInventory().getArmorContents();

        location = player.getLocation();

        selectedSlot = player.getInventory().getHeldItemSlot();

        gameMode = player.getGameMode();
        flying = player.isFlying();
    }

    public void applyPlayer(){
        Player player = Bukkit.getPlayer(ownerID);

        player.getInventory().setContents(inventoryContents);
        player.getInventory().setArmorContents(armorContents);

        player.teleport(location);
        player.getInventory().setHeldItemSlot(selectedSlot);
        player.setGameMode(gameMode);
        player.setFlying(flying);

    }


    public Player getPlayer(){
        return Bukkit.getPlayer(ownerID);
    }
}
