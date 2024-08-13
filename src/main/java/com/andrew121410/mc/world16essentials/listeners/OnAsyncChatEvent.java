package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnAsyncChatEvent implements Listener {

    private final World16Essentials plugin;
    private final API api;

    public OnAsyncChatEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // Name ping
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            for (Player targetPlayer : plugin.getServer().getOnlinePlayers()) {
                if (message.contains(targetPlayer.getName())) {
                    if (this.api.isAfk(targetPlayer)) {
                        this.plugin.getServer().broadcastMessage(Translate.color("&cSystem -> <gold><u>" + targetPlayer.getName() + " is AFK!"));
                    }
                    targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }

                if (message.contains("!" + targetPlayer.getName())) {
                    targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_HORSE_DEATH, 10.0f, 1.0f);
                }
            }
        });
    }
}