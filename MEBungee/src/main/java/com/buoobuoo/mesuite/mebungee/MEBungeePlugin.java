package com.buoobuoo.mesuite.mebungee;

import com.buoobuoo.mesuite.mebungee.gamehandler.listener.MovePlayerPacketListener;
import com.buoobuoo.mesuite.mebungee.party.PartyManager;
import com.buoobuoo.mesuite.melinker.redis.bungee.BungeeMELinker;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class MEBungeePlugin extends Plugin implements Listener {
    //local managers
    private BungeeMELinker meLinker;
    private PartyManager partyManager;

    private ServerOrchestrator serverOrchestrator;

    @Override
    public void onEnable() {
        this.meLinker = new BungeeMELinker(this);
        this.partyManager = new PartyManager(this);
        this.serverOrchestrator = new ServerOrchestrator(this);
        initListeners();
    }

    public void initListeners(){
        registerEvents(
                this,
                partyManager,
                serverOrchestrator,

                new MovePlayerPacketListener(this)
        );
    }

    public void registerEvents(Listener... listeners){
        for(Listener listener : listeners){
            getProxy().getPluginManager().registerListener(this, listener);
        }
    }
}
