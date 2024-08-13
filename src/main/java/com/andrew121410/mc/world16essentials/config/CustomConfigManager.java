package com.andrew121410.mc.world16essentials.config;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;

public class CustomConfigManager {

    private final World16Essentials plugin;
    private final ConfigUtils configUtils;

    private final CustomYmlManager shitYml;

    private final CustomYmlManager warpsYml;

    private final CustomYmlManager playersYml;

    private final CustomYmlManager kitSettingsYml;

    private final CustomYmlManager messagesYml;
    private final MessagesUtils messagesUtils;

    public CustomConfigManager(World16Essentials plugin) {
        this.plugin = plugin;
        this.configUtils = new ConfigUtils(plugin);

        //shit.yml
        this.shitYml = new CustomYmlManager(this.plugin, false);
        this.shitYml.setup("shit.yml");
        this.shitYml.saveConfig();
        this.shitYml.reloadConfig();
        //...

        //warps.yml
        this.warpsYml = new CustomYmlManager(this.plugin, false);
        this.warpsYml.setup("warps.yml");
        this.warpsYml.saveConfig();
        this.warpsYml.reloadConfig();
        //...

        //players.yml
        this.playersYml = new CustomYmlManager(this.plugin, false);
        this.playersYml.setup("players.yml");
        this.playersYml.saveConfig();
        this.playersYml.reloadConfig();
        //...

        //messages.yml
        this.messagesYml = new CustomYmlManager(this.plugin, false);
        this.messagesYml.setup("messages.yml");
        this.messagesUtils = new MessagesUtils(this.plugin, this.messagesYml);
        //...

        //kit-settings.yml
        this.kitSettingsYml = new CustomYmlManager(this.plugin, false);
        this.kitSettingsYml.setup("kit-settings.yml");
        this.kitSettingsYml.saveConfig();
        this.kitSettingsYml.reloadConfig();
        //...
    }

    public void saveAll() {
        this.plugin.saveConfig();
        this.shitYml.saveConfig();
        this.warpsYml.saveConfig();
        this.playersYml.saveConfig();
        this.messagesYml.saveConfig();
        this.kitSettingsYml.saveConfig();
    }

    public void reloadAll() {
        this.plugin.reloadConfig();
        this.shitYml.reloadConfig();
        this.warpsYml.reloadConfig();
        this.playersYml.reloadConfig();
        this.messagesYml.reloadConfig();
        this.kitSettingsYml.reloadConfig();
    }

    public ConfigUtils getConfigUtils() {
        return configUtils;
    }

    public CustomYmlManager getShitYml() {
        return shitYml;
    }

    public CustomYmlManager getWarpsYml() {
        return warpsYml;
    }

    public CustomYmlManager getPlayersYml() {
        return playersYml;
    }

    public CustomYmlManager getMessagesYml() {
        return messagesYml;
    }

    public MessagesUtils getMessagesUtils() {
        return messagesUtils;
    }

    public CustomYmlManager getKitSettingsYml() {
        return kitSettingsYml;
    }
}
