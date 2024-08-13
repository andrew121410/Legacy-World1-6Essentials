package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.commands.back.BackEnum;
import com.andrew121410.mc.world16essentials.managers.SavedInventoriesManager;
import com.andrew121410.mc.world16essentials.objects.SavedInventoryObject;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class OnPlayerDeathEvent implements Listener {

    private final World16Essentials plugin;

    private final Map<UUID, Map<BackEnum, UnlinkedWorldLocation>> backMap;

    public OnPlayerDeathEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.backMap = this.plugin.getMemoryHolder().getBackMap();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // Back
        Map<BackEnum, UnlinkedWorldLocation> playerBackMap = this.backMap.get(player.getUniqueId());
        if (playerBackMap != null) {
            playerBackMap.remove(BackEnum.DEATH);
            playerBackMap.put(BackEnum.DEATH, new UnlinkedWorldLocation(player.getLocation()));
        }

        // If saveInventoryOnDeath is true then save the inventory.
        if (this.plugin.getApi().getConfigUtils().isSaveInventoryOnDeath()) {
            SavedInventoriesManager savedInventoriesManager = this.plugin.getSavedInventoriesManager();

            Set<String> inventorySet = this.plugin.getMemoryHolder().getSavedInventoryMap().get(player.getUniqueId());

            // Shouldn't be null, but just in case.
            if (inventorySet == null) return;

            SavedInventoryObject savedInventoryObject = SavedInventoryObject.create(player, "death");

            savedInventoriesManager.delete(player.getUniqueId(), "death");
            savedInventoriesManager.save(player.getUniqueId(), savedInventoryObject);
        }
    }
}