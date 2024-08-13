package com.andrew121410.mc.world16utils.gui.buttons.events.pages;

import com.andrew121410.mc.world16utils.gui.AbstractGUIWindow;
import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUINextPageEvent extends GUIClickEvent {

    private final int page;
    private final Integer nextPage;
    private PageEventType pageEventType;
    private final boolean afterPageCreation;

    public GUINextPageEvent(AbstractGUIWindow guiWindow, InventoryClickEvent event, int page, Integer nextPage, PageEventType pageEventType, boolean afterPageCreation) {
        super(guiWindow, event);
        this.page = page;
        this.nextPage = nextPage;
        this.pageEventType = pageEventType;
        this.afterPageCreation = afterPageCreation;
    }

    public GUINextPageEvent(GUIClickEvent guiClickEvent, int page, Integer nextPage, PageEventType pageEventType, boolean afterPageCreation) {
        this(guiClickEvent.getGuiWindow(), guiClickEvent.getEvent(), page, nextPage, pageEventType, afterPageCreation);
    }

    public int getPage() {
        return page;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public PageEventType getPageEventType() {
        return pageEventType;
    }

    public boolean isAfterPageCreation() {
        return afterPageCreation;
    }
}

