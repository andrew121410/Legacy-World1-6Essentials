package com.andrew121410.mc.world16essentials.config;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.entity.Player;

public class MessagesUtils {

    private final World16Essentials plugin;
    private final CustomYmlManager messagesYml;

    private String prefix;
    private String welcomeBackMessage;
    private String firstJoinMessage;
    private String leaveMessage;

    public MessagesUtils(World16Essentials plugin, CustomYmlManager messagesYml) {
        this.plugin = plugin;
        this.messagesYml = messagesYml;

        // Add default messages if they don't exist.
        addDefaults();

        this.prefix = this.messagesYml.getConfig().getString("prefix");
        this.welcomeBackMessage = this.messagesYml.getConfig().getString("welcomeBackMessage");
        this.firstJoinMessage = this.messagesYml.getConfig().getString("firstJoinMessage");
        this.leaveMessage = this.messagesYml.getConfig().getString("leaveMessage");
    }

    private void addDefaults() {
        this.messagesYml.getConfig().addDefault("prefix", "[<blue>World1-6<reset>]");
        this.messagesYml.getConfig().addDefault("welcomeBackMessage", "%prefix% <gold>Welcome back, %player%!");
        this.messagesYml.getConfig().addDefault("firstJoinMessage", "%prefix% <gold>Welcome to the server, %player%!");
        this.messagesYml.getConfig().addDefault("leaveMessage", "%prefix% <gold>%player% has left the server.");

        this.messagesYml.getConfig().options().copyDefaults(true);
        this.messagesYml.saveConfig();
        this.messagesYml.reloadConfig();
    }

    public String parseMessageString(Player player, String message) {
        message = message.replaceAll("%player%", player.getName());
        message = message.replaceAll("%prefix%", this.prefix);
        return message;
    }

    public String parseMessage(Player player, String message) {
        message = parseMessageString(player, message);
        return Translate.miniMessage(message);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.messagesYml.getConfig().set("prefix", prefix);
        this.messagesYml.saveConfig();
    }

    public String getWelcomeBackMessage() {
        return welcomeBackMessage;
    }

    public void setWelcomeBackMessage(String welcomeBackMessage) {
        this.welcomeBackMessage = welcomeBackMessage;
        this.messagesYml.getConfig().set("welcomeBackMessage", welcomeBackMessage);
        this.messagesYml.saveConfig();
    }

    public String getFirstJoinMessage() {
        return firstJoinMessage;
    }

    public void setFirstJoinMessage(String firstJoinMessage) {
        this.firstJoinMessage = firstJoinMessage;
        this.messagesYml.getConfig().set("firstJoinMessage", firstJoinMessage);
        this.messagesYml.saveConfig();
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
        this.messagesYml.getConfig().set("leaveMessage", leaveMessage);
        this.messagesYml.saveConfig();
    }
}