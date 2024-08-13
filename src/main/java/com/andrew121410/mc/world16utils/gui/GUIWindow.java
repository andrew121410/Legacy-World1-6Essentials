package com.andrew121410.mc.world16utils.gui;

import com.andrew121410.mc.world16utils.gui.buttons.AbstractGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class GUIWindow extends AbstractGUIWindow {

    private String name;
    private int slots;
    private Map<Integer, AbstractGUIButton> guiButtonMap;

    public GUIWindow() {
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getSlotCount() {
        return this.slots;
    }

    @Override
    public void populateInventory(Player player) {
        this.guiButtonMap.forEach((k, v) -> this.getInventory().setItem(k, v.getItemStack()));
    }

    @Override
    public boolean onSlotClicked(InventoryClickEvent event) {
        if (event == null) return false;
        if (event.getCurrentItem() == null) return false;
        AbstractGUIButton guiButton = this.guiButtonMap.get(event.getSlot());
        if (guiButton != null) guiButton.onClick(new GUIClickEvent(this, event));
        return false;
    }

    public void update(List<AbstractGUIButton> guiButtons, String name, Integer slots) {
        if (guiButtons != null)
            this.guiButtonMap = guiButtons.stream().collect(Collectors.toMap(AbstractGUIButton::getSlot, k -> k));
        else this.guiButtonMap = new HashMap<>();
        if (name != null) this.name = name;
        if (slots != null) this.slots = slots;
    }
}
