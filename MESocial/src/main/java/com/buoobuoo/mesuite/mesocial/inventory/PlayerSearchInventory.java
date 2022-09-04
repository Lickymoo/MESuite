package com.buoobuoo.mesuite.mesocial.inventory;

import com.buoobuoo.mesuite.mecore.gamehandler.event.AnvilRenameEvent;
import com.buoobuoo.mesuite.meinventories.CustomInventory;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.mesocial.MESocialPlugin;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerSearchInventory extends CustomInventory {
    private final Consumer<NetworkedPlayer> consumer;
    private final MESocialPlugin plugin;
    private Collection<NetworkedPlayer> playerList;

    public PlayerSearchInventory(MESocialPlugin plugin, Player player, Consumer<NetworkedPlayer> consumer){
        this(plugin, player, plugin.getMeCorePlugin().getNetworkPlayerData(), consumer);
    }

    public PlayerSearchInventory(MESocialPlugin plugin, Player player, Collection<NetworkedPlayer> playerSet, Consumer<NetworkedPlayer> consumer){
        super(plugin.getInventoryManager(), player, UnicodeSpaceUtil.getNeg(60)
                + "&r&f" + CharRepo.UI_INVENTORY_PLAYER_SEARCH_OVERLAY
                + UnicodeSpaceUtil.getNeg(117)
                +"&8Enter player name", 27);

        this.plugin = plugin;
        this.consumer = consumer;
        this.playerList = playerSet;

        slotMap.put(2, event -> {
            if(event.getInventory().getItem(2) == null){
                return;
            }
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1 ,1);

            ItemStack item = event.getInventory().getItem(2);
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            String playerUUID = pdc.get(NamespacedKey.minecraft("player-sel"), PersistentDataType.STRING);
            UUID uuid = UUID.fromString(playerUUID);

            consumer.accept(plugin.getMeCorePlugin().getNetworkedPlayer(uuid));
            playerList = null; //clear mem

        });
    }

    private NetworkedPlayer getPlayer(String name){
        for(NetworkedPlayer networkedPlayer : plugin.getMeCorePlugin().getNetworkPlayerData()) {
            if (networkedPlayer.getName().equalsIgnoreCase(name))
                return networkedPlayer;
        }
        return null;
    }

    @Override
    public Inventory getInventory() {
        ItemStack item = new ItemBuilder(Material.PLAYER_HEAD).setCustomModelData(10)
                .skullTexture(
                        "eyJ0aW1lc3RhbXAiOjE1ODc4MjU0NzgwNDcsInByb2ZpbGVJZCI6ImUzYjQ0NWM4NDdmNTQ4ZmI4YzhmYTNmMWY3ZWZiYThlIiwicHJvZmlsZU5hbWUiOiJNaW5pRGlnZ2VyVGVzdCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E1ODg4YWEyZDdlMTk5MTczYmEzN2NhNzVjNjhkZTdkN2Y4NjJiMzRhMTNiZTMyNDViZTQ0N2UyZjIyYjI3ZSJ9fX0=",
                        "Yt6VmTAUTbpfGQoFneECtoYcbu7jcARAwZu2LYWv3Yf1MJGXv6Bi3i7Pl9P8y1ShB7V1Q2HyA1bce502x1naOKJPzzMJ0jKZfEAKXnzaFop9t9hXzgOq7PaIAM6fsapymYhkkulRIxnJdMrMb2PLRYfo9qiBJG+IEbdj8MTSvWJO10xm7GtpSMmA2Xd0vg5205hsj0OxSdgxf1uuWPyRaXpPZYDUU05/faRixDKti86hlkBs/v0rttU65r1UghkftfjK0sJoPpk9hABvkw4OjXVFb63wcb27KPhIiSHZzTooSxjGNDniauCsF8Je+fhhMebpXeba1R2lZPLhkHwazNgZmTCKbV1M/a8BDHN24HH9okJpQOR9SPCPOJrNbK+LTPsrR06agj+H/yvYq0ZMJTF6IE6C3KJqntPJF1NQvJM0/YegPPtzpbT/7O1cd4JBCVmguhadOFYvrxqCKHcmaYdkyMJtnGub/5sCjJAG7fZadACftwLnmdBZoQRcNKQMubpdUjuzF8g6C03MiZkeNBUgqkfVjXi7DqpmB0ZvTttp34vy7EIBCo3Hfj15779nGs8SoTw9V2zZc+LgiVPjWF6tffjWkgzLq8K2Cndu6RDlWGJWmrztN/X9lIiLdn8GEfSSGY983n0C91x8mkpOKSfAWPnSZd7NuHU5GaoMvyE="
                )
                .name(" ").create();

        Inventory inv = Bukkit.createInventory(this, InventoryType.ANVIL, Colour.format(title));
        inv.setItem(0, item);

        ItemStack invis = new ItemBuilder(MatRepo.INVISIBLE).name(" ").create();
        inv.setItem(1,invis);

        return inv;
    }

    private NetworkedPlayer currentTarget;

    @EventHandler
    public void onRename(AnvilRenameEvent event){
        if(event.getHandler() != this)
            return;

        String name = event.getName().trim();
        NetworkedPlayer target = getPlayer(name);
        Inventory inv = event.getPlayer().getOpenInventory().getTopInventory();

        String curName = inv.getItem(0).getItemMeta().getDisplayName();
        if(curName.equals("Player Head") || name.isBlank()){
            name = " ";
        }
        if(curName.equals(name) && target == null){
            inv.setItem(2, null);
            return;
        }
        if(currentTarget != null && currentTarget == target){
            return;
        }

        currentTarget = target;

        if(target != null) {
            ItemStack head = new ItemBuilder(Material.PLAYER_HEAD).setCustomModelData(10)
                    .skullTexture(target.getSkinValue(), target.getSkinSignature())
                    .name(name).create();
            inv.setItem(0, head);

            ItemStack accept = new ItemBuilder(MatRepo.GREEN_TICK).name(Colour.format("&a&lSelect Player")).create();
            ItemMeta meta = accept.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(NamespacedKey.minecraft("player-sel"), PersistentDataType.STRING, target.getUuid().toString());
            accept.setItemMeta(meta);

            inv.setItem(2,accept);
        }else{
            ItemStack head = new ItemBuilder(Material.PLAYER_HEAD).setCustomModelData(10)
                    .skullTexture(
                            "eyJ0aW1lc3RhbXAiOjE1ODc4MjU0NzgwNDcsInByb2ZpbGVJZCI6ImUzYjQ0NWM4NDdmNTQ4ZmI4YzhmYTNmMWY3ZWZiYThlIiwicHJvZmlsZU5hbWUiOiJNaW5pRGlnZ2VyVGVzdCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E1ODg4YWEyZDdlMTk5MTczYmEzN2NhNzVjNjhkZTdkN2Y4NjJiMzRhMTNiZTMyNDViZTQ0N2UyZjIyYjI3ZSJ9fX0=",
                            "Yt6VmTAUTbpfGQoFneECtoYcbu7jcARAwZu2LYWv3Yf1MJGXv6Bi3i7Pl9P8y1ShB7V1Q2HyA1bce502x1naOKJPzzMJ0jKZfEAKXnzaFop9t9hXzgOq7PaIAM6fsapymYhkkulRIxnJdMrMb2PLRYfo9qiBJG+IEbdj8MTSvWJO10xm7GtpSMmA2Xd0vg5205hsj0OxSdgxf1uuWPyRaXpPZYDUU05/faRixDKti86hlkBs/v0rttU65r1UghkftfjK0sJoPpk9hABvkw4OjXVFb63wcb27KPhIiSHZzTooSxjGNDniauCsF8Je+fhhMebpXeba1R2lZPLhkHwazNgZmTCKbV1M/a8BDHN24HH9okJpQOR9SPCPOJrNbK+LTPsrR06agj+H/yvYq0ZMJTF6IE6C3KJqntPJF1NQvJM0/YegPPtzpbT/7O1cd4JBCVmguhadOFYvrxqCKHcmaYdkyMJtnGub/5sCjJAG7fZadACftwLnmdBZoQRcNKQMubpdUjuzF8g6C03MiZkeNBUgqkfVjXi7DqpmB0ZvTttp34vy7EIBCo3Hfj15779nGs8SoTw9V2zZc+LgiVPjWF6tffjWkgzLq8K2Cndu6RDlWGJWmrztN/X9lIiLdn8GEfSSGY983n0C91x8mkpOKSfAWPnSZd7NuHU5GaoMvyE="
                            )
                    .name(name).create();
            inv.setItem(0, head);

            inv.setItem(2, null);
        }
        player.updateInventory();

    }
}
