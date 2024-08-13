package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.World16Utils;
import org.bukkit.plugin.Plugin;

public class OtherPlugins {

    private final World16Essentials plugin;

    // Depend
    private World16Utils world16Utils;

    // Soft Depend
    private boolean hasFloodgate = false;

    public OtherPlugins(World16Essentials plugin) {
        this.plugin = plugin;

        // Soft Depend
        setupFloodgate();
    }

    private void setupFloodgate() {
        Plugin plugin = this.plugin.getServer().getPluginManager().getPlugin("floodgate");

        if (plugin != null) {
            hasFloodgate = true;
        }
    }

    public World16Utils getWorld16Utils() {
        return world16Utils;
    }

    public boolean hasFloodgate() {
        return hasFloodgate;
    }
}
