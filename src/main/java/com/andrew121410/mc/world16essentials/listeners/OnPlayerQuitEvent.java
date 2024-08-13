package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {

    private final World16Essentials plugin;
    private final API api;

    public OnPlayerQuitEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Save their flying state.
        this.api.saveFlyingState(player);

        this.plugin.getPlayerInitializer().unload(player);
        event.setQuitMessage((api.parseMessage(player, api.getMessagesUtils().getLeaveMessage())));
    }
}
