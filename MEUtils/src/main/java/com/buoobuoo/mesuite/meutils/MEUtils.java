package com.buoobuoo.mesuite.meutils;

import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public class MEUtils {

    public static Entity getTarget(Player player, float range) {
        List<Entity> nearbyE = player.getNearbyEntities(range,
                range,range);
        ArrayList<LivingEntity> livingE = new ArrayList<>();

        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }

        BlockIterator bItr = new BlockIterator(player, (int)range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
            block = bItr.next();
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (LivingEntity e : livingE) {
                loc = e.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) {
                    // entity is close enough, set target and stop
                    return (Entity) e;
                }
            }
        }
        return null;
    }

    public static boolean isChunkLoaded(Location loc){
        int x = loc.getBlockX() >> 4;
        int z = loc.getBlockZ() >> 4;

        return loc.getWorld().isChunkLoaded(x, z);
    }


    public static String grayScaleString(String input){
        String section = "\u00a7";
        String output = input;
        //dark
        String[] darkReplace = new String[]{"4", "6", "2", "3", "1", "9", "5", "7", "8", "0"};
        String[] lightRepalce = new String[]{"c", "e", "a", "b", "d", "f"};

        for(String r : darkReplace){
            output = output.replace(section + r, section + "8");
        }
        for(String r : lightRepalce){
            output = output.replace(section + r, section + "7");
        }
        return output;
    }

    public static String getBarSlider(double max, double cur, CharRepo empty, CharRepo first, CharRepo second, CharRepo generic, boolean fromLeft){
        double val = Math.floor((cur / max) * 79d);

        double fullBars = val;
        double emptyBars = 79 - val;

        StringBuilder sb = new StringBuilder();
        sb.append(empty);
        sb.append(UnicodeSpaceUtil.getNeg(81));

        int index = 0;
        if(!fromLeft){
            for(int i = 0; i < fullBars; i++){
                if (index == 0 || index == 78) {
                    sb.append(first);
                    sb.append(UnicodeSpaceUtil.getNeg(1));
                } else if (index == 1) {
                    sb.append(second);
                    sb.append(UnicodeSpaceUtil.getNeg(1));
                } else {
                    sb.append(generic);
                    sb.append(UnicodeSpaceUtil.getNeg(1));
                }
                index++;
            }
            for (int i = 0; i < emptyBars; i++) {
                sb.append(UnicodeSpaceUtil.getPos(1));
                index++;
            }
        }else{
            for (int i = 0; i < emptyBars; i++) {
                sb.append(UnicodeSpaceUtil.getPos(1));
                index++;
            }
            for (int i = 0; i < fullBars; i++) {
                if (index == 0 || index == 78) {
                    sb.append(first);
                    sb.append(UnicodeSpaceUtil.getNeg(1));
                } else if (index == 1) {
                    sb.append(second);
                    sb.append(UnicodeSpaceUtil.getNeg(1));
                } else {
                    sb.append(generic);
                    sb.append(UnicodeSpaceUtil.getNeg(1));
                }
                index++;
            }
        }

        return sb.toString();
    }

    public static String getBarIcons(double max, double cur, CharRepo full, CharRepo empty, CharRepo half, boolean fromLeft){
        double val = Math.floor((cur / max) * 20d) / 2;

        double fullBalls = Math.floor(val);
        double halfBalls = Math.ceil(val - Math.floor(val));
        double emptyBalls = 10 - (fullBalls + halfBalls);

        StringBuilder sb = new StringBuilder();
        sb.append(UnicodeSpaceUtil.getPos(2));

        if(fromLeft) {
            sb.append(StringUtils.repeat(UnicodeSpaceUtil.getNeg(2) + empty, (int) emptyBalls));
            sb.append(StringUtils.repeat(UnicodeSpaceUtil.getNeg(2) + half, (int) halfBalls));
            sb.append(StringUtils.repeat(UnicodeSpaceUtil.getNeg(2) + full, (int) fullBalls));
        }else{
            sb.append(StringUtils.repeat(UnicodeSpaceUtil.getNeg(2) + full, (int) fullBalls));
            sb.append(StringUtils.repeat(UnicodeSpaceUtil.getNeg(2) + half, (int) halfBalls));
            sb.append(StringUtils.repeat(UnicodeSpaceUtil.getNeg(2) + empty, (int) emptyBalls));
        }
        return sb.toString();
    }

    public static String getNBTString(ItemStack item, String tag){
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(NamespacedKey.minecraft(tag), PersistentDataType.STRING);
    }


}
