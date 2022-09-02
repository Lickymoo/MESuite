package com.buoobuoo.mesuite.metrade.p2p.inventory.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class P2PTradeInstance {
    private final UUID player1;
    private final UUID player2;

    private boolean player1Confirmed = false;
    private boolean player2Confirmed = false;

    private ItemStack[] player1Offer;
    private ItemStack[] player2Offer;

    public P2PTradeInstance(UUID player1, UUID player2){
        this.player1 = player1;
        this.player2 = player2;
    }

    public UUID getAlternatePlayer(UUID player){
        return player1 == player ? player2 : player1;
    }

    public void cancelTrade(){
        Player player1Bukkit = Bukkit.getPlayer(player1);
        Player player2Bukkit = Bukkit.getPlayer(player2);
        if(player1Bukkit != null){
            player1Bukkit.getInventory().addItem(player1Offer);
        }

        if(player2Bukkit != null){
            player2Bukkit.getInventory().addItem(player2Offer);
        }
    }
}
