package com.buoobuoo.mesuite.meutils.model;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MEPlugin extends JavaPlugin implements Listener{

    @Override
    public void onEnable(){
        initDependencies();
        initManagers();
        initListeners();
        initTimers();
        defineCommands();
        registerEvents(this);
    }

    public abstract void initDependencies();

    public abstract void initManagers();

    public abstract void initListeners();

    public abstract void initTimers();

    public void registerEvents(Listener... listeners){
        for(Listener listener : listeners){
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public void defineCommands(){

    }
}
