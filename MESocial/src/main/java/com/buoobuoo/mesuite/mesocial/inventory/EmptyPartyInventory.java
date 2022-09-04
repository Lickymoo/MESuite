package com.buoobuoo.mesuite.mesocial.inventory;

import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.melinker.redis.AbsPacketManager;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.PartyDataByInvitePacket;
import com.buoobuoo.mesuite.melinker.redis.packet.impl.party.PartyDataByPlayerRequest;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.melinker.util.PartyData;
import com.buoobuoo.mesuite.mesocial.MESocialPlugin;
import com.buoobuoo.mesuite.mesocial.gamehandler.event.PartyInviteUpdateEvent;
import com.buoobuoo.mesuite.mesocial.party.PartyManager;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class EmptyPartyInventory extends CustomInventory {
    private final PartyManager partyManager;
    public final MESocialPlugin plugin;

    public EmptyPartyInventory(MESocialPlugin plugin, Player player){
        super(plugin.getInventoryManager(), player, UnicodeSpaceUtil.getNeg(8) + "&r&f" + CharRepo.UI_INVENTORY_PARTY_EMPTY + UnicodeSpaceUtil.getNeg(169)
                +"&8Party Invites", 36);
        this.partyManager = plugin.getPartyManager();
        this.plugin = plugin;

        this.addDefaultHandler(event -> {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);
            if(event.getCurrentItem() == null)
                return;

            ItemStack item = event.getCurrentItem();

            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            String playerUUID = pdc.get(NamespacedKey.minecraft("player-sel"), PersistentDataType.STRING);

            if(playerUUID == null)
                return;

            NetworkedPlayer leader = plugin.getNetworkedPlayer(UUID.fromString(playerUUID));

            if(leader != null) {
                partyManager.join(player, leader);
                async(() -> {
                    AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();
                    PartyData partyData = new PartyDataByPlayerRequest(UUID.fromString(playerUUID), null).await(packetManager, 3).getPartyData();

                    sync(() -> {
                        Inventory inv = new PartyInventory(plugin, player, partyData).getInventory();
                        player.openInventory(inv);
                    });
                });
            }
        });

        this.addHandler(event -> {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);

            async(() -> {
                PartyData newParty = partyManager.createPartySync(player);
                if(newParty == null)
                    return;

                sync(() -> {
                    Inventory inv = new PartyInventory(plugin, player, newParty).getInventory();
                    player.openInventory(inv);
                });
            });
        }, 27, 28);
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, Colour.format(title));

        ItemStack create = new ItemBuilder(MatRepo.INVISIBLE).name("&7Create Party").create();
        inv.setItem(27, create);
        inv.setItem(28, create);

        async(() -> {
            AbsPacketManager packetManager = plugin.getMeLinker().getPacketManager();

            int index = 0;
            List<PartyData> parties = new PartyDataByInvitePacket(player.getUniqueId()).await(packetManager, 3).getPartyIds();
            for(PartyData partyData : parties){
                NetworkedPlayer leader = plugin.getNetworkedPlayer(partyData.getPartyLeader());
                ItemStack head = new ItemBuilder(Material.PLAYER_HEAD).skullTexture(leader.getSkinValue(), leader.getSkinSignature()).name(leader.getName() + "'s Party").lore("&7Click to join party").create();

                ItemMeta meta = head.getItemMeta();
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                pdc.set(NamespacedKey.minecraft("player-sel"), PersistentDataType.STRING, leader.getUuid().toString());
                head.setItemMeta(meta);

                inv.setItem(index++, head);
            }
        });
        return inv;
    }

    @EventHandler
    public void partyUpdate(PartyInviteUpdateEvent event){
        if(!player.getUniqueId().equals(event.getPlayerID()))
            return;

        player.openInventory(new EmptyPartyInventory(plugin, player).getInventory());

    }

    public void async(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public void sync(Runnable runnable){
        Bukkit.getScheduler().runTask(plugin, runnable);
    }
}
