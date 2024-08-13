package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.config.CustomConfigManager;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class WarpManager {

    private final Map<String, UnlinkedWorldLocation> warpsMap;

    private final World16Essentials plugin;
    private final CustomYmlManager warpsYml;

    public WarpManager(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.warpsYml = customConfigManager.getWarpsYml();
        this.warpsMap = this.plugin.getMemoryHolder().getWarpsMap();
    }

    public void loadAllWarps() {
        ConfigurationSection cs = getConfigurationSection();
        for (String key : cs.getKeys(false)) {
            ConfigurationSection warpCs = cs.getConfigurationSection(key);

            Object object = warpCs.get("Location");

//            // Convert old location to UnlinkedWorldLocation
//            if (!(object instanceof UnlinkedWorldLocation)) {
//                ConfigurationSection locationCs = warpCs.getConfigurationSection("Location");
//                if (locationCs != null) {
//                    String world = locationCs.getString("world", null);
//                    String x = locationCs.getString("x", null);
//                    String y = locationCs.getString("y", null);
//                    String z = locationCs.getString("z", null);
//                    String yaw = locationCs.getString("yaw", null);
//                    String pitch = locationCs.getString("pitch", null);
//
//                    if (world != null && x != null && y != null && z != null && yaw != null && pitch != null) {
//                        UnlinkedWorldLocation unlinkedWorldLocation = new UnlinkedWorldLocation(
//                                world,
//                                Double.parseDouble(x),
//                                Double.parseDouble(y),
//                                Double.parseDouble(z),
//                                Float.parseFloat(yaw),
//                                Float.parseFloat(pitch));
//
//                        warpCs.set("Location", unlinkedWorldLocation);
//                        this.warpsYml.saveConfig();
//                    }
//                }
//            }

            Location location = (Location) warpCs.get("Location");
            UnlinkedWorldLocation unlinkedWorldLocation = new UnlinkedWorldLocation(location);

            this.warpsMap.putIfAbsent(key, unlinkedWorldLocation);
        }
    }

    public void add(String name, Location location) {
        String newWarpName = name.toLowerCase();

        this.warpsMap.put(newWarpName, new UnlinkedWorldLocation(location));
        ConfigurationSection cs = getConfigurationSection();
        ConfigurationSection warpCs = cs.createSection(newWarpName);
        warpCs.set("Location", new UnlinkedWorldLocation(location));
        this.warpsYml.saveConfig();
    }

    public void delete(String name) {
        String newWarpName = name.toLowerCase();
        if (!this.warpsMap.containsKey(newWarpName)) return;
        ConfigurationSection cs = getConfigurationSection();
        cs.set(newWarpName, null);
        this.warpsYml.saveConfig();
        this.warpsMap.remove(newWarpName);
    }

    private ConfigurationSection getConfigurationSection() {
        ConfigurationSection cs = this.warpsYml.getConfig().getConfigurationSection("Warps");
        if (cs == null) {
            this.warpsYml.getConfig().createSection("Warps");
            this.warpsYml.saveConfig();
            this.warpsYml.reloadConfig();
            // Get it again
            cs = this.warpsYml.getConfig().getConfigurationSection("Warps");
        }
        return cs;
    }
}