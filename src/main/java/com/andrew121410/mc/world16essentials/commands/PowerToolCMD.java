package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.PowerToolObject;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class PowerToolCMD implements CommandExecutor {

    private final Map<UUID, PowerToolObject> powerToolMap;

    private final World16Essentials plugin;
    private final API api;

    public PowerToolCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.powerToolMap = this.plugin.getMemoryHolder().getPowerToolMap();

        this.plugin.getCommand("powertool").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.powertool")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        PowerToolObject powerToolObject = this.powerToolMap.get(player.getUniqueId());
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (args.length == 0) {
            powerToolObject.deletePowerTool(itemInMainHand.getType());
            player.sendMessage(Translate.miniMessage("<yellow>Removed the power tool from the item in your hand."));
            return true;
        } else {
            String[] command = Arrays.copyOfRange(args, 0, args.length);
            String realCommand = String.join(" ", command);
            String unmodifiedCommand = realCommand;

            if (realCommand.startsWith("/")) {
                realCommand = realCommand.substring(1);
            }

            powerToolObject.registerPowerTool(itemInMainHand.getType(), realCommand);
            player.sendMessage(Translate.miniMessage("<gold>When you left click with this item it will run the command: <white>" + unmodifiedCommand + "<green>."));
            return true;
        }
    }
}
