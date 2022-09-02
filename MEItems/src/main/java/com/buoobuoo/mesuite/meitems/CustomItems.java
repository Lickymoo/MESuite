package com.buoobuoo.mesuite.meitems;

import com.buoobuoo.mesuite.meitems.impl.BubysHelmetItem;
import com.buoobuoo.mesuite.meitems.impl.quest.act1.ACT1_MQ2_NecklaceItem;
import com.buoobuoo.mesuite.meitems.impl.weapon.IceSwordItem;
import com.buoobuoo.mesuite.meitems.impl.weapon.TestBowItem;
import com.buoobuoo.mesuite.meitems.impl.weapon.starter.StarterAxeItem;
import com.buoobuoo.mesuite.meitems.impl.weapon.starter.StarterBowItem;
import com.buoobuoo.mesuite.meitems.impl.weapon.starter.StarterStaffItem;
import com.buoobuoo.mesuite.meitems.impl.weapon.starter.StarterSwordItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItems{
    private static final Map<String, CustomItem> registry = new HashMap<>();

    static{
        CustomItems.register("STARTER_STAFF", new StarterStaffItem());
        CustomItems.register("STARTER_BOW", new StarterBowItem());
        CustomItems.register("STARTER_SWORD", new StarterSwordItem());
        CustomItems.register("STARTER_AXE", new StarterAxeItem());

        CustomItems.register("TEST_BOW", new TestBowItem());
        CustomItems.register("ICE_SWORD", new IceSwordItem());

        CustomItems.register("BUBYS_HELMET", new BubysHelmetItem());

        CustomItems.register("ACT1_MQ3_NECKLACE", new ACT1_MQ2_NecklaceItem());
    }

    public static CustomItem getHandler(String name){
        return registry.get(name);
    }

    public static void register(String name, CustomItem handler){
        registry.put(name, handler);
    }

    public static CustomItem[] getHandlers(){
        List<CustomItem> items = new ArrayList<>(CustomItems.registry.values());
        return items.toArray(new CustomItem[0]);
    }

    public static List<String> getNames(){
        return new ArrayList<>(registry.keySet());
    }
}































