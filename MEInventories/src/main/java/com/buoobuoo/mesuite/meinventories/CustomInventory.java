package com.buoobuoo.mesuite.meinventories;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public class CustomInventory implements Listener, InventoryHolder {
    protected final CustomInventoryManager manager;
    protected final Player player;
    protected Map<Integer, Consumer<InventoryClickEvent>> slotMap = new HashMap<>();

    protected String title;
    protected int size;
    protected boolean cancelBottomClick = true;

    public CustomInventory(CustomInventoryManager manager, Player player, String title, int size){
        this.manager = manager;
        this.title = title;
        this.size = size;
        this.player = player;

        manager.registerInventory(this);
        addDefaultHandler(null);
    }

    public void addHandler(Consumer<InventoryClickEvent> event, int... slots){
        for(int i : slots){
            slotMap.put(i, event);
        }
    }

    public void addDefaultHandler(Consumer<InventoryClickEvent> event){
        for(int i = 0; i < size; i++){
            slotMap.put(i, event);
        }
    }

    @Override
    public Inventory getInventory() {
        return Bukkit.createInventory(this, size, title);
    }

    public boolean isApplicable(Inventory inventory){
        return inventory.getHolder() == this;
    }

    public void onClose(InventoryCloseEvent event){
    }

    public void onBottomClick(InventoryClickEvent event){
    }

    public void openInventory(){
        player.openInventory(getInventory());
    }
}
