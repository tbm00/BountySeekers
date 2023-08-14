package com.mrkelpy.bountyseekers.commons.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStackUtils {

    /**
     * Iterates over every item in the given List, tracks the amount of items in each,
     * and generates a compressed list of ItemStacks.
     *
     * @param list The list to compress.
     * @return The compressed list.
     */
    public static List<ItemStack> compress(List<ItemStack> list) {
        Map<ItemStack, Integer> generationMap = new HashMap<>();  // The map holding the stacks and their amount.

        // Iterate over every item in the list and count the amount of each.
        for (ItemStack item : list) {

            if (item == null || item.getType() == Material.AIR) continue;

            // Creates a pivot ItemStack to use for comparison.
            ItemStack pivot = ItemStackUtils.makePivot(item);

            if (generationMap.containsKey(pivot))
                generationMap.put(pivot, generationMap.get(pivot) + item.getAmount());

            else {
                generationMap.put(pivot, item.getAmount());
            }
        }

        // Generates the compressed ItemStacks given the generationMap.
        List<ItemStack> compressedList = new ArrayList<>();
        for (ItemStack item : generationMap.keySet()) {

            do {
                int amount = item.getMaxStackSize();  // Defaults the itemstack amount to the max stack size.

                // If the amount left is greater than the stack size, diminish the amount left by the stack size.
                if (generationMap.get(item) > amount)
                    generationMap.put(item, generationMap.get(item) - amount);

                else {
                    // If not, change the amount to what's left, and do the same.
                    amount = generationMap.get(item);
                    generationMap.put(item, generationMap.get(item) - amount);
                }

                // Create a clone of the itemstack, change the item count, and add it to the compressed list.
                ItemStack itemClone = item.clone();
                itemClone.setAmount(amount);
                compressedList.add(itemClone);

            }
            // Keep repeating this until the amount of items left is 0.
            while (generationMap.get(item) != 0);
        }

        return compressedList;
    }

    /**
     * Iterates over the given list and checks if there's any itemStack inside with less
     * than its max stack size that the given itemStack will be compressed into.
     *
     * @param stack The itemStack to check.
     * @param list  The list to check in.
     * @return True if the itemStack can be compressed, false otherwise.
     */
    public static boolean willCompress(ItemStack stack, List<ItemStack> list) {

        for (ItemStack item : list) {
            if (item.isSimilar(stack) && item.getAmount() < item.getMaxStackSize())
                return true;
        }

        return false;
    }

    /**
     * Iterates over the given list and adds every ItemStack to a new list, ignoring the addition
     * of an ItemStack if it is already inside the list.
     * @param list The list to remove the duplicates from
     * @return The list without duplicates
     */
    public static List<ItemStack> removeDuplicates(List<ItemStack> list) {
        List<ItemStack> newList = new ArrayList<>();

        for (ItemStack item : list) {
            if (!newList.contains(item))
                newList.add(item);
        }

        return newList;
    }

    /**
     * Filters out the slots that are not storage slots in the inventory. (The hotbar + the 27 storage ones)
     *
     * @param player The player to check.
     * @return The filtered ItemStack[].
     */
    public static ItemStack[] getStorageContents(Player player) {

        ItemStack[] contents = player.getInventory().getContents();
        List<ItemStack> storageContents = new ArrayList<>();

        for (int i = 0; i < contents.length; i++)
            if (i < InventoryType.CHEST.getDefaultSize() + 9) storageContents.add(contents[i]);

        return storageContents.toArray(new ItemStack[0]);
    }

    /**
     * Returns the ItemStack, with a custom name.
     *
     * @param item The itemStack to return named.
     * @param name The name to give it.
     * @return The named itemStack.
     */
    public static ItemStack getRenamed(ItemStack item, String name) {
        ItemStack renamed = item.clone();
        ItemMeta meta = renamed.getItemMeta();
        meta.setDisplayName(name);
        renamed.setItemMeta(meta);
        return renamed;
    }

    /**
     * Creates a new ItemStack with the given material and amount set to 1.
     * @param item The material to create the itemStack with.
     * @return The pivot item.
     */
    public static ItemStack makePivot(ItemStack item) {
        ItemStack pivot = item.clone();
        pivot.setAmount(1);
        return pivot;
    }
}

