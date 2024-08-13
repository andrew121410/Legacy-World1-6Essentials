package com.andrew121410.mc.world16utils.chat;

import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageLocale {

    private final Map<String, String> languageMap = new HashMap<>();

    private final JavaPlugin javaPlugin;
    private final CustomYmlManager customYmlManager;
    private final FileConfiguration config;

    public LanguageLocale(JavaPlugin plugin, String languageFile) {
        this.javaPlugin = plugin;

        this.customYmlManager = new CustomYmlManager(plugin);
        this.customYmlManager.setup(new File(plugin.getDataFolder(), "lang"), languageFile);
        this.config = this.customYmlManager.getConfig();
    }

    public boolean loadLanguage() {
        for (String key : config.getKeys(false)) {
            languageMap.putIfAbsent(key, config.getString(key));
        }
        return true;
    }

    public String translate(String key) {
        return languageMap.getOrDefault(key, null);
    }

    public CustomYmlManager getConfig() {
        return customYmlManager;
    }
}
