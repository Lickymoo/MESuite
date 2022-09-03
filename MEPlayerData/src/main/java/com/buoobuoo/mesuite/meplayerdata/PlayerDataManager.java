package com.buoobuoo.mesuite.meplayerdata;

import com.buoobuoo.mesuite.mecore.gamehandler.event.UpdateTickEvent;
import com.buoobuoo.mesuite.mecore.persistence.MongoHook;
import com.buoobuoo.mesuite.melinker.gamehandler.event.MEPacketEvent;
import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.*;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.display.ActionbarQueryEvent;
import com.buoobuoo.mesuite.meplayerdata.gamehandler.event.display.PrefixQueryEvent;
import com.buoobuoo.mesuite.meplayerdata.model.PlayerData;
import com.buoobuoo.mesuite.meplayerdata.model.ProfileData;
import com.buoobuoo.mesuite.meplayerdata.packet.ResumePlayerDataPacket;
import com.buoobuoo.mesuite.meplayerdata.packet.SuspendPlayerDataPacket;
import com.buoobuoo.mesuite.meplayerdata.stats.PlayerStatInstance;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.JavaUtils;
import com.buoobuoo.mesuite.meutils.MEUtils;
import com.buoobuoo.mesuite.meutils.stats.EntityStatInstance;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class PlayerDataManager implements Listener {
    private final MEPlayerDataPlugin plugin;
    private final MongoHook mongoHook;

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private final Map<UUID, ProfileData> profileDataMap = new HashMap<>();

    public PlayerDataManager(MEPlayerDataPlugin plugin){
        this.plugin = plugin;
        this.mongoHook = plugin.getMongoHook();
    }

    //player
    public PlayerData getData(Player player){
        return getData(player.getUniqueId());
    }

    public PlayerData getData(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, key -> {
            if (mongoHook.valueExists("_id", uuid.toString(), "playerData")) {
                return mongoHook.loadObject(uuid.toString(), PlayerData.class, "playerData");
            }else{
                plugin.getLogger().info("Creating new player data for player " + uuid);
                PlayerData playerData = new PlayerData();
                playerData.setOwnerID(uuid);
                return playerData;
            }
        });
    }

    /**
     * Save player data to database
     * @param playerData
     */
    public void saveData(PlayerData playerData){
        UUID activeProfile = playerData.getActiveProfileID();
        if(activeProfile != null)
            saveProfile(getProfile(activeProfile));

        PlayerDataSaveEvent event = new PlayerDataSaveEvent(playerData);
        Bukkit.getPluginManager().callEvent(event);

        mongoHook.saveObject(playerData.getOwnerID().toString(), playerData, "playerData");
    }

    /**
     * Removes player data & subsequent active profile from local cache
     * @param playerData
     */
    public void removeData(PlayerData playerData){
        playerDataMap.remove(playerData.getOwnerID());

        UUID activeProfile = playerData.getActiveProfileID();
        if(activeProfile != null)
            removeProfile(getProfile(activeProfile));

        PlayerDataRemoveEvent event = new PlayerDataRemoveEvent(playerData);
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Set player's active profile to selected profile, creating a new instance if necessary
     * @param playerData
     * @param uuid
     */
    public void setProfile(PlayerData playerData, UUID uuid){
        if(playerData.getActiveProfileID() != null){
            ProfileData activeProfile = getProfile(playerData.getActiveProfileID());
            saveProfile(activeProfile);
            removeProfile(activeProfile);
            playerData.setActiveProfileID(null);
        }

        Player player = playerData.getPlayer();
        player.getInventory().clear();
        player.setHealth(20);
        player.setPlayerWeather(WeatherType.CLEAR);
        player.getActivePotionEffects().forEach(e -> player.removePotionEffect(e.getType()));

        if(uuid == null){
            return;
        }
        if(playerData.getProfileIDs().contains(uuid)){
            saveData(playerData);
            ProfileData profileData = getProfile(uuid);
            playerData.setActiveProfileID(uuid);
            profileData.applyPlayer();
        }else{
            ProfileData profileData = getProfile(uuid);
            profileData.setProfileID(uuid);
            playerData.getProfileIDs().add(uuid);
            playerData.setActiveProfileID(uuid);
            profileData.applyPlayer();
        }
    }

    //profile
    public ProfileData getProfile(Player player){
        if(player == null || !player.isOnline()){
            return null;
        }

        PlayerData data = getData(player);
        if(data.getActiveProfileID() == null)
            return null;
        return getProfile(data.getActiveProfileID());
    }

    public ProfileData getProfile(UUID uuid){
        if(profileDataMap.containsKey(uuid)){
            return profileDataMap.get(uuid);
        }else{
            ProfileData data = mongoHook.loadObject(uuid.toString(), ProfileData.class, "profileData");
            profileDataMap.put(uuid, data);

            ProfileDataLoadEvent event = new ProfileDataLoadEvent(data);
            Bukkit.getPluginManager().callEvent(event);
            return data;
        }
    }

    public ProfileData createNewProfile(PlayerData data){
        UUID uuid = UUID.randomUUID();

        plugin.getLogger().info("Creating new profile data for profile " + uuid);
        ProfileData profileData = new ProfileData();
        profileData.setOwnerID(data.getOwnerID());
        profileData.setProfileID(uuid);
        profileData.setProfileName(data.getPlayer().getName() + "'s Profile");

        data.getProfileIDs().add(uuid);

        saveProfile(profileData);
        return profileData;
    }

    public void deleteProfile(ProfileData data){
        PlayerData playerData = getData(data.getOwnerID());
        playerData.getProfileIDs().remove(data.getProfileID());
        saveData(playerData);
    }

    /**
     * Save profile to database
     * @param profileData
     */
    public void saveProfile(ProfileData profileData){
        profileData.preSave();

        ProfileDataSaveEvent event = new ProfileDataSaveEvent(profileData);
        Bukkit.getPluginManager().callEvent(event);

        mongoHook.saveObject(profileData.getProfileID().toString(), profileData, "profileData");
    }

    /**
     * Remove profile data from local cache
     * @param profileData
     */
    public void removeProfile(ProfileData profileData){
        profileDataMap.remove(profileData.getProfileID());

        ProfileDataRemoveEvent event = new ProfileDataRemoveEvent(profileData);
        Bukkit.getPluginManager().callEvent(event);
    }

    public boolean isProfileLoaded(UUID uuid){
        return profileDataMap.containsKey(uuid);
    }

    public boolean hasActive(Player player){
        PlayerData data = getData(player);
        return data.getActiveProfileID() != null;
    }

    //event handler save/load
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        PlayerData data = getData(event.getPlayer());
        setProfile(data, null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        PlayerData data = getData(event.getPlayer());
        //suspend data while saving
        SuspendPlayerDataPacket packet = new SuspendPlayerDataPacket(event.getPlayer().getUniqueId());
        plugin.getMeLinker().getPacketManager().sendPacket(packet);

        saveData(data);
        saveProfile(getProfile(data.getActiveProfileID()));
        removeData(data);
        removeProfile(getProfile(data.getActiveProfileID()));

        ResumePlayerDataPacket resumePacket = new ResumePlayerDataPacket(event.getPlayer().getUniqueId());
        plugin.getMeLinker().getPacketManager().sendPacket(resumePacket);
    }

    private final Set<UUID> suspendedPlayers = new HashSet<>();
    @EventHandler
    public void onPacket(MEPacketEvent event){
        MEPacket packet = event.getPacket();

        if(packet instanceof SuspendPlayerDataPacket suspendPlayerData){
            suspendedPlayers.add(suspendPlayerData.getPlayer());
            System.out.println("SUSPENDED DATA FOR " + suspendPlayerData.getPlayer());
            return;
        }

        if(packet instanceof ResumePlayerDataPacket resumePlayerData){
            suspendedPlayers.remove(resumePlayerData.getPlayer());
            System.out.println("RESUMED DATA FOR " + resumePlayerData.getPlayer());
            return;
        }
    }


    private String statusBar = "";

    @EventHandler
    public void updateTick(UpdateTickEvent event){
        for(PlayerData data : playerDataMap.values()){
            Player player = Bukkit.getPlayer(data.getOwnerID());
            if(player == null)
                continue;

            if(data.getActiveProfileID() == null) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
                continue;
            }

            if(player.getGameMode() == GameMode.SPECTATOR)
                continue;

            setDisplayPrefix(data);
            ProfileData profileData = getProfile(data.getActiveProfileID());
            if(profileData.getStatInstance() == null)
                profileData.setStatInstance(new PlayerStatInstance(profileData));


            //Status effect bar
            statusBar = UnicodeSpaceUtil.getNeg(1);
            String temp = "";

            checkHealth(player, profileData);
            checkMana(player, profileData);
            checkLevel(player, profileData);
            EntityStatInstance statInstance = profileData.getStatInstance();

            ActionbarQueryEvent queryEvent = new ActionbarQueryEvent(player, temp);
            Bukkit.getPluginManager().callEvent(queryEvent);
            temp = queryEvent.getActionbar();

            {//Mana & health bar
            /*
            each bar is 81 pixels
             */
                String manaBar = "";
                String healthBar = "";
                if (!data.isSetting_gui_sliders()) {
                    //ICON HEALTH BAR AND MANA BAR
                    manaBar = MEUtils.getBarIcons(statInstance.getMaxMana(), profileData.getMana(), CharRepo.MANA_BALL_FULL, CharRepo.MANA_BALL_EMPTY, CharRepo.MANA_BALL_HALF, true);
                    healthBar = MEUtils.getBarIcons(statInstance.getMaxHealth(), profileData.getHealth(), CharRepo.HEART_FULL, CharRepo.HEART_EMPTY, CharRepo.HEART_HALF, false);
                } else {
                    //SLIDER HEALTH BAR AND MANA BAR;
                    //idk why the mana bar needes spacing
                    manaBar = UnicodeSpaceUtil.getPos(2) + MEUtils.getBarSlider(statInstance.getMaxMana(), profileData.getMana(), CharRepo.SLIDER_BAR_EMPTY, CharRepo.SLIDER_MANA_1, CharRepo.SLIDER_MANA_2, CharRepo.SLIDER_MANA_GENERIC, true) + UnicodeSpaceUtil.getPos(2);
                    healthBar = MEUtils.getBarSlider(statInstance.getMaxHealth(), profileData.getHealth(), CharRepo.SLIDER_BAR_EMPTY, CharRepo.SLIDER_HEALTH_1, CharRepo.SLIDER_HEALTH_2, CharRepo.SLIDER_HEALTH_GENERIC, false);

                }

                temp += healthBar;
                temp += UnicodeSpaceUtil.getPos(19);
                temp += manaBar;

                //reset to left
                temp += UnicodeSpaceUtil.getNeg(182);
            }

            /*
            int maxStatusEffectIcons = 20;
            int iconsIndex = 0;
            for(StatusEffect effect : profileData.getStatusEffects()){
                temp += effect.getIcon();
                temp += UnicodeSpaceUtil.getNeg(1);

                iconsIndex++;
            }
            temp += UnicodeSpaceUtil.getPos(1 + (9 * (maxStatusEffectIcons - iconsIndex)));

            //reset to center
            temp += UnicodeSpaceUtil.getNeg(91);
             */



            /*
            String comboStr = plugin.getAbilityManager().getComboString(player);
            comboStr = comboStr == null ? " " : comboStr;
            int comboLength = CharRepo.getPixelWidth(comboStr);
            temp += UnicodeSpaceUtil.getNeg(comboLength/2);
            temp += comboStr;
            */

            //calculate offset
            int left = CharRepo.getPixelWidth(temp);
            statusBar += UnicodeSpaceUtil.getSpace(left - 182);
            statusBar += temp;





            player.setWalkSpeed(Math.max(0, (float) profileData.getStatInstance().getWalkSpeed()));

            if(player.getGameMode() == GameMode.SURVIVAL)
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(statusBar));

            player.setFoodLevel(20);
        }
    }

    private void checkLevel(Player player, ProfileData profileData){
        player.setLevel(profileData.getLevel());

        int level = profileData.getLevel();
        int requiredExp = 25 * level * (1 + level);
        int currentExp = profileData.getExperience();

        if(currentExp >= requiredExp){
            profileData.setLevel(level+1);
            profileData.setExperience(currentExp-requiredExp);

            PlayerLevelUpEvent levelUpEvent = new PlayerLevelUpEvent(player);
            Bukkit.getPluginManager().callEvent(levelUpEvent);
            /*
            ParticleDirectory.LEVELUP.playEffect(plugin, player.getLocation(), 1, 2.5, 3);

            EntityManager entityManager = plugin.getEntityManager();
            List<TagEntity> entityList = entityManager.spawnHologram(player.getLocation().clone().add(0, 1.5, 0), 40, CharRepo.TAG_LEVEL_UP.toString());

            new BukkitRunnable() {
                @Override
                public void run() {
                    for(TagEntity ent : entityList){
                        if(ent == null)
                            this.cancel();

                        ArmorStand as = (ArmorStand) ent.asEntity().getBukkitEntity();
                        Location loc = as.getLocation();
                        loc.add(0, .05, 0);
                        as.teleport(loc);
                    }
                }

            }.runTaskTimer(plugin, 0, 1);
            */
        }

        float progress = JavaUtils.clamp((float)currentExp / (float)requiredExp, 0, 1);
        player.setExp(progress);


    }

    private void checkMana(Player player, ProfileData profileData) {

        double mana = profileData.getMana();

        double maxMana = profileData.getStatInstance().getMaxMana();

        if(mana > maxMana){
            mana = maxMana;
            profileData.setMana(mana);
        }
    }

    private void checkHealth(Player player, ProfileData profileData) {
        double health = profileData.getHealth();

        double maxHealth = profileData.getStatInstance().getMaxHealth();

        //DEATH
        if (health <= 0) {
            player.setHealth(0);
            profileData.setHealth(maxHealth);
            profileData.setMana(ProfileData.BASE_MANA);
            return;
        }
        if(health > maxHealth){
            health = maxHealth;
            profileData.setHealth(maxHealth);
        }

        if (!player.isDead()) {
            player.setHealth((health / maxHealth) * 20);
        }

    }

    private void setDisplayPrefix(PlayerData playerData) {
        Player player = Bukkit.getPlayer(playerData.getOwnerID());
        PrefixQueryEvent queryEvent = new PrefixQueryEvent(player, "");
        Bukkit.getPluginManager().callEvent(queryEvent);

        if(playerData.getActiveProfileID() == null)
            return;

        ProfileData profileData = getProfile(player);
        try {
            Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
            Team team = scoreboard.getTeam(player.getName());


            if(team == null)
                team = scoreboard.registerNewTeam(player.getName());

            String prefix = queryEvent.getPrefix();
            team.setPrefix(Colour.format(prefix));
            team.addEntry(player.getName());

            Objective objective = null;

            if(scoreboard.getObjective("showhealth") == null)
                objective = scoreboard.registerNewObjective("showhealth", "dummy", "");
            else
                objective = scoreboard.getObjective("showhealth");

            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName(CharRepo.HEART.toString());
            objective.getScore(player.getName()).setScore((int)profileData.getHealth());


        }catch(Exception e) {
            e.printStackTrace();
        }

    }
}
