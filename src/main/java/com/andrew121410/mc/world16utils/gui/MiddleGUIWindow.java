package com.andrew121410.mc.world16utils.gui;

import com.andrew121410.mc.world16utils.gui.buttons.AbstractGUIButton;

import java.util.List;

public abstract class MiddleGUIWindow extends GUIWindow {

    int n = 3; // number of buttons
    int size = n <= 3 ? 27 : ((n - 1) / 9 + 1) * 9; // calculate size of GUI
    int startSlot = (size - (n * 2 - 1)) / 2; // calculate starting slot for first button
    int increment = 2; // calculate increment for each subsequent button

    public MiddleGUIWindow() {
    }

    public MiddleGUIWindow(int increment) {
        this.increment = increment;
    }

    @Override
    public void update(List<AbstractGUIButton> guiButtons, String name, Integer slots) {
        // Before we start, we need to know how many buttons we have.
        update(guiButtons.size());

        // We need to re-set the slot for each button.
        for (int i = 0; i < guiButtons.size(); i++) {
            int newSlot = startSlot + (i * increment);
            guiButtons.get(i).setSlot(newSlot);
        }

        super.update(guiButtons, name, slots == null ? size : slots);
    }

    private void update(int numberOfButtons) {
        n = numberOfButtons;
        size = numberOfButtons <= 3 ? 27 : ((numberOfButtons - 1) / 9 + 1) * 9;
        startSlot = (size - (n * 2 - 1)) / 2; // calculate starting slot for first button
    }
}
