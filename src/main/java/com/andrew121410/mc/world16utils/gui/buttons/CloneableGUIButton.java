package com.andrew121410.mc.world16utils.gui.buttons;

import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import org.bukkit.inventory.ItemStack;

public class CloneableGUIButton extends AbstractGUIButton implements Cloneable {

    public CloneableGUIButton(int slot, ItemStack itemStack) {
        super(slot, itemStack);
    }

    @Override
    public void onClick(GUIClickEvent guiClickEvent) {

    }

    @Override
    public CloneableGUIButton clone() {
        return new CloneableGUIButton(this.getSlot(), this.getItemStack().clone());
    }
}
