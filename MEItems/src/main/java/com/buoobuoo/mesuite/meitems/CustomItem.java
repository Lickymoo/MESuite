package com.buoobuoo.mesuite.meitems;

import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.MatRepo;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class CustomItem implements Listener {
    protected CustomItemManager manager;

    protected String id;
    protected String displayName;
    protected String[] lore;
    protected Material material;
    protected int customModelData;

    protected ItemRarity rarity = ItemRarity.COMMON;

    public void addManager(CustomItemManager manager){
        this.manager = manager;
    }

    public CustomItem(String id, Material material, String displayName, String... lore){
        this.id = id;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
    }

    public CustomItem(String id, Material material, int customModelData, String displayName, String... lore){
        this.id = id;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.customModelData = customModelData;
    }

    public CustomItem(String id, MatRepo mat, String displayName, String... lore){
        this.id = id;
        this.material = mat.getMat();
        this.displayName = displayName;
        this.lore = lore;
        this.customModelData = mat.getCustomModelData();
    }

    protected boolean isApplicable(ItemStack item){
        return this.manager.getRegistry().isApplicable(this, item);
    }

    public void onCreate(MEItemsPlugin plugin, ItemBuilder ib){
        ib.lore(lore);
        ib.lore(10, "&r&f" + rarity.getIcon().getCh());
    }

    public ItemStack update(MEItemsPlugin plugin, ProfileData profileData, ItemStack item){
        ItemBuilder builder = new ItemBuilder(item);
        update(plugin, profileData, builder);
        return builder.create();
    }

    public void update(MEItemsPlugin plugin, ProfileData profileData, ItemBuilder ib){
        ib.clearLore();
        ib.lore(lore);
        ib.lore(10, "&r&f" + rarity.getIcon().getCh());
    }
}
