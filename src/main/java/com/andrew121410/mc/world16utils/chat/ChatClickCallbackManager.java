package com.andrew121410.mc.world16utils.chat;

import com.andrew121410.mc.world16utils.World16Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatClickCallbackManager {

    private final World16Utils plugin;

    private final Map<UUID, Map<String, Consumer<Player>>> map;
    private final Map<UUID, Map<Long, String>> timeMap;

    public ChatClickCallbackManager(World16Utils plugin) {
        this.plugin = plugin;
        this.map = new HashMap<>();
        this.timeMap = new HashMap<>();
    }

    public ClickEvent create(Player player, Consumer<Player> consumer) {
        String key = UUID.randomUUID().toString();

        this.map.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        this.map.get(player.getUniqueId()).put(key, consumer);
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/world1-6utils callclickevent " + key);
    }

    public Map<UUID, Map<String, Consumer<Player>>> getMap() {
        return map;
    }
}