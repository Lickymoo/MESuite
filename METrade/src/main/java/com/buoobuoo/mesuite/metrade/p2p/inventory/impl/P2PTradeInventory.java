package com.buoobuoo.mesuite.metrade.p2p.inventory.impl;

import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.metrade.METradePlugin;
import com.buoobuoo.mesuite.metrade.packet.trade.p2p.P2PTradeCancelPacket;
import com.buoobuoo.mesuite.metrade.packet.trade.p2p.P2PTradeUpdateAcceptStatusPacket;
import com.buoobuoo.mesuite.metrade.packet.trade.p2p.P2PTradeUpdateOfferPacket;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.google.common.collect.Lists;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Setter
public class P2PTradeInventory extends CustomInventory {
    private final METradePlugin plugin;
    private final UUID otherPlayer;
    private boolean ignoreClose;

    private static final int[] trade1Slots = {0, 1, 2, 3,   9, 10, 11, 12,  18, 19, 20, 21,  27, 28, 29, 30,  36, 37, 38, 39};
    private static final int[] trade2Slots = {5, 6, 7, 8,  14, 15, 16 ,17,  23, 24, 25, 26,  32, 33, 34, 35,  40, 41, 42, 43};

    //53, 45
    //4, 13, 22, 31, 40, 49

    public P2PTradeInventory(METradePlugin plugin, Player player, UUID otherPlayer) {
        super(plugin.getInventoryManager(), player, "Trade", 54);
        this.plugin = plugin;
        this.cancelBottomClick = true;
        this.otherPlayer = otherPlayer;

        this.addDefaultHandler(event -> {
            int slot = event.getSlot();

            if(Arrays.stream(trade1Slots).allMatch(i -> i != slot)) {
                return;
            }


            ItemStack item = event.getCurrentItem();

            if(item == null)
                return;

            List<ItemStack> currentOffer = new ArrayList<>();
            if(plugin.getP2PTradeManager().getPlayerOffers().get(player.getUniqueId()) != null)
                currentOffer.addAll(Arrays.asList(plugin.getP2PTradeManager().getPlayerOffers().get(player.getUniqueId())));

            currentOffer.remove(item);
            player.getInventory().addItem(item);


            P2PTradeUpdateOfferPacket updateOfferPacket = new P2PTradeUpdateOfferPacket(player.getUniqueId(), currentOffer.toArray(new ItemStack[currentOffer.size()]));
            plugin.getMeLinker().getPacketManager().sendPacket(updateOfferPacket);

            setAllAcceptFalse();
        });

        this.addHandler(event -> {
            boolean accepted = !plugin.getP2PTradeManager().getPlayerConfirmMap().get(player.getUniqueId());

            P2PTradeUpdateAcceptStatusPacket acceptStatusPacket = new P2PTradeUpdateAcceptStatusPacket(player.getUniqueId(), accepted);
            plugin.getMeLinker().getPacketManager().sendPacket(acceptStatusPacket);
        }, 45);

        this.addHandler(event -> {}, 53);
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

        boolean playerAcceptStatus = plugin.getP2PTradeManager().getPlayerConfirmMap().get(player.getUniqueId());
        ItemStack playerAccepted = settingToggleItem("Click to accept", playerAcceptStatus);
        inv.setItem(45, playerAccepted);

        boolean otherPlayerAcceptedStatus = plugin.getP2PTradeManager().getPlayerConfirmMap().get(otherPlayer);
        ItemStack otherPlayerAccepted = settingToggleItem("Other player accept status", otherPlayerAcceptedStatus);
        inv.setItem(53, otherPlayerAccepted);

        ItemStack[] playerOffer = plugin.getP2PTradeManager().getPlayerOffers().get(player.getUniqueId());
        if(playerOffer != null){
            for(int i = 0; i < playerOffer.length; i++){
                inv.setItem(trade1Slots[i], playerOffer[i]);
            }
        }

        ItemStack[] otherPlayerOffer = plugin.getP2PTradeManager().getPlayerOffers().get(otherPlayer);
        if(otherPlayerOffer != null){
            for(int i = 0; i < otherPlayerOffer.length; i++){
                inv.setItem(trade2Slots[i], otherPlayerOffer[i]);
            }
        }


        return inv;
    }

    @Override
    public void onBottomClick(InventoryClickEvent event){
        ItemStack item = event.getCurrentItem();
        if(item == null) return;

        List<ItemStack> currentOffer = new ArrayList<>();
        if(plugin.getP2PTradeManager().getPlayerOffers().get(player.getUniqueId()) != null)
            currentOffer.addAll(Arrays.asList(plugin.getP2PTradeManager().getPlayerOffers().get(player.getUniqueId())));

        if(currentOffer.size() == trade1Slots.length)
            return;

        currentOffer.add(item);
        //TODO
        player.getInventory().setItem(event.getSlot(), null);

        P2PTradeUpdateOfferPacket updateOfferPacket = new P2PTradeUpdateOfferPacket(player.getUniqueId(), currentOffer.toArray(new ItemStack[currentOffer.size()]));
        plugin.getMeLinker().getPacketManager().sendPacket(updateOfferPacket);

        setAllAcceptFalse();
    }

    @Override
    public void onClose(InventoryCloseEvent event){
        if (ignoreClose)
            return;

        P2PTradeCancelPacket tradeCancelPacket = new P2PTradeCancelPacket(player.getUniqueId());
        plugin.getMeLinker().getPacketManager().sendPacket(tradeCancelPacket);
    }

    private ItemStack settingToggleItem(String name, boolean value){
        return new ItemBuilder(value ? Material.GREEN_WOOL : Material.RED_WOOL)
                .name(name)
                .lore(value ? "&aAccepted" : "&cNot Accepted Yet")
                .create();
    }

    private void setAllAcceptFalse(){

        P2PTradeUpdateAcceptStatusPacket acceptStatusPacket = new P2PTradeUpdateAcceptStatusPacket(player.getUniqueId(), false);
        plugin.getMeLinker().getPacketManager().sendPacket(acceptStatusPacket);


        P2PTradeUpdateAcceptStatusPacket acceptOtherStatusPacket = new P2PTradeUpdateAcceptStatusPacket(otherPlayer, false);
        plugin.getMeLinker().getPacketManager().sendPacket(acceptOtherStatusPacket);
    }
}
