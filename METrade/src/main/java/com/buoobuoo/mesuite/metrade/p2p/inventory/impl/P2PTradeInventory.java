package com.buoobuoo.mesuite.metrade.p2p.inventory.impl;

import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.metrade.METradePlugin;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class P2PTradeInventory extends CustomInventory {
    private final P2PTradeInstance tradeInstance;
    private final METradePlugin plugin;

    //53, 45
    //4, 13, 22, 31, 40, 49

    public P2PTradeInventory(METradePlugin plugin, Player player, P2PTradeInstance tradeInstance) {
        super(plugin.getInventoryManager(), player, "Trade", 54);
        this.plugin = plugin;
        this.tradeInstance = tradeInstance;
        this.cancelBottomClick = true;
    }

    @Override
    public Inventory getInventory(){
        Inventory inv = Bukkit.createInventory(this, size, title);
        ItemStack divider = new ItemBuilder(Material.DIRT).name(" ").create();
        inv.setItem(4, divider);
        inv.setItem(13, divider);
        inv.setItem(22, divider);
        inv.setItem(31, divider);
        inv.setItem(40, divider);
        inv.setItem(49, divider);

        return inv;
    }

    @Override
    public void onBottomClick(InventoryClickEvent event){

    }

    @Override
    public void onClose(InventoryCloseEvent event){
        tradeInstance.cancelTrade();
    }
}
