package com.buoobuoo.mesuite.mesocial.inventory;

import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.melinker.redis.AbsPacketManager;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.PartyDataByPlayerRequest;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.PartyDataRequestPacket;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.melinker.util.PartyData;
import com.buoobuoo.mesuite.mesocial.MESocialPlugin;
import com.buoobuoo.mesuite.mesocial.gamehandler.event.PartyUpdateEvent;
import com.buoobuoo.mesuite.mesocial.party.PartyManager;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartyInventory extends CustomInventory {
    private final PartyData party;
    private final PartyManager partyManager;
    private final MESocialPlugin plugin;

    public PartyInventory(MESocialPlugin plugin, Player player, PartyData party){
        super(plugin.getInventoryManager(), player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + (party.isLeader(player) ? CharRepo.UI_INVENTORY_PARTY_MAIN_LEADER : CharRepo.UI_INVENTORY_PARTY_MAIN_MEMBER) + UnicodeSpaceUtil.getNeg(177) + CharRepo.UI_INVENTORY_PARTY_DECO_1, 36);
        boolean isLeader = party.isLeader(player);
        this.party = party;
        this.partyManager = plugin.getPartyManager();
        this.plugin = plugin;

        if(isLeader) {
            this.addHandler(event -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
                Inventory inv = new PlayerSearchInventory(plugin, player, target -> {
                    Inventory reopen = new PartyInventory(plugin, player, party).getInventory();
                    partyManager.invite(player, target);
                    player.openInventory(reopen);
                }).getInventory();
                player.openInventory(inv);
            }, 27, 28);
        }

        if(isLeader) {
            this.addHandler(event -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
                List<NetworkedPlayer> partyMembers = new ArrayList<>();
                for(UUID uuid : party.getAllIDs()){
                    partyMembers.add(plugin.getMeCorePlugin().getNetworkedPlayer(uuid));
                }

                Inventory inv = new PlayerSearchInventory(plugin, player, partyMembers, target -> {
                    partyManager.promote(player, target);

                    Inventory reopen = new PartyInventory(plugin, player, party).getInventory();
                    player.openInventory(reopen);
                }).getInventory();
                player.openInventory(inv);
            }, 29, 30);
        }

        if(isLeader) {
            this.addHandler(event -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
                List<NetworkedPlayer> partyMembers = new ArrayList<>();
                for(UUID uuid : party.getAllIDs()){
                    partyMembers.add(plugin.getMeCorePlugin().getNetworkedPlayer(uuid));
                }


                Inventory inv = new PlayerSearchInventory(plugin, player, partyMembers, target -> {
                    Inventory reopen = new PartyInventory(plugin, player, party).getInventory();
                    partyManager.kick(player, target);
                    player.openInventory(reopen);
                }).getInventory();
                player.openInventory(inv);
            }, 31, 32);
        }

        if(isLeader) {
            this.addHandler(event -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
                partyManager.disband(player);
                player.closeInventory();
            }, 34, 35);
        }else{
            this.addHandler(event -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
                partyManager.leave(player);
                player.closeInventory();
            }, 34, 35);
        }
    }

    @EventHandler
    public void partyUpdate(PartyUpdateEvent event){
        async(() -> {
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
            PartyData partyData = new PartyDataByPlayerRequest(player.getUniqueId(), null).await(packetManager, 3).getPartyData();

            sync(() -> {
                if(partyData == null){
                    player.closeInventory();
                    return;
                }

                if(!party.getPartyID().equals(partyData.getPartyID()))
                    return;

                new PartyInventory(plugin, player, partyData).openInventory();
            });
        });

    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title));
        boolean isLeader = party.isLeader(player);

        if(isLeader) {
            ItemStack invite = new ItemBuilder(MatRepo.INVISIBLE).name("&7Invite Player").create();
            inv.setItem(27, invite);
            inv.setItem(28, invite);

            ItemStack promote = new ItemBuilder(MatRepo.INVISIBLE).name("&7Promote Player").create();
            inv.setItem(29, promote);
            inv.setItem(30, promote);

            ItemStack kick = new ItemBuilder(MatRepo.INVISIBLE).name("&7Kick Player").create();
            inv.setItem(31, kick);
            inv.setItem(32, kick);

            ItemStack leave = new ItemBuilder(MatRepo.INVISIBLE).name("&cDisband Party").create();
            inv.setItem(34, leave);
            inv.setItem(35, leave);
        }else{
            ItemStack leave = new ItemBuilder(MatRepo.INVISIBLE).name("&7Leave Party").create();
            inv.setItem(34, leave);
            inv.setItem(35, leave);
        }

        int index = 11;

        List<NetworkedPlayer> partyMembers = new ArrayList<>();
        for(UUID uuid : party.getAllIDs()){
            partyMembers.add(plugin.getMeCorePlugin().getNetworkedPlayer(uuid));
        }
        for(NetworkedPlayer networkedPlayer : partyMembers){
            if(networkedPlayer == null)
                continue;

            ItemStack head = new ItemBuilder(Material.PLAYER_HEAD).skullTexture(networkedPlayer.getSkinValue(), networkedPlayer.getSkinSignature()).name(networkedPlayer.getName()).create();
            inv.setItem(index, head);
            index++;
        }

        return inv;
    }

    public void async(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public void sync(Runnable runnable){
        Bukkit.getScheduler().runTask(plugin, runnable);
    }
}
