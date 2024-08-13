package com.andrew121410.mc.world16utils.gui;

import com.andrew121410.mc.world16utils.gui.buttons.CloneableGUIButton;

import java.util.List;

public class PaginatedReturn {

    private boolean hasNextPage;
    private boolean hasPreviousPage;

    private List<CloneableGUIButton> buttons;

    public PaginatedReturn(boolean hasNextPage, boolean hasPreviousPage, List<CloneableGUIButton> buttons) {
        this.hasNextPage = hasNextPage;
        this.hasPreviousPage = hasPreviousPage;
        this.buttons = buttons;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public boolean hasPreviousPage() {
        return hasPreviousPage;
    }

    public List<CloneableGUIButton> getButtons() {
        return buttons;
    }
}
