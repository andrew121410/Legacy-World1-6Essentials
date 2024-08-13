package com.andrew121410.mc.world16utils.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CustomYmlManager {

    private final JavaPlugin plugin;

    private String fileName;
    private File file;
    private FileConfiguration fileConfiguration;

    private boolean isNew = false;

    public CustomYmlManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public CustomYmlManager(JavaPlugin plugin, boolean debug) {
        this(plugin);
    }

    public void setup(File folder, String fileName) {
        this.fileName = fileName;

        if (!folder.exists()) {
            if (!folder.mkdir()) {
                throw new RuntimeException("Failed to create directory: " + folder.getAbsolutePath());
            }
        }

        this.file = new File(folder, this.fileName);

        if (!this.file.exists()) {
            try {
                if (this.file.createNewFile()) {
                    this.isNew = true;
                } else {
                    throw new RuntimeException("Failed to create file: " + this.file.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void setup(String fileName) {
        setup(this.plugin.getDataFolder(), fileName);
    }

    public void saveConfig() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return this.fileConfiguration;
    }

    public boolean isNew() {
        return isNew;
    }
}
