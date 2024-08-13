package com.andrew121410.mc.world16utils;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.ChatClickCallbackManager;
import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.listeners.OnAsyncPlayerChatEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryClickEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryCloseEvent;
import com.andrew121410.mc.world16utils.listeners.OnPlayerQuitEvent;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Consumer;

public final class World16Utils {

    static {
        ConfigurationSerialization.registerClass(UnlinkedWorldLocation.class, "UnlinkedWorldLocation");
    }

    private ChatResponseManager chatResponseManager;
    private ChatClickCallbackManager chatClickCallbackManager;

    private World16Essentials plugin;

    public void onEnable(World16Essentials plugin) {
        this.plugin = plugin;
        this.chatResponseManager = new ChatResponseManager(plugin);
        this.chatClickCallbackManager = new ChatClickCallbackManager(this);
        registerListeners(plugin);
        registerCommand();
    }

    private void registerListeners(World16Essentials plugin) {
        new OnAsyncPlayerChatEvent(plugin, this.chatResponseManager);
        new OnInventoryClickEvent(plugin);
        new OnInventoryCloseEvent(plugin);
        new OnPlayerQuitEvent(plugin);
    }

    private void registerCommand() {
        this.plugin.getCommand("world1-6utils").setExecutor((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.world1-6utils")) {
                sender.sendMessage("You do not have permission to use this command.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("/world1-6utils update");
            } else if (args[0].equalsIgnoreCase("update") && args.length == 2) {
                new UnsupportedOperationException("Update command is not supported.");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("callclickevent")) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("You must be a player to use this command.");
                    return true;
                }

                Map<String, Consumer<Player>> map = this.chatClickCallbackManager.getMap().get(player.getUniqueId());
                if (map == null) return true;

                String key = args[1];
                Consumer<Player> consumer = map.get(key);
                if (consumer == null) return true;
                consumer.accept((Player) sender);
                map.remove(key);
            }
            return true;
        });
    }

    public ChatResponseManager getChatResponseManager() {
        return chatResponseManager;
    }

    public ChatClickCallbackManager getChatClickCallbackManager() {
        return chatClickCallbackManager;
    }
}
