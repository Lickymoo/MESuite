package com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack;

import com.buoobuoo.mesuite.meitems.additional.attributes.ItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;

public class CriticalMultiplierItemAttribute extends ItemAttribute {
    public CriticalMultiplierItemAttribute(double minRoll, double maxRoll) {
        super("Critical Strike Multi", "CRITICAL_STRIKE_MULTI", minRoll, maxRoll);
        this.setRoundValue(true);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance) {
        return "&r&f" + instance.getValueFormat() + "% &7Increased critical strike multiplier";
    }

    @Override
    public void onCalc(EntityStatInstance statInstance, ItemAttributeInstance instance) {
        statInstance.increaseCritStrikeMulti(instance.getValue());
    }
}
