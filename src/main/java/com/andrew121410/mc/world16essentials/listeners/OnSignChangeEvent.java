package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnSignChangeEvent implements Listener {

    private final World16Essentials plugin;
    private final API api;

    public OnSignChangeEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void signChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("world16.sign.color") && api.getConfigUtils().isSignTranslateColors()) {
            for (int i = 0; i < 4; i++) {
                String line = event.getLine(i);
                if (line != null && !line.equals("")) event.setLine(i, Translate.color(line));
            }
        }
    }
}
