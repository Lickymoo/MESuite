package com.buoobuoo.mesuite.meitems.interf;

import com.buoobuoo.mesuite.meitems.CustomItem;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.additional.attributes.ItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.mesuite.meitems.additional.requirements.ItemRequirement;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import com.buoobuoo.mesuite.meutils.JavaUtils;
import com.buoobuoo.mesuite.meutils.MatRepo;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AttributedItem extends CustomItem {

    public List<ItemAttribute> attributes = new ArrayList<>();
    public List<ItemRequirement> requirements = new ArrayList<>();

    public AttributedItem(String id, Material material, String displayName, String... lore) {
        super(id, material, displayName, lore);
    }

    public AttributedItem(String id, Material material, int customModelData, String displayName, String... lore) {
        super(id, material, customModelData, displayName, lore);
    }

    public AttributedItem(String id, MatRepo mat, String displayName, String... lore) {
        super(id, mat, displayName, lore);
    }

    public void addAttributes(ItemAttribute... attributes){
        this.attributes.addAll(List.of(attributes));
    }

    public void addRequirements(ItemRequirement... requirements){
        this.requirements.addAll(List.of(requirements));
    }

    @Override
    public void onCreate(MEItemsPlugin plugin, ItemBuilder ib){
        super.onCreate(plugin, ib);
        List<String> attribValues = new ArrayList<>();
        for(ItemAttribute attribute : attributes){
            ItemAttributeInstance inst = new ItemAttributeInstance(attribute);
            attribValues.add(inst.getAttributeString());
            ib.lore(4, attribute.itemLore(inst));
        }
        ib.nbtString(plugin, "me-attrib-list", JavaUtils.fromList(attribValues));


        List<String> reqValues = new ArrayList<>();
        for(ItemRequirement requirement : requirements){
            reqValues.add(requirement.getId());
            ib.lore(2, requirement.itemLore(null));
        }
        ib.lore(3, "");

        ib.nbtString(plugin, "me-req-list", JavaUtils.fromList(reqValues));
    }

    @Override
    public void update(MEItemsPlugin plugin, ProfileData profileData, ItemBuilder ib){
        super.update(plugin, profileData, ib);
        List<ItemAttributeInstance> attributeInstances = plugin.getItemAttributeManager().getAttribInstances(ib.getItemStackRaw());

        for(ItemAttributeInstance attribute : attributeInstances){
            ib.lore(4, attribute.getAttribute().itemLore(attribute));
        }
        for(ItemRequirement requirement : requirements){
            ib.lore(2, requirement.itemLore(profileData));
        }
        ib.lore(3, "");
    }

    public void onCalc(MEItemsPlugin plugin, ItemStack item, EntityStatInstance instance){
        List<ItemAttributeInstance> attributeInstanceList = plugin.getItemAttributeManager().getAttribInstances(item);
        for(ItemAttributeInstance attributeInstance : attributeInstanceList){
            attributeInstance.onCalc(instance);
        }
    }

    public ItemAttribute getItemAttribByID(String id){
        for(ItemAttribute attribute : attributes){
            if(attribute.getId().equalsIgnoreCase(id))
                return attribute;
        }
        return null;
    }


    public ItemRequirement getItemReqByID(String id){
        for(ItemRequirement requirement : requirements){
            if(requirement.getId().equalsIgnoreCase(id))
                return requirement;
        }
        return null;
    }


    public boolean meetsRequirement(ProfileData profileData){
        for(ItemRequirement requirement : requirements){
            if(!requirement.meetsRequirement(profileData))
                return false;
        }
        return true;
    }
}

































