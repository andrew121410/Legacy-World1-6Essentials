package com.andrew121410.mc.world16utils.gui;

import com.andrew121410.mc.world16utils.gui.buttons.AbstractGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.CloneableGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ChatResponseButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ClickEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.NoEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.events.pages.GUINextPageEvent;
import com.andrew121410.mc.world16utils.gui.buttons.events.pages.PageEventType;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import com.andrew121410.mc.world16utils.utils.Utils;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GUIMultipageListWindow extends GUIWindow {

    private String name;
    private int slots = 54;

    private int page = 0;
    private int itemsPerPage;
    private List<List<CloneableGUIButton>> pages;

    private Consumer<GUINextPageEvent> pageEvent = null;

    public GUIMultipageListWindow(String name, List<CloneableGUIButton> buttons, Integer itemsPerPage) {
        this.name = name;
        this.itemsPerPage = itemsPerPage != null ? itemsPerPage : 45;
        this.pages = setup(buttons);
    }

    public GUIMultipageListWindow(String name, List<CloneableGUIButton> buttons) {
        this(name, buttons, null);
    }

    @Override
    public void onCreate(Player player) {
        handle(player, null);
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }

    private void handle(Player player, List<List<CloneableGUIButton>> searchResults) {
        List<CloneableGUIButton> bottomButtons = new ArrayList<>();
        List<List<CloneableGUIButton>> pages = this.pages;

        if (searchResults != null) {
            pages = searchResults;
        }

        if (page != 0 && Utils.indexExists(pages, page - 1)) {
            bottomButtons.add(new ClickEventButton(45, InventoryUtils.createItem(Material.ARROW, 1, "&6Previous Page"), (guiClickEvent) -> {
                if (this.pageEvent != null)
                    this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.page, this.page - 1, PageEventType.PREV_PAGE, false));
                this.page--;
                this.handle(player, searchResults);
                if (this.pageEvent != null)
                    this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.page, null, PageEventType.PREV_PAGE, true));
            }));
        }

        if (pages.size() >= 2 && Utils.indexExists(pages, page + 1)) {
            bottomButtons.add(new ClickEventButton(53, InventoryUtils.createItem(Material.ARROW, 1, "&6Go to next page"), (guiClickEvent) -> {
                if (this.pageEvent != null)
                    this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.page, this.page + 1, PageEventType.NEXT_PAGE, false));
                this.page++;
                this.handle(player, searchResults);
                if (this.pageEvent != null)
                    this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.page, null, PageEventType.NEXT_PAGE, true));
            }));
        }

        List<AbstractGUIButton> guiButtonList = new ArrayList<>(pages.get(page));

        int realPageNumber = this.page + 1;
        bottomButtons.add(new NoEventButton(49, InventoryUtils.createItem(Material.PAPER, realPageNumber <= 64 ? realPageNumber : 1, "&5Current Page", "&aCurrent Page: &6" + realPageNumber)));

        if (searchResults == null) {
            bottomButtons.add(new ChatResponseButton(48, InventoryUtils.createItem(Material.COMPASS, 1, "&6Search", "Use this to search for things"), null, null, (player1, string) -> {
                List<CloneableGUIButton> sortByContainsList = new ArrayList<>();

                List<CloneableGUIButton> toSort = new ArrayList<>();
                for (List<CloneableGUIButton> guiButtons : this.pages) {
                    for (CloneableGUIButton guiButton : guiButtons) {
                        toSort.add(guiButton.clone());
                    }
                }

                // Loop through all the buttons and add them to a list if they contain the search string.
                for (CloneableGUIButton guiButton : toSort) {
                    ItemStack itemStack = guiButton.getItemStack();
                    if (itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName()) {
                        if (itemStack.getItemMeta().getDisplayName().toLowerCase().contains(string.toLowerCase())) {
                            sortByContainsList.add(guiButton);
                        }
                    }
                }

                if (sortByContainsList.size() == 0) {
                    player1.sendMessage("Nothing was found with the search string of: " + string);
                    return;
                }

                this.page = 0;
                this.handle(player1, setup(sortByContainsList));
            }));
        } else {
            bottomButtons.add(new ClickEventButton(48, InventoryUtils.createItem(Material.BARRIER, 1, "&eReturn", "Click me to exit search mode", "You're currently in search mode"), (guiClickEvent) -> {
                this.page = 0;
                this.handle(player, null);
            }));
        }

        // Add the bottom buttons to the GUIButtonList.
        guiButtonList.addAll(bottomButtons);

        this.update(guiButtonList, this.name, this.slots);
        if (!super.isFirst()) {
            this.refresh(player);
        }
    }

    private List<List<CloneableGUIButton>> setup(List<CloneableGUIButton> guiButtonList) {
        List<List<CloneableGUIButton>> thePages = Lists.partition(guiButtonList, this.itemsPerPage);

        // Set the correct slot numbers for the buttons.
        for (List<CloneableGUIButton> page : thePages) {
            determineSlotNumbers(page, this.itemsPerPage);
        }

        return thePages;
    }

    public static void determineSlotNumbers(List<CloneableGUIButton> guiButtonList, int itemsPerPage) {
        int i = 0;

        for (CloneableGUIButton guiButton : guiButtonList) {
            guiButton.setSlot(i);

            if (i < itemsPerPage) {
                i++;
            } else {
                i = 0;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public List<List<CloneableGUIButton>> getPages() {
        return pages;
    }

    public void setPages(List<List<CloneableGUIButton>> pages) {
        this.pages = pages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Consumer<GUINextPageEvent> getPageEvent() {
        return pageEvent;
    }

    public void setPageEvent(Consumer<GUINextPageEvent> pageEvent) {
        this.pageEvent = pageEvent;
    }
}