package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.ccutils.utils.TimeUtils;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.config.ConfigUtils;
import com.andrew121410.mc.world16essentials.config.MessagesUtils;
import com.andrew121410.mc.world16essentials.objects.AfkObject;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class API {

    // Testing

    private final String dateOfBuild = com.andrew121410.mc.world16essentials.utils.BlossomOutput.DATE_OF_BUILD;

    private final World16Essentials plugin;

    // Configuration Utils
    private final ConfigUtils configUtils;
    private final MessagesUtils messagesUtils;

    private final Map<UUID, Long> timeOfLoginMap;
    private final Map<UUID, AfkObject> afkMap;

    private final List<UUID> godList;
    private final List<UUID> hiddenPlayers;

    public API(World16Essentials plugin) {
        this.plugin = plugin;

        // Configuration Utils
        this.configUtils = this.plugin.getCustomConfigManager().getConfigUtils();
        this.messagesUtils = this.plugin.getCustomConfigManager().getMessagesUtils();

        this.timeOfLoginMap = this.plugin.getMemoryHolder().getTimeOfLoginMap();
        this.afkMap = this.plugin.getMemoryHolder().getAfkMap();

        this.godList = this.plugin.getMemoryHolder().getGodList();
        this.hiddenPlayers = this.plugin.getMemoryHolder().getHiddenPlayers();
    }

    public boolean isAfk(Player player) {
        return afkMap.get(player.getUniqueId()).isAfk();
    }

    public boolean isGod(Player player) {
        return godList.contains(player.getUniqueId());
    }

    public boolean isHidden(Player player) {
        return hiddenPlayers.contains(player.getUniqueId());
    }

    public String getTimeSinceLogin(Player player) {
        long loginTime = timeOfLoginMap.get(player.getUniqueId());
        return TimeUtils.makeIntoEnglishWords(loginTime, System.currentTimeMillis(), false, true);
    }

    public String getTimeSinceLastLogin(OfflinePlayer offlinePlayer) {
        long lastLogin = offlinePlayer.getLastPlayed();
        return TimeUtils.makeIntoEnglishWords(lastLogin, System.currentTimeMillis(), false, true);
    }

    public String getTimeSinceFirstLogin(OfflinePlayer offlinePlayer) {
        long firstPlayed = offlinePlayer.getFirstPlayed();
        return TimeUtils.makeIntoEnglishWords(firstPlayed, System.currentTimeMillis(), false, true);
    }

    public boolean didPlayerJustJoin(Player player) {
        long loginTime = timeOfLoginMap.get(player.getUniqueId());
        long timeElapsed = System.currentTimeMillis() - loginTime;
        long minutes = (timeElapsed % (1000 * 60 * 60)) / (1000 * 60);
        return minutes < 1;
    }

    public Location getLocationFromFile(CustomYmlManager customYmlManager, String path) {
        if (customYmlManager == null || path == null) return null;
        return (Location) customYmlManager.getConfig().get(path);
    }

    public void setLocationToFile(CustomYmlManager customYmlManager, String path, Location location) {
        if (customYmlManager == null || path == null || location == null) {
            return;
        }
        customYmlManager.getConfig().set(path, location);
        customYmlManager.saveConfig();
    }

    public ConfigurationSection getPlayersYML(Player player) {
        ConfigurationSection configurationSection = this.plugin.getCustomConfigManager().getPlayersYml().getConfig().getConfigurationSection("UUID." + player.getUniqueId());
        if (configurationSection == null)
            configurationSection = this.plugin.getCustomConfigManager().getPlayersYml().getConfig().createSection("UUID." + player.getUniqueId());
        return configurationSection;
    }

    public boolean doAfk(Player player, String color, boolean announce) {
        if (color == null) {
            color = "<gray>";
            if (player.isOp()) color = "<dark_red>";
        }

        AfkObject afkObject = this.afkMap.get(player.getUniqueId());
        if (afkObject.isAfk()) {
            if (announce) {
//                this.plugin.getServer().broadcastMessage(Translate.color("&7*" + color + " " + player.getDisplayName() + "&r&7 is no longer AFK."));
                this.plugin.getServer().broadcastMessage(Translate.miniMessage("<gray>* " + color + player.getName() + " <reset><gray>is no longer AFK."));
            }
            afkObject.restart(player);
            return false;
        } else {
            if (announce) {
//                this.plugin.getServer().broadcastMessage(Translate.color("&7* " + color + player.getDisplayName() + "&r&7" + " is now AFK."));
                this.plugin.getServer().broadcastMessage(Translate.miniMessage("<gray>* " + color + player.getName() + " <reset><gray>is now AFK."));
            }
            afkObject.setAfk(true, player.getLocation());
            return true;
        }
    }

    public void saveFlyingState(Player player) {
        // Don't save flying state if player is in creative or spectator.
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        // Don't save if player isn't flying.
        if (!player.isFlying()) return;

        ConfigurationSection configurationSection = getPlayersYML(player);
        configurationSection.set("Flying", true);
        this.plugin.getCustomConfigManager().getPlayersYml().saveConfig();
    }

    public void loadFlyingState(Player player) {
        ConfigurationSection configurationSection = getPlayersYML(player);
        if (configurationSection.get("Flying") == null) return;
        boolean fly = configurationSection.getBoolean("Flying");
        player.setAllowFlight(fly);
        player.setFlying(fly);

        configurationSection.set("Flying", null);
        this.plugin.getCustomConfigManager().getPlayersYml().saveConfig();
        player.sendMessage(Translate.color("&bYour flying state has been restored."));
    }

    public void sendPermissionErrorMessage(CommandSender sender) {
        sender.sendMessage(Translate.color("&4You do not have permission to do this command."));
    }

    public String parseMessageString(Player player, String message) {
        return this.getMessagesUtils().parseMessageString(player, message);
    }

    public String parseMessage(Player player, String message) {
        return this.getMessagesUtils().parseMessage(player, message);
    }

    public ConfigUtils getConfigUtils() {
        return configUtils;
    }

    public MessagesUtils getMessagesUtils() {
        return messagesUtils;
    }

    public String getDateOfBuild() {
        return dateOfBuild;
    }
}