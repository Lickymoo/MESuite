package com.buoobuoo.mesuite.meutils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Colour {
    public static String format(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static List<String> format(Collection<String> str){
        List<String> out = new ArrayList<>();
        for(String s : str){
            out.add(format(s));
        }
        return out;
    }

}
