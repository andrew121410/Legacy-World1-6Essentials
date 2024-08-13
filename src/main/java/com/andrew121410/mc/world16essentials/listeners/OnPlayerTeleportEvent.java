package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.commands.back.BackEnum;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Map;
import java.util.UUID;

public class OnPlayerTeleportEvent implements Listener {

    private final World16Essentials plugin;

    private final Map<UUID, Map<BackEnum, UnlinkedWorldLocation>> backMap;

    public OnPlayerTeleportEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.backMap = this.plugin.getMemoryHolder().getBackMap();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();

        // Only save location if teleporting more than 5 blocks.
        if (!to.getWorld().equals(from.getWorld()) || to.distanceSquared(from) > 25) {
            Map<BackEnum, UnlinkedWorldLocation> playerBackMap = this.backMap.get(player.getUniqueId());
            if (playerBackMap != null) {
                playerBackMap.remove(BackEnum.TELEPORT);
                playerBackMap.put(BackEnum.TELEPORT, new UnlinkedWorldLocation(player.getLocation()));
            }
        }
    }
}