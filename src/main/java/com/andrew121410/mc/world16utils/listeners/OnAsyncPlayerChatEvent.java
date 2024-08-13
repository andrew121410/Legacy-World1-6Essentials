package com.andrew121410.mc.world16utils.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnAsyncPlayerChatEvent implements Listener {

    private World16Essentials plugin;
    private ChatResponseManager chatResponseManager;

    public OnAsyncPlayerChatEvent(World16Essentials plugin, ChatResponseManager chatResponseManager) {
        this.plugin = plugin;
        this.chatResponseManager = chatResponseManager;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String messageString = event.getMessage();

        if (chatResponseManager.isWaiting(player.getUniqueId())) {
            event.setCancelled(true);
            if (messageString.equalsIgnoreCase("cancel")) {
                chatResponseManager.remove(player.getUniqueId());
                player.sendMessage(Translate.color("&eSuccessfully canceled."));
                return;
            }
            Bukkit.getScheduler().runTask(plugin, () -> {
                chatResponseManager.get(player).accept(player, messageString);
                chatResponseManager.remove(player.getUniqueId());
            });
        }
    }
}
