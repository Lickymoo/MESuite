package com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack;

import com.buoobuoo.mesuite.meitems.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.mesuite.meitems.additional.attributes.VariableItemAttribute;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;

import java.text.DecimalFormat;

public class BaseRangedItemAttribute extends VariableItemAttribute {
    public BaseRangedItemAttribute(double value) {
        super("Ranged Damage", "BASE_RANGED_DAMAGE", value);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance){
        return "&r&f" + new DecimalFormat("#").format(value) + " &7Base Ranged Damage";
    }

    @Override
    public void onCalc(EntityStatInstance statInstance, ItemAttributeInstance instance) {
        statInstance.increaseDamageDealt(instance.getValue());
    }
}
