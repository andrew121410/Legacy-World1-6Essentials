package com.andrew121410.mc.world16essentials.commands.home;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.sharedtabcomplete.HomeListTab;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class HomeCMD implements CommandExecutor {

    private final Map<UUID, Map<String, UnlinkedWorldLocation>> homesMap;

    private final World16Essentials plugin;
    private final API api;

    public HomeCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.homesMap = this.plugin.getMemoryHolder().getHomesMap();

        this.plugin.getCommand("home").setExecutor(this);
        this.plugin.getCommand("home").setTabCompleter(new HomeListTab(this.plugin));
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.home")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        Map<String, UnlinkedWorldLocation> homes = this.homesMap.get(player.getUniqueId());
        if (homes.isEmpty()) {
            player.sendMessage(Translate.color("&cYou don't have any homes."));
            return true;
        }

        // Easy Bedrock Support - if the player is a bedrock player, and they don't specify a home name, just show them all of their homes
//        if (args.length == 0 && this.plugin.getOtherPlugins().hasFloodgate() && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
//            handleBedrockPlayer(player);
//            return true;
//        }

        String homeName = "home";
        if (args.length == 1) {
            homeName = args[0].toLowerCase();
        }

        toHome(player, homeName);
        return true;
    }

    private void toHome(Player player, String homeName) {
        UnlinkedWorldLocation home = this.homesMap.get(player.getUniqueId()).get(homeName);
        if (home == null) {
            player.sendMessage(Translate.color("&cHome Not Found."));
            return;
        }

//        if (!home.isWorldLoaded()) {
//            player.sendMessage(Translate.colorc("&cWorld is not loaded."));
//            return;
//        }

        player.teleport(home);
        player.sendMessage(Translate.color("&6Teleporting..."));
    }

//    private void handleBedrockPlayer(Player player) {
//        Map<String, UnlinkedWorldLocation> homes = this.homesMap.get(player.getUniqueId());
//
//        if (homes.isEmpty()) {
//            player.sendMessage(Translate.miniMessage("<red>You don't have any homes."));
//            return;
//        }
//
//        SimpleForm.Builder simpleFormBuilder = SimpleForm.builder().title("Homes").content("List of your homes");
//
//        for (String homeName : homes.keySet()) {
//            simpleFormBuilder.button(homeName);
//        }
//
//        simpleFormBuilder.validResultHandler((simpleForm, simpleFormResponse) -> {
//            String homeName = simpleFormResponse.clickedButton().text();
//            toHome(player, homeName);
//        });
//
//        FloodgateApi.getInstance().sendForm(player.getUniqueId(), simpleFormBuilder.build());
//    }
}