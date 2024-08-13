package com.andrew121410.mc.world16utils.gui.buttons.defaults;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.gui.buttons.CloneableGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class ChatResponseButton extends CloneableGUIButton {

    private final String title;
    private final String subtitle;
    private final BiConsumer<Player, String> event;

    public ChatResponseButton(int slot, ItemStack itemStack, String title, String subtitle, BiConsumer<Player, String> event) {
        super(slot, itemStack);
        this.title = title;
        this.subtitle = subtitle;
        this.event = event;
    }

    @Override
    public void onClick(GUIClickEvent guiClickEvent) {
        Player player = (Player) guiClickEvent.getEvent().getWhoClicked();
        player.closeInventory();
        World16Essentials.getPlugin().getWorld16Utils().getChatResponseManager().create(player, this.title, this.subtitle, this.event);
    }
}
