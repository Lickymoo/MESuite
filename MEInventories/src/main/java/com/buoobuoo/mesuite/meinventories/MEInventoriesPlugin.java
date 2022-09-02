package com.buoobuoo.mesuite.meinventories;

import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import lombok.Getter;

@Getter
public class MEInventoriesPlugin extends MEPlugin {
    private CustomInventoryManager inventoryManager;

    @Override
    public void initDependencies() {

    }

    @Override
    public void initManagers() {
        this.inventoryManager = new CustomInventoryManager(this);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initTimers() {

    }
}
