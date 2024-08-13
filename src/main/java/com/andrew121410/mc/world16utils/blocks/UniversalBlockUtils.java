package com.andrew121410.mc.world16utils.blocks;

import com.andrew121410.mc.world16utils.utils.Utils;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_12_R1.TileEntitySign;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UniversalBlockUtils {

    public static boolean isStairs(Block block) {
        return block.getType().toString().contains("STAIRS");
    }

    public static boolean isDoor(Block block) {
        new UnsupportedOperationException("Not implemented yet.");
        return false;
    }

    public static Block ifDoorThenGetBlockUnderTheDoorIfNotThanReturn(Block block) {
        new UnsupportedOperationException("Not implemented yet.");
        return null;
    }

    public static Block getDoorBaseBlock(Block block) {
        new UnsupportedOperationException("Not implemented yet.");
        return null;
    }

    public static boolean isOpenable(Block block) {
        new UnsupportedOperationException("Not implemented yet.");
        return false;
    }

    public static boolean doOpenable(Block block, boolean value) {
        new UnsupportedOperationException("Not implemented yet.");
        return false;
    }

    public static Sign isSign(Block block) {
        BlockState blockState = block.getState();
        if (blockState instanceof Sign) {
            return (Sign) blockState;
        }
        return null;
    }

    public static String signCenterText(String text) {
        return Utils.centerText(text, 16);
    }

    public static boolean isFarmLand(Material material) {
        return material == Material.SOIL;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius, boolean doY) {
        List<Block> blocks = new ArrayList<>();

        if (doY) {
            for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
                for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                    for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                        blocks.add(location.getWorld().getBlockAt(x, y, z));
                    }
                }
            }
        } else {
            for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, location.getBlockY(), z));
                }
            }
        }

        return blocks;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        return getNearbyBlocks(location, radius, true);
    }

    public static void editSign(Player player, Sign sign) {
        Location location = sign.getLocation();
        TileEntitySign tileEntitySign = (TileEntitySign) ((CraftWorld) location.getWorld()).getHandle().getTileEntity(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        tileEntitySign.isEditable = true;
        tileEntitySign.a(((CraftPlayer) player).getHandle());
        PacketPlayOutOpenSignEditor packetPlayOutOpenSignEditor = new PacketPlayOutOpenSignEditor(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutOpenSignEditor);
    }
}
