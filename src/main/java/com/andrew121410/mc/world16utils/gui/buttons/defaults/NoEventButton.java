package com.andrew121410.mc.world16utils.gui.buttons.defaults;

import com.andrew121410.mc.world16utils.gui.buttons.CloneableGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import org.bukkit.inventory.ItemStack;

public class NoEventButton extends CloneableGUIButton {

    public NoEventButton(int slot, ItemStack itemStack) {
        super(slot, itemStack);
    }

    @Override
    public void onClick(GUIClickEvent guiClickEvent) {

    }
}
