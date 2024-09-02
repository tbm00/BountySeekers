package com.mrkelpy.bountyseekers.commons.gui;

import com.mrkelpy.bountyseekers.commons.configuration.ConfigurableTextHandler;
import com.mrkelpy.bountyseekers.commons.configuration.InternalConfigs;
import com.mrkelpy.bountyseekers.commons.utils.ChatUtils;
import com.mrkelpy.bountyseekers.commons.utils.FileUtils;
import com.mrkelpy.bountyseekers.commons.utils.ItemStackUtils;
import com.mrkelpy.bountyseekers.commons.utils.SerializationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class creates a GUI capable of mediating the rewards that can go
 * into a bounty.
 */
public class RewardFilterGUI extends ConfirmationGUI {

    private final Player user;
    private final Inventory inventoryBackup;

    /**
     * Main constructor for the RewardFilterGUI class.
     * @param user The player who is setting the filter
     */
    public RewardFilterGUI(Player user) {
        super(ConfigurableTextHandler.INSTANCE.getValue("bounty.reward.filter.title"), 9*5, user.getUniqueId());
        this.user = user;

        this.inventoryBackup = Bukkit.createInventory(null, InventoryType.CHEST.getDefaultSize() + 9);
        this.inventoryBackup.setContents(ItemStackUtils.getStorageContents(this.user));

        ItemStack[] items = FileUtils.getRewardFilter();
        if (items == null) return;

        Arrays.stream(items).filter(item -> item != null && item.getType() != Material.AIR).forEach(this.inventory::addItem);
    }

    /**
     * Open the GUI for the player.
     */
    public void openInventory() {
        this.user.openInventory(this.inventory);
    }

    /**
     * Adds all the filter items into the InternalConfigs file as a base64 string.
     * @param player The player that pressed the confirm button.
     */
    @Override
    public void onConfirm(Player player) {

        // Unregisters the listeners and closes the inventory.
        HandlerList.unregisterAll(this);

        if (this.user.getOpenInventory().getType() == InventoryType.CHEST)
            this.user.closeInventory();

        // Converts every item in the inventory into pivot items.
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < this.storageSlots + 1; i++) {

            ItemStack item = this.inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;
            items.add(ItemStackUtils.makePivot(item));
        }

        // Serializes the items into a base64 string and updates the config.
        items = ItemStackUtils.removeDuplicates(items);
        String serialized = !items.isEmpty() ? SerializationUtils.itemStackArrayToBase64(items.toArray(new ItemStack[0])) : null;

        InternalConfigs.INSTANCE.getConfig().set("reward-filter", serialized);
        InternalConfigs.INSTANCE.save();

        ChatUtils.sendMessage(player, "Updated the reward filters!");
    }

    /**
     * Returns the user's inventory as of before the GUI was opened.
     *
     * @param player The player that pressed the cancel button.
     */
    @Override
    public void onCancel(Player player) {

        // Unregisters the event handlers and closes the inventory so there's no recursion
        HandlerList.unregisterAll(this);
        boolean isKeepInventoryFalse = player.getWorld().getGameRuleValue("keepInventory").equals("false");

        if (this.user.getOpenInventory().getType() == InventoryType.CHEST)
            this.user.closeInventory();

        // Drops all the items inside the GUI at the player's location if they die with the GUI open, or they're not online.
        if ((player.getHealth() == 0 && isKeepInventoryFalse) || Bukkit.getServer().getPlayerExact(player.getName()) == null) {

            for (int i = 0; i <= this.storageSlots; i++) {
                ItemStack item = this.inventory.getItem(i);

                if (item != null && item.getType() != Material.AIR)
                    ItemStackUtils.scheduleItemDrop(player, item, 1L);
            }
            return;
        }

        // If that doesn't happen, and there's a normal cancellation, return the items to the player.
        for (int i = 0; i < player.getPlayer().getInventory().getStorageContents().length; i++) {

            if (this.inventory.getItem(i) == null) continue;
            player.getPlayer().getInventory().setItem(i, this.inventory.getItem(i));
        }

        player.updateInventory();
    }

    /**
     * Stops a player from being able to drop items from this GUI.
     * @param event The event that was fired.
     */
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        if (event.getPlayer().getUniqueId().equals(this.userUUID)) {

            event.setCancelled(true);
            ItemStackUtils.scheduleRunnable(() -> event.getPlayer().updateInventory(), 1L);
        }
    }

    /**
     * Count closing the inventory as a cancel.
     *
     * @param event InventoryCloseEvent
     */
    @Override
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (event.getInventory().equals(this.inventory) && this.userUUID.equals(event.getPlayer().getUniqueId()))
            ItemStackUtils.scheduleRunnable(() -> this.onCancel((Player) event.getPlayer()), 1L);
    }

    /**
     * While this GUI is opened, the user can't pick up any items, to prevent losses.
     *
     * @param event The event that is being handled.
     */
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {

        if (event.getPlayer() == this.user.getPlayer())
            event.setCancelled(true);
    }

}

