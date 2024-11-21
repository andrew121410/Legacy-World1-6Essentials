package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.KitObject;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoinEvent implements Listener {

    private final World16Essentials plugin;
    private final API api;

    public OnPlayerJoinEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Kit
        if (!player.hasPlayedBefore()) {
            for (KitObject value : this.plugin.getMemoryHolder().getKitsMap().values()) {
                if (value.getSettings().isGiveOnFirstJoin()) {
                    this.plugin.getKitManager().giveKit(player, value);
                }
            }
        }

        // Load flying state (this prevents them from falling to their death when they join)
        this.api.loadFlyingState(player);

        String message = player.hasPlayedBefore() ? this.api.getMessagesUtils().getWelcomeBackMessage() : this.api.getMessagesUtils().getFirstJoinMessage();
        event.setJoinMessage(api.parseMessage(player, message));
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);

        if (player.isOp() && this.api.getConfigUtils().isShowLastUpdatedMessageToOPs()) {
            player.sendMessage(Translate.color("&cL&6e&eg&aa&3c&9y&5-&cW&6o&er&al&3d&91&5-&c6&6E&es&as&3e&9n&5t&ci&6a&el&as was last updated on " + api.getDateOfBuild()));
        }

        this.plugin.getPlayerInitializer().load(player);
    }
}