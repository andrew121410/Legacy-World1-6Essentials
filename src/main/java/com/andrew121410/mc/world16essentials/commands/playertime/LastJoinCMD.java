package com.andrew121410.mc.world16essentials.commands.playertime;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.gui.PaginatedGUIMultipageListWindow;
import com.andrew121410.mc.world16utils.gui.PaginatedReturn;
import com.andrew121410.mc.world16utils.gui.buttons.CloneableGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ChatResponseButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ClickEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import com.andrew121410.mc.world16utils.player.PlayerUtils;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LastJoinCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public LastJoinCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("lastjoin").setExecutor(this);
        this.plugin.getCommand("lastjoin").setTabCompleter((sender, command, alias, args) -> {
                    if (!(sender instanceof Player player)) return null;
                    if (!player.hasPermission("world16.lastjoin") && !player.hasPermission("world16.lastonline")) return null;

                    if (args.length == 1) {
                        return TabUtils.getContainsString(args[0], Collections.singletonList("get"));
                    }

                    return null;
                }
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.lastjoin") && !player.hasPermission("world16.lastonline")) {
            this.plugin.getApi().sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            // Open GUI
            openGUI(player, null);
            return true;
        } else if (args.length == 1) {
            OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(args[0]);

            if (!offlinePlayer.hasPlayedBefore()) {
                player.sendMessage(Translate.color("&cThis user has never joined before..."));
                return true;
            }

            player.sendMessage(Translate.color("&aLast join of &6" + offlinePlayer.getName() + "&a was &6" + this.api.getTimeSinceLastLogin(offlinePlayer) + " &aago."));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("get")) {
            Integer days = Utils.asIntegerOrElse(args[1], null);

            if (days == null) {
                player.sendMessage(Translate.color("&cInvalid number."));
                return true;
            }

            long currentTime = System.currentTimeMillis();
            long thresholdTime = currentTime - ((long) days * 24 * 60 * 60 * 1000); // Convert days to milliseconds

            OfflinePlayer[] offlinePlayers = this.plugin.getServer().getOfflinePlayers();
            List<OfflinePlayer> recentPlayers = new ArrayList<>();

            // Get all players who have joined in the last x days
            for (OfflinePlayer offlinePlayer : offlinePlayers) {
                if (offlinePlayer.getLastPlayed() >= thresholdTime) {
                    recentPlayers.add(offlinePlayer);
                }
            }

            // Sort by greatest to the least time
            recentPlayers.sort((o1, o2) -> Long.compare(o2.getLastPlayed(), o1.getLastPlayed()));

            // If no players have joined in the last x days
            if (recentPlayers.isEmpty()) {
                player.sendMessage(Translate.color("&cNo players have joined in the last &e" + days + " &cdays."));
                return true;
            }

            player.sendMessage(Translate.color("&aPlayers who joined in the last " + days + " days:"));
            for (OfflinePlayer recentPlayer : recentPlayers) {
                player.sendMessage(Translate.color("&6" + recentPlayer.getName() + "&a - " + this.api.getTimeSinceLastLogin(recentPlayer) + " ago."));
            }

            // Click to copy
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("Players who joined in the last ").append(days).append(" days:\n");
//            for (OfflinePlayer recentPlayer : recentPlayers) {
//                stringBuilder.append(recentPlayer.getName()).append(" -> ").append(this.api.getTimeSinceLastLogin(recentPlayer)).append(" ago.").append("\n");
//            }
//            Component component = Translate.miniMessage("<green><bold>[Click to copy!]").clickEvent(ClickEvent.copyToClipboard(stringBuilder.toString()));
//            player.sendMessage(component);
        }
        return true;
    }

    private void openGUI(Player player, String toFilter) {
        String title = toFilter == null ? Translate.miniMessage("<green>Last Join!") : Translate.miniMessage("<green>Last Join! <red>Search: <yellow>" + toFilter);
        PaginatedGUIMultipageListWindow gui = new PaginatedGUIMultipageListWindow(title, 0, true, true);

        Predicate<OfflinePlayer> filter = getFilterPredicate(toFilter);

        // Button provider is async
        gui.setButtonProvider(pageNumber -> {
            makeGUIButtons(gui, player, pageNumber, (paginatedReturn -> {
                gui.setPageDone(pageNumber, paginatedReturn);
            }), filter);
            return null; // We can return null right now as we will setPageDone when it is done making the heads and buttons
        });

        gui.open(player);
    }

    /**
     * Generates a predicate for filtering OfflinePlayers based on specified player names.
     *
     * @param toFilter A string of player names to filter by. Names are separated by commas.
     * @return The generated predicate for filtering OfflinePlayers.
     */
    private Predicate<OfflinePlayer> getFilterPredicate(String toFilter) {
        if (toFilter == null) return null;

        // Remove spaces in between commas if there are any
        toFilter = toFilter.replaceAll(", ", ",");

        String[] playerNames = toFilter.split(",");

        return (offlinePlayer -> {
            if (offlinePlayer == null || offlinePlayer.getName() == null) return false;
            String playerNameLower = offlinePlayer.getName().toLowerCase();
            for (String name : playerNames) {
                if (playerNameLower.contains(name.toLowerCase())) {
                    return false; // Player name matches one of the specified names keep it
                }
            }
            return true; // Player name does not match any of the specified names remove it
        });
    }

    private void makeGUIButtons(PaginatedGUIMultipageListWindow gui, Player player, int pageNumber, Consumer<PaginatedReturn> consumer, Predicate<OfflinePlayer> filter) {
        OfflinePlayer[] offlinePlayers = this.plugin.getServer().getOfflinePlayers();
        List<OfflinePlayer> offlinePlayerList = new ArrayList<>(List.of(offlinePlayers));

        // Filter if not null
        if (filter != null) {
            offlinePlayerList.removeIf(filter);
        }

        if (offlinePlayerList.isEmpty()) {
            player.sendMessage(Translate.miniMessage("<red>No players found!"));
            consumer.accept(emptyPaginatedReturn());
            return;
        }

        // Remove broken players
        offlinePlayerList.removeIf(offlinePlayer -> offlinePlayer == null || offlinePlayer.getName() == null || offlinePlayer.getName().isEmpty() || offlinePlayer.getName().equals("null"));

        // Sort
        offlinePlayerList.sort((o1, o2) -> Long.compare(o2.getLastPlayed(), o1.getLastPlayed()));

        // Make into chunks keep order
        List<List<OfflinePlayer>> chunks = new ArrayList<>();
        int chunkSize = 45;
        for (int i = 0; i < offlinePlayerList.size(); i += chunkSize) {
            chunks.add(offlinePlayerList.subList(i, Math.min(i + chunkSize, offlinePlayerList.size())));
        }

        // See if page exists
        if (chunks.size() < pageNumber) {
            player.sendMessage(Translate.miniMessage("<red>Page <yellow>" + pageNumber + " <red>does not exist!"));
            consumer.accept(emptyPaginatedReturn());
            return;
        }

        PlayerUtils.getPlayerHeads(chunks.get(pageNumber), (map) -> {
            List<LastJoinGUIButton> guiButtons = new ArrayList<>();

            map.forEach((offlinePlayer, itemStack) -> {
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(Collections.singletonList(api.getTimeSinceLastLogin(offlinePlayer)));
                itemStack.setItemMeta(itemMeta);

                guiButtons.add(new LastJoinGUIButton(offlinePlayer.getLastPlayed(), -1, itemStack, (guiClickEvent) -> {
                    player.sendMessage(Translate.color("&aLast join of &6" + offlinePlayer.getName() + "&a was &6" + this.api.getTimeSinceLastLogin(offlinePlayer) + " &aago."));
                }));
            });

            sortByLeastToGreatestTime(guiButtons);

            List<CloneableGUIButton> cloneableGUIButtons = new ArrayList<>(guiButtons);
            boolean hasNextPage = chunks.size() > pageNumber + 1;
            boolean hasPreviousPage = pageNumber > 0;

            // Clear custom bottom buttons
            gui.getCustomBottomButtons().clear();

            // Add search button if there is no filter
            if (filter == null) {
                CloneableGUIButton cloneableGUIButton = new ChatResponseButton(50, InventoryUtils.createItem(Material.COMPASS, 1, Translate.miniMessage("<dark_green>Search!"), Translate.miniMessage("<blue>Name/s to search for")), Translate.miniMessage("<blue>Name/s to search for"), null, (player1, toFilter) -> {
                    player1.closeInventory();

                    openGUI(player1, toFilter);
                });

                // Add to the custom bottom buttons
                gui.getCustomBottomButtons().add(cloneableGUIButton);
            }

            consumer.accept(new PaginatedReturn(hasNextPage, hasPreviousPage, cloneableGUIButtons));
        });
    }

    private PaginatedReturn emptyPaginatedReturn() {
        return new PaginatedReturn(false, false, new ArrayList<>());
    }

    private void sortByLeastToGreatestTime(List<LastJoinGUIButton> guiButtons) {
        guiButtons.sort(((o1, o2) -> Long.compare(o2.getLastTimePlayed(), o1.getLastTimePlayed())));
    }
}

class LastJoinGUIButton extends ClickEventButton {

    private final long lastTimePlayed;
    private final ItemStack itemStack;

    public LastJoinGUIButton(Long lastTimePlayed, int slot, ItemStack itemStack, Consumer<GUIClickEvent> consumer) {
        super(slot, itemStack, consumer);
        this.lastTimePlayed = lastTimePlayed;
        this.itemStack = itemStack;
    }

    public long getLastTimePlayed() {
        return lastTimePlayed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LastJoinGUIButton that)) return false;
        if (!super.equals(o)) return false;

        if (lastTimePlayed != that.lastTimePlayed) return false;
        return itemStack != null ? itemStack.equals(that.itemStack) : that.itemStack == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (lastTimePlayed ^ (lastTimePlayed >>> 32));
        result = 31 * result + (itemStack != null ? itemStack.hashCode() : 0);
        return result;
    }
}
