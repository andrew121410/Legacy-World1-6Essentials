package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16essentials.config.ConfigUtils;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RamCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;
    private final ConfigUtils configUtils;

    private String cpuModelCache = null;
    private String operatingSystemCache = null;
    private String kernelNumberCache = null;
    private String javaVersionCache = null;

    public RamCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.configUtils = this.plugin.getApi().getConfigUtils();

        this.plugin.getCommand("ram").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.ram")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        if (args.length == 0) {
            sendInfo(sender);
        }

        return true;
    }

    private void sendInfo(CommandSender sender) {
        if (this.cpuModelCache == null) {
            try (Stream<String> stream = Files.lines(Paths.get("/proc/cpuinfo"))) {
                this.cpuModelCache = stream.filter(line -> line.startsWith("model name"))
                        .map(line -> line.replaceAll(".*: ", ""))
                        .findFirst().orElse("");
            } catch (Exception ignored) {
                this.cpuModelCache = null;
            }
        }
        if (this.cpuModelCache != null) {
            sender.sendMessage(Translate.color("&6CPU: &7" + this.cpuModelCache));
        }

        if (this.kernelNumberCache == null) {
            try {
                this.kernelNumberCache = System.getProperty("os.version");
            } catch (Exception ignored) {
                this.kernelNumberCache = null;
            }
        }

        if (this.operatingSystemCache == null) {
            try {
                this.operatingSystemCache = System.getProperty("os.name");
            } catch (Exception ignored) {
                this.operatingSystemCache = null;
            }
        }
        if (this.operatingSystemCache != null && this.kernelNumberCache != null) {
            sender.sendMessage(Translate.color("&6OS: &7" + this.operatingSystemCache + " &6Kernel: &7" + this.kernelNumberCache));
        } else if (this.operatingSystemCache != null) {
            sender.sendMessage(Translate.color("&6OS: &7" + this.operatingSystemCache));
        } else if (this.kernelNumberCache != null) {
            sender.sendMessage(Translate.color("&6Kernel: &7" + this.kernelNumberCache));
        }

        if (this.javaVersionCache == null) {
            try {
                this.javaVersionCache = System.getProperty("java.version");
            } catch (Exception ignored) {
                this.javaVersionCache = null;
            }
        }
        if (this.javaVersionCache != null) {
            sender.sendMessage(Translate.color("&6Java: &7" + this.javaVersionCache));
        }

        // Disk Usage
        sendDiskUsage(sender);

        // RAM Usage
        long maxMemory = (Runtime.getRuntime().maxMemory() / 1024 / 1024);
        long allocatedMemory = (Runtime.getRuntime().totalMemory() / 1024 / 1024);
        long allocatedPercent = (allocatedMemory * 100) / maxMemory;
        long freeMemory = (Runtime.getRuntime().freeMemory() / 1024 / 1024);
        long freePercent = (freeMemory * 100) / maxMemory;
        long usedMemory = allocatedMemory - freeMemory;
        long usedPercent = (usedMemory * 100) / maxMemory;

        sender.sendMessage(Translate.color("&6Maximum memory: &c" + maxMemory + " MB."));
        sender.sendMessage(Translate.color("&6Allocated memory: &c" + allocatedMemory + " MB." + " &6(" + allocatedPercent + "%)"));
        sender.sendMessage(Translate.color("&6Used memory: &c" + usedMemory + " MB." + " &6(" + usedPercent + "%)"));
        sender.sendMessage(Translate.color("&6Free memory: &c" + freeMemory + " MB." + " &6(" + freePercent + "%)"));
    }

    private void sendDiskUsage(CommandSender sender) {
        // If isMoreAccurateDiskInfo is true then we will use the df and du command to get the disk info.
        if (configUtils.isMoreAccurateDiskInfo()) {
            if (doesDfCommandExist()) {
                diskInfoFromDfCommand(sender);
            }
            if (doesDuCommandExist()) {
                mcServerDiskInfoFromDuCommand(sender);
            }
            return;
        }

        File file = new File("./");
        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        long usedPercentSpace = (usedSpace * 100) / totalSpace;
        String usedSpaceInGB = String.valueOf(usedSpace / 1024 / 1024 / 1024);
        String totalSpaceInGB = String.valueOf(totalSpace / 1024 / 1024 / 1024);

        // Used percent color.
        String usedPercentColor = "";
        if (usedPercentSpace >= 90) {
            usedPercentColor = "&c";
        } else if (usedPercentSpace >= 80) {
            usedPercentColor = "&e";
        } else {
            usedPercentColor = "&a";
        }

        sender.sendMessage(Translate.color("&6Disk usage: " + usedPercentColor + usedPercentSpace + "% &e(&6" + usedSpaceInGB + "&e/&6" + totalSpaceInGB + " GB&e)"));
    }

    private void diskInfoFromDfCommand(CommandSender sender) {
        try {
            // Execute the df -h command
            Process process = Runtime.getRuntime().exec(new String[]{"df", "-h", "/"});

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            reader.readLine(); // Skip the first line

            // Process the output
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                String totalSpace = parts[1];
                String usedSpace = parts[2];
                String availableSpace = parts[3];
                String usedPercent = parts[4];

                // Used percent color.
                String usedPercentColor = "";
                if (usedPercent.contains("%")) {
                    usedPercent = usedPercent.replace("%", "");
                }
                if (Integer.parseInt(usedPercent) >= 90) {
                    usedPercentColor = "&c";
                } else if (Integer.parseInt(usedPercent) >= 80) {
                    usedPercentColor = "&e";
                } else {
                    usedPercentColor = "&a";
                }

                sender.sendMessage(Translate.color("&6Disk usage: " + usedPercentColor + usedPercent + "% &e(&6" + usedSpace + "&e/&6" + totalSpace + "&e) (&6" + availableSpace + " available to use&e)"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mcServerDiskInfoFromDuCommand(CommandSender sender) {
        try {
            // Execute the du -sh command
            ProcessBuilder processBuilder = new ProcessBuilder("du", "-sh", "./");

            // Get the output of the command
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Process the output
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                String usedSpace = parts[0];

                sender.sendMessage(Translate.color("&6MC Server disk usage: &e(&6" + usedSpace + " used&e)"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean doesDfCommandExist() {
        return new File("/bin/df").exists();
    }

    private boolean doesDuCommandExist() {
        return new File("/bin/du").exists();
    }
}
