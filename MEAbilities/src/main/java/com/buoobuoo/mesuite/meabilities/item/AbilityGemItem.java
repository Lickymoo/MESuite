package com.buoobuoo.mesuite.meabilities.item;

import com.buoobuoo.mesuite.meabilities.Ability;
import com.buoobuoo.mesuite.meitems.CustomItem;
import com.buoobuoo.mesuite.meitems.MEItemsPlugin;
import com.buoobuoo.mesuite.meitems.interf.NotStackable;
import com.buoobuoo.mesuite.meutils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Getter
@Setter
public class AbilityGemItem extends CustomItem implements NotStackable {
    private Ability ability;

    public AbilityGemItem(Ability ability) {
        super("ABILITY_GEM", Material.PAPER, "", "");
        this.ability = ability;
        this.lore = ability == null ? null : ability.getLore();
        this.displayName = ability == null ? null : ability.getName();
    }

    @Override
    public void onCreate(MEItemsPlugin plugin, ItemBuilder ib){
        ib.material(ability.getType().getMat().getMat());
        ib.setCustomModelData(ability.getType().getMat().getCustomModelData());
        ib.name(ability.getName());
        ib.lore(ability.getLore());
        ib.nbtString("me-ability-id", ability.getId());
    }
}
