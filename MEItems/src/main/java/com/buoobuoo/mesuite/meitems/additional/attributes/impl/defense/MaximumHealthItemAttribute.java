package com.buoobuoo.mesuite.meitems.additional.attributes.impl.defense;


import com.buoobuoo.mesuite.meitems.additional.attributes.ItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;

public class MaximumHealthItemAttribute extends ItemAttribute {

    public MaximumHealthItemAttribute(double minRoll, double maxRoll) {
        super("Maximum Health", "MAXIMUM_HEALTH", minRoll, maxRoll);
        this.setRoundValue(true);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance) {
        return "&r&f" + instance.getValueFormat() + " &7Increased Maximum Health";
    }

    @Override
    public void onCalc(EntityStatInstance statInstance, ItemAttributeInstance instance) {
        statInstance.increaseMaxHealth(instance.getValue());
    }
}
