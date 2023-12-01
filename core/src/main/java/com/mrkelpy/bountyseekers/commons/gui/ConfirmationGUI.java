package com.mrkelpy.bountyseekers.commons.gui;

import com.mrkelpy.bountyseekers.commons.utils.GUIUtils;
import com.mrkelpy.bountyseekers.commons.utils.PluginConstants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * This abstract class adds confirm/cancel buttons to a GUI.
 */
public abstract class ConfirmationGUI implements Listener {

    // This inventory instance should be used by any classes inheriting from the class.
    protected final Inventory inventory;
    protected final int storageSlots;
    protected final UUID userUUID;

    /**
     * Main constructor for the ConfirmationGUI class. This constructor automatically
     * adds the extra space needed for the confirm/cancel buttons to the inventory.
     *
     * @param title         The title of the inventory.
     * @param inventorySize The size of the inventory.
     * @param userUUID      The UUID of the player who is using this instance of the inventory
     */
    public ConfirmationGUI(String title, int inventorySize, UUID userUUID) {
        this.storageSlots = inventorySize - 1;
        this.inventory = Bukkit.createInventory(null, inventorySize + 9, title);
        this.userUUID = userUUID;
        this.addConfirmationButtons();
        this.registerListeners();
    }

    /**
     * This method is called when the player clicks the confirm button.
     */
    public abstract void onConfirm(Player player);

    /**
     * This method is called when the player clicks the cancel button.
     */
    public abstract void onCancel(Player player);

    /**
     * Prevents an item from being clicked on, to prevent exploits, and checks if the slot clicked
     * was any of the paging button ones. If so, handle them accordingly.
     * <br>
     * This method can, and should be overriden to process clicks on GUIs that extend PagedGUI, but the super
     * should be called first.
     *
     * @param event InventoryClickEvent
     */
    @EventHandler
    public void onItemClick(InventoryClickEvent event) {

        // Checks if the inventory being used is the one that this class is using.
        boolean instanceEvaluation = event.getInventory().equals(this.inventory) && event.getWhoClicked().getUniqueId().equals(this.userUUID);
        if (!instanceEvaluation) return;

        // Gets the player who clicked the inventory.
        Player player = (Player) event.getWhoClicked();

        // Checks if a shift click was performed on the player inventory whilst the GUI is already full, thus sending the item to the unused 7 slots between the buttons.
        if (Collections.frequency(Arrays.asList(this.inventory.getContents()), null) == 7 && event.isShiftClick() && event.getRawSlot() > this.inventory.getSize()) {
            event.setCancelled(true);
        }

        // Prevents the player from shift clicking on the Confirm/Cancel buttons.
        if (this.isButtonSlot(event.getRawSlot()) && event.isShiftClick()) {
            event.setCancelled(true);
            return;
        }

        // Checks if a click was performed in the 7 unused slots between the buttons.
        if (event.getRawSlot() > this.storageSlots + 1 && event.getRawSlot() < this.storageSlots + 9) {
            event.setCancelled(true);
            return;
        }

        // Prevents the player from clicking on the Confirm/Cancel buttons with an item on their cursor
        if (this.isButtonSlot(event.getRawSlot()) && player.getItemOnCursor().getType() != Material.AIR) {
            event.setCancelled(true);
            return;
        }

        // Prevents any SHIFT_LEFT clicks on reserved storage slots. This avoids accidental cancel clicks.
        if (event.getRawSlot() >= this.storageSlots && event.getRawSlot() <= this.storageSlots + 9 && event.getClick() == ClickType.SHIFT_LEFT) {
            event.setCancelled(true);
            return;
        }

        if (event.getRawSlot() == this.storageSlots + 1) this.onCancel(player);
        if (event.getRawSlot() == this.storageSlots + 9) this.onConfirm(player);
    }

    /**
     * Checks if the slot clicked was any of the paging button ones.
     * @param slot The slot clicked.
     * @return True if the slot clicked was any of the paging button ones, false otherwise.
     */
    private boolean isButtonSlot(int slot) {
        return slot == this.storageSlots + 1 || slot == this.storageSlots + 9;
    }

    /**
     * Prevents an item from being dragged on, to prevent exploits.
     * @param event InventoryDragEvent
     */
    @EventHandler
    public void onItemDrag(InventoryDragEvent event) {

        if (event.getInventory().equals(this.inventory) && event.getWhoClicked().getUniqueId().equals(this.userUUID) &&
                event.getNewItems().keySet().stream().anyMatch(slot -> slot > this.storageSlots))
            event.setCancelled(true);
    }

    /**
     * Unregisters all event listeners present in an instance of this GUI to save resources.
     * @param event InventoryCloseEvent
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (event.getInventory().equals(this.inventory) && event.getPlayer().getUniqueId().equals(this.userUUID))
            HandlerList.unregisterAll(this);
    }

    /**
     * Adds the confirm and cancel buttons to the inventory.
     */
    private void addConfirmationButtons() {

        ItemStack redWool = Material.getMaterial("RED_WOOL") != null ?
                GUIUtils.createItemPlaceholder(Material.getMaterial("RED_WOOL"), "§cCancel") :
                GUIUtils.createItemPlaceholder(Material.getMaterial("WOOL"), "§cCancel", null, (short) 14);

        ItemStack limeWool = Material.getMaterial("LIME_WOOL") != null ?
                GUIUtils.createItemPlaceholder(Material.getMaterial("LIME_WOOL"), "§eConfirm") :
                GUIUtils.createItemPlaceholder(Material.getMaterial("WOOL"), "§eConfirm", null, (short) 5);

        this.inventory.setItem(this.storageSlots + 1, redWool);

        this.inventory.setItem(this.storageSlots + 9, limeWool);
    }

    /**
     * Registers all event listeners used by an instance of this GUI.
     */
    private void registerListeners() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(PluginConstants.PLUGIN_NAME);
        Bukkit.getPluginManager().registerEvents(this, Objects.requireNonNull(plugin));
    }

}

