package com.buoobuoo.mesuite.metrade;

import com.buoobuoo.mesuite.mecore.MECorePlugin;
import com.buoobuoo.mesuite.meinventories.CustomInventoryManager;
import com.buoobuoo.mesuite.meinventories.MEInventoriesPlugin;
import com.buoobuoo.mesuite.melinker.redis.spigot.SpigotMELinker;
import com.buoobuoo.mesuite.metrade.command.TradeCommand;
import com.buoobuoo.mesuite.metrade.p2p.P2PTradeManager;
import com.buoobuoo.mesuite.metrade.packet.trade.p2p.P2PTradeCancelPacket;
import com.buoobuoo.mesuite.metrade.packet.trade.p2p.P2PTradeInitiatePacket;
import com.buoobuoo.mesuite.metrade.packet.trade.p2p.P2PTradeUpdatePacket;
import com.buoobuoo.mesuite.meutils.command.CommandManager;
import com.buoobuoo.mesuite.meutils.model.MEPlugin;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;

@Getter
public class METradePlugin extends MEPlugin {

    //dependencies
    private MECorePlugin meCorePlugin;
    private SpigotMELinker meLinker;
    private CommandManager commandManager;

    private MEInventoriesPlugin meInventoriesPlugin;
    private CustomInventoryManager inventoryManager;

    //local managers
    private P2PTradeManager p2PTradeManager;

    @Override
    public void initDependencies() {
        PluginManager pluginManager = getServer().getPluginManager();
        this.meCorePlugin = (MECorePlugin) pluginManager.getPlugin("MECore");
        this.meLinker = meCorePlugin.getMeLinker();
        this.commandManager = meCorePlugin.getCommandManager();

        this.meInventoriesPlugin = (MEInventoriesPlugin) pluginManager.getPlugin("MEInventories");
        this.inventoryManager = meInventoriesPlugin.getInventoryManager();

        this.meLinker.getPacketManager().registerPacket("P2P_TRADE_UPDATE", P2PTradeUpdatePacket.class);
        this.meLinker.getPacketManager().registerPacket("P2P_TRADE_INITIATE", P2PTradeInitiatePacket.class);
        this.meLinker.getPacketManager().registerPacket("P2P_TRADE_CANCEL", P2PTradeCancelPacket.class);
    }

    @Override
    public void initManagers() {
        this.p2PTradeManager = new P2PTradeManager(this);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initTimers() {

    }

    @Override
    public void defineCommands(){
        commandManager.registerCommand(new TradeCommand(this));
    }
}
