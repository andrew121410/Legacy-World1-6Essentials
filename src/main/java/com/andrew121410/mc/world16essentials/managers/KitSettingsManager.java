package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.ccutils.utils.TimeUtils;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.KitObject;
import com.andrew121410.mc.world16essentials.objects.KitSettingsObject;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class KitSettingsManager {

    private final World16Essentials plugin;
    private final CustomYmlManager kitSettingsYml;

    public KitSettingsManager(World16Essentials plugin) {
        this.plugin = plugin;
        this.kitSettingsYml = this.plugin.getCustomConfigManager().getKitSettingsYml();
    }

    public KitSettingsObject generateSettings(String kitName) {
        KitSettingsObject kitSettings = new KitSettingsObject("none", "world16.kit." + kitName, false);
        this.kitSettingsYml.getConfig().set("kits." + kitName, kitSettings);
        this.kitSettingsYml.saveConfig();
        return kitSettings;
    }

    public KitSettingsObject getFromConfig(String kitName) {
        return (KitSettingsObject) this.kitSettingsYml.getConfig().get("kits." + kitName);
    }

    public void deleteFromConfig(String kitName) {
        this.kitSettingsYml.getConfig().set("kits." + kitName, null);
        this.kitSettingsYml.saveConfig();
    }

    public void setLastUsed(Player player, KitObject kitObject) {
        this.plugin.getApi().getPlayersYML(player).set("kits." + kitObject.getKitName() + ".lastUsed", System.currentTimeMillis());
        this.plugin.getCustomConfigManager().getPlayersYml().saveConfig();
    }

    public Long getLastUsed(Player player, KitObject kitObject) {
        return this.plugin.getApi().getPlayersYML(player).getLong("kits." + kitObject.getKitName() + ".lastUsed");
    }

    // m = minutes, h = hours, d = days, none = no cooldown, one = one time use.
    public boolean handleCooldown(Player player, KitObject kitObject) {
        String cooldown = kitObject.getSettings().getCooldown();
        String numbers = cooldown.replaceAll("[^0-9]", "");
        String letters = cooldown.replaceAll("[^a-zA-Z]", "");

        Long lastUsed = this.getLastUsed(player, kitObject);
        if (lastUsed == null) return true;

        if (letters.equalsIgnoreCase("none")) {
            return true;
        } else if (letters.equalsIgnoreCase("m")) {
            long now = System.currentTimeMillis();
            long difference = now - lastUsed;
            long minutes = difference / 1000 / 60;
            return minutes >= Integer.parseInt(numbers);
        } else if (letters.equalsIgnoreCase("h")) {
            long now = System.currentTimeMillis();
            long difference = now - lastUsed;
            long hours = difference / 1000 / 60 / 60;
            return hours >= Integer.parseInt(numbers);
        } else if (letters.equalsIgnoreCase("d")) {
            long now = System.currentTimeMillis();
            long difference = now - lastUsed;
            long days = difference / 1000 / 60 / 60 / 24;
            return days >= Integer.parseInt(numbers);
        }
        return false;
    }

    public String getTimeUntilCanUseAgain(Player player, KitObject kitObject) {
        String cooldown = kitObject.getSettings().getCooldown();
        String numbers = cooldown.replaceAll("[^0-9]", "");
        String letters = cooldown.replaceAll("[^a-zA-Z]", "");

        Long lastUsed = this.getLastUsed(player, kitObject);
        if (lastUsed == null) return "0";

        // m = minutes, h = hours, d = days, none = no cooldown, one = one time use.
        // Get the time when the player can use the kit again in milliseconds.
        long timeWhenCanUseAgain = 0;
        if (letters.equalsIgnoreCase("none")) {
            return "0";
        } else if (letters.equalsIgnoreCase("m")) {
            timeWhenCanUseAgain = lastUsed + TimeUnit.MINUTES.toMillis(Integer.parseInt(numbers));
        } else if (letters.equalsIgnoreCase("h")) {
            timeWhenCanUseAgain = lastUsed + TimeUnit.HOURS.toMillis(Integer.parseInt(numbers));
        } else if (letters.equalsIgnoreCase("d")) {
            timeWhenCanUseAgain = lastUsed + TimeUnit.DAYS.toMillis(Integer.parseInt(numbers));
        }

        return TimeUtils.makeIntoEnglishWords(System.currentTimeMillis(), timeWhenCanUseAgain, true, true);
    }
}

