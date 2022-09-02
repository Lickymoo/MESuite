package com.buoobuoo.mesuite.meitems.additional.attributes;

import com.buoobuoo.mesuite.meutils.JavaUtils;
import com.buoobuoo.mesuite.meutils.model.Pair;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;

@Getter
@Setter
public abstract class ItemAttribute implements Listener {
    protected final String name;
    protected final String id;
    protected final Pair<Double, Double> minMaxRoll;

    protected boolean roundValue = false;

    public ItemAttribute(String name, String id, double minRoll, double maxRoll){
        this.name = name;
        this.id = id;
        this.minMaxRoll = Pair.of(minRoll, maxRoll);
    }

    public abstract String itemLore(ItemAttributeInstance instance);

    public double roll(){
        double val = JavaUtils.randomDouble(minMaxRoll.getLeft(), minMaxRoll.getRight());
        if(roundValue)
            val = Math.round(val);
        return  val;
    }

    public abstract void onCalc(EntityStatInstance statInstance, ItemAttributeInstance instance);
}
