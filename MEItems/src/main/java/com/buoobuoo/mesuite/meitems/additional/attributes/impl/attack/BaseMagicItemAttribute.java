package com.buoobuoo.mesuite.meitems.additional.attributes.impl.attack;

import com.buoobuoo.mesuite.meitems.additional.attributes.ItemAttributeInstance;
import com.buoobuoo.mesuite.meitems.additional.attributes.VariableItemAttribute;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;

import java.text.DecimalFormat;

public class BaseMagicItemAttribute extends VariableItemAttribute {
    public BaseMagicItemAttribute(double value) {
        super("Magic Damage", "BASE_MAGIC_DAMAGE", value);
    }

    @Override
    public String itemLore(ItemAttributeInstance instance){
        return "&r&f" + new DecimalFormat("#").format(value) + " &7Base Magic Damage";
    }

    @Override
    public void onCalc(EntityStatInstance statInstance, ItemAttributeInstance instance) {
        statInstance.increaseDamageDealt(instance.getValue());
    }
}
