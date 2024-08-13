package com.andrew121410.mc.world16utils.gui.buttons.defaults;

import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.gui.AbstractGUIWindow;
import com.andrew121410.mc.world16utils.gui.buttons.CloneableGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.BiConsumer;

public class LoreShifterButton extends CloneableGUIButton {

    private BiConsumer<GUIClickEvent, Integer> onClickBiConsumer;

    private boolean needToConfirm;

    private String prefix = Translate.color("&b&l>&r ");
    private ItemStack itemStack;
    private List<String> beforeLoreList; // This like extra stuff before the options.
    private List<String> loreList;

    private int lineNumber = 0;
    private String unModifiedLore;
    private String modifiedLore;

    public LoreShifterButton(int slot, ItemStack itemStack, List<String> options, boolean needToConfirm, BiConsumer<GUIClickEvent, Integer> onClickBiConsumer) {
        super(slot, itemStack);
        this.itemStack = itemStack;
        this.beforeLoreList = null;
        this.loreList = options;
        this.needToConfirm = needToConfirm;
        this.onClickBiConsumer = onClickBiConsumer;

        String firstLore = getLoreFromIndex(0);
        if (firstLore != null) {
            this.unModifiedLore = firstLore;
            this.modifiedLore = this.prefix + firstLore;
            this.loreList.set(0, this.modifiedLore);
        }

        refreshTheItem();
    }

    public LoreShifterButton(int slot, ItemStack itemStack, List<String> before, List<String> options, boolean needToConfirm, BiConsumer<GUIClickEvent, Integer> onClickBiConsumer) {
        this(slot, itemStack, options, needToConfirm, onClickBiConsumer);
        this.beforeLoreList = before;

        refreshTheItem();
    }

    @Override
    public void onClick(GUIClickEvent guiClickEvent) {
        InventoryClickEvent event = guiClickEvent.getEvent();
        AbstractGUIWindow guiWindow = guiClickEvent.getGuiWindow();
        Player player = (Player) event.getWhoClicked();

        if (needToConfirm) {
            if (event.getClick() == ClickType.LEFT) {
                move(guiClickEvent);
            } else if (event.getClick() == ClickType.RIGHT) {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                this.onClickBiConsumer.accept(guiClickEvent, this.lineNumber);
            }
        } else {
            if (event.getClick() == ClickType.LEFT) {
                move(guiClickEvent);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                this.onClickBiConsumer.accept(guiClickEvent, this.lineNumber);
            }
        }
    }

    private void move(GUIClickEvent guiClickEvent) {
        InventoryClickEvent event = guiClickEvent.getEvent();
        AbstractGUIWindow guiWindow = guiClickEvent.getGuiWindow();
        Player player = (Player) event.getWhoClicked();

        this.loreList.set(this.lineNumber, this.unModifiedLore);
        this.lineNumber++;

        // Get the lore from the index, if it's null then reset it back to 0.
        String lore = getLoreFromIndex(this.lineNumber);
        if (lore == null) {
            this.lineNumber = 0;
            lore = getLoreFromIndex(this.lineNumber);

            // Shouldn't be null but just in case.
            if (lore == null) {
                return;
            }
        }

        // Save and set the new lore.
        this.unModifiedLore = lore;
        this.modifiedLore = this.prefix + lore;
        this.loreList.set(this.lineNumber, this.modifiedLore);

        // Refresh the item with the new lore list.
        refreshTheItem();

        // Refresh
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F);
        guiWindow.refresh(player);
    }

    private void refreshTheItem() {
        List<String> list = this.beforeLoreList == null ? this.loreList : this.beforeLoreList;

        if (this.beforeLoreList != null) {
            list.add("\n");
            list.addAll(this.loreList);
        }

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(list);
        this.itemStack.setItemMeta(itemMeta);
    }

    private String getLoreFromIndex(int index) {
        if (index >= 0 && index < this.loreList.size()) {
            return this.loreList.get(index);
        }
        return null;
    }
}
