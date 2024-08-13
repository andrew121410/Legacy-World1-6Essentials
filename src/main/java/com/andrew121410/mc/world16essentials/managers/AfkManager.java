package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.AfkObject;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class AfkManager {

    private final Map<UUID, AfkObject> afkMap;

    private final World16Essentials plugin;
    private final API api;

    public AfkManager(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.afkMap = this.plugin.getMemoryHolder().getAfkMap();

        afkChecker();
    }

    private void afkChecker() {
        // Checks if the player is afk. 3m
        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<UUID, AfkObject>> iterator = afkMap.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<UUID, AfkObject> entry = iterator.next();
                    UUID uuid = entry.getKey();
                    AfkObject afkObject = entry.getValue();

                    Player player = plugin.getServer().getPlayer(uuid);
                    if (player == null || !player.isOnline()) {
                        iterator.remove();
                        return;
                    }

                    // Don't run if player is already AFK
                    if (afkObject.isAfk()) return;

                    // Checks if the player has not moved in 3 min if not afk them if so restart()
                    if (player.getLocation().equals(afkObject.getLocation()) && !api.didPlayerJustJoin(player)) {
                        api.doAfk(player, null, !afkObject.isIgnore());
                    } else afkObject.restart(player);
                }
            }
        }.runTaskTimer(plugin, 1200L, 2400L);

        // Checks if the player moves, this runs every 2 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<UUID, AfkObject>> iterator = afkMap.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<UUID, AfkObject> entry = iterator.next();
                    UUID uuid = entry.getKey();
                    AfkObject afkObject = entry.getValue();

                    Player player = plugin.getServer().getPlayer(uuid);
                    if (player == null || !player.isOnline()) {
                        iterator.remove();
                        return;
                    }

                    // If not afk don't run
                    if (!afkObject.isAfk()) return;

                    if (afkObject.isIgnore()) {
                        player.sendActionBar(Translate.miniMessage("<aqua>You are AFK, but it won't be announced."));
                    }

                    // Only if the player moves more than 3 blocks
                    if (player.getLocation().distanceSquared(afkObject.getLocation()) > 9) {
                        api.doAfk(player, null, !afkObject.isIgnore());
                    }
                }
            }
        }.runTaskTimer(this.plugin, 1200L, 40L);
    }
}
