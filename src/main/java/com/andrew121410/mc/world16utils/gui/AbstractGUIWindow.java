package com.andrew121410.mc.world16utils.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class AbstractGUIWindow implements InventoryHolder {

    private boolean isFirst;
    private Inventory inventory;

    public AbstractGUIWindow() {
        this.isFirst = true;
    }

    public abstract String getName();

    public abstract int getSlotCount();

    public abstract void onCreate(Player player);

    public abstract void populateInventory(Player player);

    public abstract boolean onSlotClicked(InventoryClickEvent event);

    public void open(Player player) {
        if (this.isFirst) {
            this.onCreate(player);
            this.isFirst = false;
        }
        inventory = Bukkit.createInventory(this, getSlotCount(), getName());
        this.populateInventory(player);
        player.openInventory(this.inventory);
    }

    public void refresh(Player player) {
        // Try to open the Inventory again if the GUI refreshed and the player isn't in the GUI.
        if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof AbstractGUIWindow)) {
            this.open(player);
        } else {
            this.inventory.clear();
            this.populateInventory(player);
            player.updateInventory();
        }
    }

    public abstract void onClose(InventoryCloseEvent event);

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean isFirst() {
        return isFirst;
    }
}
