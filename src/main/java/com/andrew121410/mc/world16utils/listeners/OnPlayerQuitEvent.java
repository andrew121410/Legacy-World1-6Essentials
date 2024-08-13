package com.andrew121410.mc.world16utils.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {

    private World16Essentials plugin;

    public OnPlayerQuitEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.plugin.getWorld16Utils().getChatClickCallbackManager().getMap().remove(player.getUniqueId());
    }
}
