package com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack;

import com.buoobuoo.mesuite.meitems.additional.attributes.ItemAttribute;
import com.buoobuoo.mesuite.meitems.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;

public class CriticalStrikeItemAttribute extends ItemAttribute {
    public CriticalStrikeItemAttribute(double minRoll, double maxRoll) {
        super("Critical Strike Chance", "CRITICAL_STRIKE_CHANCE", minRoll, maxRoll);
        this.setRoundValue(true);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance) {
        return "&r&f" + instance.getValueFormat() + "% &7Critical strike chance";
    }

    @Override
    public void onCalc(EntityStatInstance statInstance, ItemAttributeInstance instance) {
        statInstance.increaseCritStrikeChance(instance.getValue());
    }
}
