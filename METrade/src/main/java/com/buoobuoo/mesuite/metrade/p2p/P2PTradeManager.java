package com.buoobuoo.mesuite.metrade.p2p;

import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketEvent;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.metrade.METradePlugin;
import com.buoobuoo.mesuite.metrade.p2p.inventory.impl.P2PTradeInventory;
import com.buoobuoo.mesuite.metrade.packet.trade.p2p.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class P2PTradeManager implements Listener {

    private METradePlugin plugin;

    private final Map<UUID, ItemStack[]> playerOffers = new HashMap<>();
    private final Map<UUID, Boolean> playerConfirmMap = new HashMap<>();
    private final Map<UUID, UUID> activeTrades = new HashMap<>();


    public P2PTradeManager(METradePlugin plugin){
        this.plugin = plugin;
    }

    public void createTradeInstance(UUID player1, UUID player2){
        activeTrades.put(player1, player2);
        activeTrades.put(player2, player1);

        playerConfirmMap.put(player1, false);
        playerConfirmMap.put(player2, false);

        Player bPlayer1 = Bukkit.getPlayer(player1);
        Player bPlayer2 = Bukkit.getPlayer(player2);

        if(bPlayer1 != null){
            Inventory inv = new P2PTradeInventory(plugin, bPlayer1, player2).getInventory();
            bPlayer1.openInventory(inv);
        }

        if(bPlayer2 != null){
            Inventory inv = new P2PTradeInventory(plugin, bPlayer2, player1).getInventory();
            bPlayer2.openInventory(inv);
        }
    }

    public void finaliseTrade(UUID player1){
        if(!activeTrades.containsKey(player1))
            return;

        UUID player2 = activeTrades.get(player1);
        Player bPlayer1 = Bukkit.getPlayer(player1);
        Player bPlayer2 = Bukkit.getPlayer(player2);


        if(bPlayer1 != null){
            if(playerOffers.get(player2) != null)
                bPlayer1.getInventory().addItem(playerOffers.get(player2));

            bPlayer1.closeInventory();
        }

        if(bPlayer2 != null){
            if(playerOffers.get(player1) != null)
                bPlayer2.getInventory().addItem(playerOffers.get(player1));

            bPlayer2.closeInventory();
        }

        playerOffers.remove(player1);
        playerOffers.remove(player2);

        activeTrades.remove(player1);
        activeTrades.remove(player2);

        playerConfirmMap.remove(player1);
        playerConfirmMap.remove(player2);

        return;
    }

    public void cancelTrade(UUID player1){
        if(!activeTrades.containsKey(player1))
            return;

        UUID player2 = activeTrades.get(player1);
        Player bPlayer1 = Bukkit.getPlayer(player1);
        Player bPlayer2 = Bukkit.getPlayer(player2);


        if(bPlayer1 != null){
            if(playerOffers.get(player1) != null)
                bPlayer1.getInventory().addItem(playerOffers.get(player1));

            bPlayer1.sendMessage("Trade cancelled");
            bPlayer1.closeInventory();
        }

        if(bPlayer2 != null){
            if(playerOffers.get(player2) != null)
                bPlayer2.getInventory().addItem(playerOffers.get(player2));

            bPlayer2.sendMessage("Trade cancelled");
            bPlayer2.closeInventory();
        }

        playerOffers.remove(player1);
        playerOffers.remove(player2);

        activeTrades.remove(player1);
        activeTrades.remove(player2);

        playerConfirmMap.remove(player1);
        playerConfirmMap.remove(player2);

        return;
    }

    public void setTradeOffer(UUID player, ItemStack[] offer){
        if(!activeTrades.containsKey(player))
            return;

        playerOffers.put(player, offer);
    }

    @EventHandler
    public void onOfferUpdate(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof P2PTradeUpdateOfferPacket tradeUpdateOfferPacket){
            setTradeOffer(tradeUpdateOfferPacket.getUuid(), tradeUpdateOfferPacket.getItems());


            UUID player1 = tradeUpdateOfferPacket.getUuid();
            UUID player2 = activeTrades.get(tradeUpdateOfferPacket.getUuid());

            Player bPlayer1 = Bukkit.getPlayer(player1);
            Player bPlayer2 = Bukkit.getPlayer(player2);

            if(bPlayer1 != null){
                ignoreCancel(bPlayer1);
                Inventory inv = new P2PTradeInventory(plugin, bPlayer1, player2).getInventory();
                bPlayer1.openInventory(inv);
            }

            if(bPlayer2 != null){
                ignoreCancel(bPlayer2);
                Inventory inv = new P2PTradeInventory(plugin, bPlayer2, player1).getInventory();
                bPlayer2.openInventory(inv);
            }
        }
    }

    @EventHandler
    public void onCancelTrade(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof P2PTradeCancelPacket tradeUpdateOfferPacket){
            cancelTrade(tradeUpdateOfferPacket.getUuid());
        }
    }

    @EventHandler
    public void onTradeInitiate(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof P2PTradeInitiatePacket initiatePacket){
            createTradeInstance(initiatePacket.getPlayer1(), initiatePacket.getPlayer2());
        }
    }

    @EventHandler
    public void onToggleAcceptStatus(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof P2PTradeUpdateAcceptStatusPacket statusPacket){
            playerConfirmMap.put(statusPacket.getUuid(), statusPacket.isStatus());


            UUID player1 = statusPacket.getUuid();
            UUID player2 = activeTrades.get(statusPacket.getUuid());

            Player bPlayer1 = Bukkit.getPlayer(player1);
            Player bPlayer2 = Bukkit.getPlayer(player2);

            if(bPlayer1 != null){
                ignoreCancel(bPlayer1);
                Inventory inv = new P2PTradeInventory(plugin, bPlayer1, player2).getInventory();
                bPlayer1.openInventory(inv);
            }

            if(bPlayer2 != null){
                ignoreCancel(bPlayer2);
                Inventory inv = new P2PTradeInventory(plugin, bPlayer2, player1).getInventory();
                bPlayer2.openInventory(inv);
            }

            if(playerConfirmMap.get(statusPacket.getUuid()) && playerConfirmMap.get(activeTrades.get(statusPacket.getUuid()))){
                P2PTradeFinialisePacket finialisePacket = new P2PTradeFinialisePacket(statusPacket.getUuid());
                plugin.getMeLinker().getPacketManager().sendPacket(finialisePacket);
            }
        }
    }

    @EventHandler
    public void onFinalise(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof P2PTradeFinialisePacket tradeFinialisePacket){
            finaliseTrade(tradeFinialisePacket.getUuid());
        }
    }

    private void ignoreCancel(Player player){
        Inventory openInventory = player.getOpenInventory().getTopInventory();
        if(openInventory == null)
            return;

        CustomInventory handler = plugin.getInventoryManager().getHandler(openInventory);
        if(handler instanceof P2PTradeInventory tradeInventory){
            tradeInventory.setIgnoreClose(true);
        }
    }

}



























