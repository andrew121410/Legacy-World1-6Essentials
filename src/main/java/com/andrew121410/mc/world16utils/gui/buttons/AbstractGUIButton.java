package com.andrew121410.mc.world16utils.gui.buttons;

import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractGUIButton {

    private int slot;
    private ItemStack itemStack;

    public AbstractGUIButton(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public abstract void onClick(GUIClickEvent guiClickEvent);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractGUIButton guiButton = (AbstractGUIButton) o;

        if (slot != guiButton.slot) return false;
        return itemStack != null ? itemStack.equals(guiButton.itemStack) : guiButton.itemStack == null;
    }

    @Override
    public int hashCode() {
        int result = slot;
        result = 31 * result + (itemStack != null ? itemStack.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GUIButton{" +
                "slot=" + slot +
                ", itemStack=" + itemStack +
                '}';
    }
}
