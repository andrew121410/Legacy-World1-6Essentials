package com.andrew121410.mc.world16utils.gui;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.gui.buttons.AbstractGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.CloneableGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ClickEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.NoEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import com.andrew121410.mc.world16utils.gui.buttons.events.pages.GUINextPageEvent;
import com.andrew121410.mc.world16utils.gui.buttons.events.pages.PageEventType;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class PaginatedGUIMultipageListWindow extends GUIWindow {

    private String name;
    private int size = 54; // Default size of gui

    private Map<Integer, PaginatedReturn> pages = new HashMap<>();
    private List<CloneableGUIButton> customBottomButtons = new ArrayList<>();

    private int currentPage;

    private Function<Integer, PaginatedReturn> buttonProvider = null;
    private Consumer<GUINextPageEvent> pageEvent = null;

    private boolean cacheMode = true;
    private boolean asyncMode = false;
    private PaginatedReturn paginatedReturn;

    private boolean isWaiting = false;

    public PaginatedGUIMultipageListWindow(String name, Integer currentPage, boolean cacheMode, boolean asyncMode) {
        this.name = name;
        this.currentPage = currentPage != null ? currentPage : 0;
        this.cacheMode = cacheMode;
        this.asyncMode = asyncMode;
    }

    public PaginatedGUIMultipageListWindow(String name, Integer currentPage) {
        this(name, currentPage, true, false);
    }

    @Override
    public void onCreate(Player player) {
        handle(player);
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }

    @Override
    public void open(Player player) {
        if (this.paginatedReturn == null && super.isFirst() && this.asyncMode) {
            handle(player);
            return; // Do not open the GUI
        }

        super.open(player);
    }

    private void handle(Player player) {
        // Call button provider to populate the page
        if (this.buttonProvider != null && this.paginatedReturn == null) {
            if (!this.pages.containsKey(this.currentPage)) {
                if (this.asyncMode) { // If async mode is enabled, run the button provider in a new thread
                    // Setup waiting timer
                    setupWaitingTimer(player);

                    World16Essentials.getPlugin().getServer().getScheduler().runTaskAsynchronously(World16Essentials.getPlugin(), () -> {
                        int thePageNumber = this.currentPage;

                        // This wait pause this thread until the paginated is returned
                        PaginatedReturn theReturnPage = this.buttonProvider.apply(thePageNumber);

                        // theReturnPage can be null that means the button provider will return when it's ready on its own.
                        if (theReturnPage != null) {
                            setPageDone(thePageNumber, theReturnPage);
                        }
                    });
                    return; // No need to continue
                } else { // If async mode is disabled, run the button provider in the main thread
                    this.paginatedReturn = this.buttonProvider.apply(this.currentPage);

                    // If cache mode is enabled, cache the buttons
                    if (this.cacheMode && this.paginatedReturn != null)
                        this.pages.putIfAbsent(this.currentPage, paginatedReturn);
                }
                // If the page is cached, use the cached buttons
            } else if (this.cacheMode && this.pages.containsKey(this.currentPage)) {
                this.paginatedReturn = this.pages.get(this.currentPage);
            }
        }

        // From the paginated return
        List<CloneableGUIButton> buttons = this.paginatedReturn != null ? this.paginatedReturn.getButtons() : this.pages.get(this.currentPage).getButtons();

        // If the buttons are null, return
        if (buttons == null) {
            World16Essentials.getPlugin().getLogger().log(java.util.logging.Level.WARNING, "Buttons are null, returning...");
            return;
        }

        // Determine and set the slot numbers for the buttons
        GUIMultipageListWindow.determineSlotNumbers(buttons, 45);

        // Create the bottom buttons
        List<CloneableGUIButton> bottomButtons = createBottomButtons(player);

        // Combine the buttons
        List<AbstractGUIButton> guiButtonList = new ArrayList<>();
        guiButtonList.addAll(buttons);
        guiButtonList.addAll(bottomButtons);

        this.update(guiButtonList, this.name, this.size);
        if (!super.isFirst()) {
            this.refresh(player);
        }
    }

    private List<CloneableGUIButton> createBottomButtons(Player player) {
        boolean hasPreviousPage = paginatedReturn != null ? paginatedReturn.hasPreviousPage() : this.currentPage != 0 && this.pages.get(this.currentPage - 1) != null;
        boolean hasNextPage = paginatedReturn != null ? paginatedReturn.hasNextPage() : this.pages.get(this.currentPage + 1) != null;

        // Create the bottom buttons list
        List<CloneableGUIButton> bottomButtons = new ArrayList<>();

        // Show previous page button if not on first page and previous page exists
        if (hasPreviousPage) {
            bottomButtons.add(new ClickEventButton(45, InventoryUtils.createItem(Material.ARROW, 1, "&6Previous Page"), (guiClickEvent) -> {
                handlePageChange(player, guiClickEvent, this.currentPage - 1, PageEventType.PREV_PAGE);
            }));
        }

        // Show next page button if the next page exists
        if (hasNextPage) {
            bottomButtons.add(new ClickEventButton(53, InventoryUtils.createItem(Material.ARROW, 1, "&6Go to next page"), (guiClickEvent) -> {
                handlePageChange(player, guiClickEvent, this.currentPage + 1, PageEventType.NEXT_PAGE);
            }));
        }

        // Show current page button
        int realPageNumber = this.currentPage + 1;
        bottomButtons.add(new NoEventButton(49, InventoryUtils.createItem(Material.PAPER, realPageNumber <= 64 ? realPageNumber : 1, "&5Current Page", "&aCurrent Page: &6" + realPageNumber)));

        // Add custom bottom buttons
        for (CloneableGUIButton customBottomButton : this.customBottomButtons) {
            // Check if the slot is already taken
            if (bottomButtons.stream().anyMatch(cloneableGUIButton -> cloneableGUIButton.getSlot() == customBottomButton.getSlot())) {
                World16Essentials.getPlugin().getLogger().log(java.util.logging.Level.WARNING, "Bottom button at slot " + customBottomButton.getSlot() + " already exists, skipping...");
                continue;
            }

            bottomButtons.add(customBottomButton);
        }

        return bottomButtons;
    }

    private void handlePageChange(Player player, GUIClickEvent guiClickEvent, int newPage, PageEventType pageEventType) {
        // Call page event (before page change)
        if (this.pageEvent != null) {
            this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.currentPage, newPage, pageEventType, false));
        }

        // Change page number
        this.currentPage = newPage;

        // Reset the paginated return
        this.paginatedReturn = null;

        // Handle the new page
        this.handle(player);

        // Call page event (after page change)
        if (this.pageEvent != null) {
            this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.currentPage, null, pageEventType, true));
        }
    }

    public void setPageDone(int pageNumber, PaginatedReturn paginatedReturn) {
        // Cache the buttons if cache mode is enabled
        if (cacheMode) {
            this.pages.putIfAbsent(pageNumber, paginatedReturn);
        }

        // Set the current page number
        this.currentPage = pageNumber;

        // Set the paginated return
        this.paginatedReturn = paginatedReturn;
    }

    private void setupWaitingTimer(Player player) {
        // Close the GUI if the player is in the GUI
        if (player.getOpenInventory().getTopInventory().getHolder() instanceof AbstractGUIWindow) {
            player.closeInventory();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                isWaiting = true;

                player.sendActionBar(Translate.color("&bWaiting for data..."));

                if (paginatedReturn != null) {
                    // Cancel the task
                    this.cancel();

                    // Reset the waiting variable
                    isWaiting = false;

                    // If first time open the GUI
                    if (isFirst()) {
                        open(player);
                        return;
                    }

                    // If not first time, refresh the GUI
                    handle(player);
                    return;
                }

                if (!player.isOnline() || !player.isValid()) {
                    this.cancel();
                }
            }
        }.runTaskTimer(World16Essentials.getPlugin(), 0L, 20L);
    }

    public List<CloneableGUIButton> getCustomBottomButtons() {
        return customBottomButtons;
    }

    public void setCustomBottomButtons(List<CloneableGUIButton> customBottomButtons) {
        this.customBottomButtons = customBottomButtons;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Map<Integer, PaginatedReturn> getPages() {
        return pages;
    }

    public void setPages(Map<Integer, PaginatedReturn> pages) {
        this.pages = pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Function<Integer, PaginatedReturn> getButtonProvider() {
        return buttonProvider;
    }

    public void setButtonProvider(Function<Integer, PaginatedReturn> buttonProvider) {
        this.buttonProvider = buttonProvider;
    }

    public Consumer<GUINextPageEvent> getPageEvent() {
        return pageEvent;
    }

    public void setPageEvent(Consumer<GUINextPageEvent> pageEvent) {
        this.pageEvent = pageEvent;
    }

    public boolean isCacheMode() {
        return cacheMode;
    }

    public void setCacheMode(boolean cacheMode) {
        this.cacheMode = cacheMode;
    }

    public boolean isAsyncMode() {
        return asyncMode;
    }

    public void setAsyncMode(boolean asyncMode) {
        this.asyncMode = asyncMode;
    }

    public PaginatedReturn getPaginatedReturn() {
        return paginatedReturn;
    }

    public void setPaginatedReturn(PaginatedReturn paginatedReturn) {
        this.paginatedReturn = paginatedReturn;
    }
}
