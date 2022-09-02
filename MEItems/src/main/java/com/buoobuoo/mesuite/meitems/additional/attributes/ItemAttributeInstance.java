package com.buoobuoo.mesuite.meitems.additional.attributes;

import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;
import lombok.Getter;

import java.text.DecimalFormat;

@Getter
public class ItemAttributeInstance {
    protected final ItemAttribute attribute;
    protected double value;

    public ItemAttributeInstance(ItemAttribute attribute){
        this.attribute = attribute;
        this.value = attribute.roll();
    }

    public ItemAttributeInstance(ItemAttribute attribute, double value){
        this.attribute = attribute;
        this.value = value;
    }

    public String getAttributeString(){
        return attribute.getId() + ":" + value;
    }

    public String getValueFormat(){
        return new DecimalFormat("#").format(value);
    }

    public void onCalc(EntityStatInstance instance){
        attribute.onCalc(instance, this);
    }
}
