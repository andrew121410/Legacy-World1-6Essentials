package com.andrew121410.mc.world16utils.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.gui.AbstractGUIWindow;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class OnInventoryCloseEvent implements Listener {

    public World16Essentials plugin;

    public OnInventoryCloseEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (inventoryHolder instanceof AbstractGUIWindow) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (event.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof AbstractGUIWindow)
                    return;
                AbstractGUIWindow guiWindow = (AbstractGUIWindow) inventoryHolder;
                guiWindow.onClose(event);
            }, 2L);
        }
    }
}
