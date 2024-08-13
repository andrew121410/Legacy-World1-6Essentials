package com.andrew121410.mc.world16essentials.commands.warp;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.sharedtabcomplete.WarpTab;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class WarpCMD implements CommandExecutor {

    private final Map<String, UnlinkedWorldLocation> warpsMap;

    private final World16Essentials plugin;
    private final API api;

    public WarpCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.warpsMap = this.plugin.getMemoryHolder().getWarpsMap();

        this.plugin.getCommand("warp").setExecutor(this);
        this.plugin.getCommand("warp").setTabCompleter(new WarpTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.warp")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            String name = args[0].toLowerCase();
            toWarp(player, null, name);
            return true;
        } else if (args.length == 2) { // /warp <player> <name>
            if (!sender.hasPermission("world16.warp.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Translate.color("&cThat player is not online."));
                return true;
            }

            String name = args[1].toLowerCase();
            toWarp(target, sender, name);
            return true;
        } else {
            // Easy Bedrock Support - if the player is a bedrock player, and they don't specify a warp name, just show them all the warps
//            if (sender instanceof Player player) {
//                if (args.length == 0 && this.plugin.getOtherPlugins().hasFloodgate() && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
//                    handleBedrockPlayer(player);
//                    return true;
//                }
//            }

            sender.sendMessage(Translate.color("&c/warp <name>"));
            sender.sendMessage(Translate.color("&c/warp <player> <name>"));
        }
        return true;
    }

    private void toWarp(Player target, CommandSender sender, String warp) {
        UnlinkedWorldLocation warpLocation = this.warpsMap.get(warp);

        if (warpLocation == null) {
            if (sender == null) {
                target.sendMessage(Translate.color("&cThat's not a warp."));
            } else {
                sender.sendMessage(Translate.color("&cThat's not a warp."));
            }
            return;
        }

        // Check if the player target has permission to use the warp, if run by console, skip this check.
        if (sender == null && !target.hasPermission("world16.warp." + warp)) {
            target.sendMessage(Translate.color("&cYou don't have permission to use this warp."));
            return;
        }

        // World may not be loaded, so we need to check.
        if (!warpLocation.isWorldLoaded()) {
            if (sender == null) {
                target.sendMessage(Translate.color("&cWorld is not loaded."));
            } else {
                sender.sendMessage(Translate.color("&cWorld is not loaded."));
            }
            return;
        }

        target.teleport(warpLocation);
        target.sendMessage(Translate.color("&6Teleporting..."));
        if (sender != null) {
            sender.sendMessage(Translate.color("&6Successfully teleported &e" + target.getName() + " &6to warp &e" + warp));
        }
    }

//    private void handleBedrockPlayer(Player player) {
//        SimpleForm.Builder simpleFormBuilder = SimpleForm.builder().title("Warps").content("List of all warps");
//
//        for (String warpName : this.warpsMap.keySet()) {
//             Check if the player has permission to use this warp, if not, skip it.
//            if (!player.hasPermission("world16.warp." + warpName)) continue;
//
//            simpleFormBuilder.button(warpName);
//        }
//
//        simpleFormBuilder.validResultHandler((simpleForm, simpleFormResponse) -> {
//            String warpName = simpleFormResponse.clickedButton().text();
//            toWarp(player, null, warpName);
//        });
//
//        SimpleForm simpleForm = simpleFormBuilder.build();
//
//        if (simpleForm.buttons().isEmpty()) {
//            player.sendMessage(Translate.miniMessage("<red>There are no warps."));
//            return;
//        }
//
//         Have to call simpleFormBuilder.build() again because if not a NoClassDefFoundError would be thrown?
//         Caused by java.lang.ClassNotFoundException: org.geysermc.cumulus.form.Form
//         Broken classpath?
//        FloodgateApi.getInstance().sendForm(player.getUniqueId(), simpleFormBuilder.build());
//}
}