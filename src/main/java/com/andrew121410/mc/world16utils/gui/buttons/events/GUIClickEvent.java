package com.andrew121410.mc.world16utils.gui.buttons.events;

import com.andrew121410.mc.world16utils.gui.AbstractGUIWindow;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIClickEvent {

    private final AbstractGUIWindow guiWindow;
    private final InventoryClickEvent event;

    public GUIClickEvent(AbstractGUIWindow guiWindow, InventoryClickEvent event) {
        this.guiWindow = guiWindow;
        this.event = event;
    }

    public AbstractGUIWindow getGuiWindow() {
        return guiWindow;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }
}
