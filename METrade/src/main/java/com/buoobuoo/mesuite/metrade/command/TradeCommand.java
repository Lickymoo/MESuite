package com.buoobuoo.mesuite.metrade.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import com.buoobuoo.mesuite.melinker.util.NetworkedPlayer;
import com.buoobuoo.mesuite.metrade.METradePlugin;
import com.buoobuoo.mesuite.metrade.p2p.P2PTradeManager;
import org.bukkit.entity.Player;

@CommandAlias("trade")
public class TradeCommand extends BaseCommand {

    private final METradePlugin plugin;

    public TradeCommand(METradePlugin plugin){
        this.plugin = plugin;
    }

    @Default
    @CommandCompletion("@networked-players-name")
    public void p2p(Player player, NetworkedPlayer target){
        P2PTradeManager p2PTradeManager = plugin.getP2PTradeManager();

    }
}
