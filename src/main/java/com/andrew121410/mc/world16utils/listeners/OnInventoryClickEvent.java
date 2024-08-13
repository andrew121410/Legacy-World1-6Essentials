package com.andrew121410.mc.world16utils.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.gui.AbstractGUIWindow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class OnInventoryClickEvent implements Listener {

    private World16Essentials plugin;

    public OnInventoryClickEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            InventoryHolder inventoryHolder = event.getClickedInventory().getHolder();
            if (inventoryHolder instanceof AbstractGUIWindow guiWindow) {
                event.setCancelled(true);
                guiWindow.onSlotClicked(event);
            }
        }
    }
}
