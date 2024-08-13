package com.andrew121410.mc.world16utils.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// https://gist.github.com/graywolf336/8153678
public class BukkitSerialization {
    public static String itemStackToBase64(ItemStack itemStack) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the itemStack
            dataOutput.writeObject(itemStack);

            // Serialize that itemStack
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }

    public static ItemStack base64ToItemStack(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack itemStack = (ItemStack) dataInput.readObject();

            dataInput.close();
            return itemStack;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack[] base64ToItemStackArray(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    // Turns the inventory contents and armor contents into base64 and then returns array of the 2 base64 strings
    public static String[] turnInventoryIntoBase64s(Player player) {
        List<ItemStack> inventoryContents = new ArrayList<>();
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                itemStack = new ItemStack(org.bukkit.Material.AIR);
            }
            inventoryContents.add(itemStack);
        }

        List<ItemStack> armorContents = new ArrayList<>();
        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                itemStack = new ItemStack(org.bukkit.Material.AIR);
            }
            armorContents.add(itemStack);
        }

        String inventoryBase64 = BukkitSerialization.itemStackArrayToBase64(inventoryContents.toArray(new ItemStack[0]));
        String armorBase64 = BukkitSerialization.itemStackArrayToBase64(armorContents.toArray(new ItemStack[0]));
        return new String[]{inventoryBase64, armorBase64};
    }

    // Takes in the 2 base64 strings and turns them into the inventory contents and armor contents
    public static void giveFromBase64s(Player player, String[] base64s) {
        ItemStack[] inventoryItemStacks;
        ItemStack[] armorItemStacks; // Not in use yet.
        try {
            inventoryItemStacks = BukkitSerialization.base64ToItemStackArray(base64s[0]);
            armorItemStacks = BukkitSerialization.base64ToItemStackArray(base64s[1]); // Not in use yet.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack itemStack = InventoryUtils.getItemInItemStackArrayIfExist(inventoryItemStacks, i);
            if (itemStack == null || itemStack.getType() == Material.AIR) continue;
            if (player.getInventory().getItem(i) != null) {
                player.getInventory().addItem(itemStack);
            } else {
                player.getInventory().setItem(i, itemStack);
            }
        }
    }
}
