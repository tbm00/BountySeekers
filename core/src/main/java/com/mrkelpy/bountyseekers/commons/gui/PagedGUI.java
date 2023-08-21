package com.mrkelpy.bountyseekers.commons.gui;

import com.mrkelpy.bountyseekers.commons.utils.GUIUtils;
import com.mrkelpy.bountyseekers.commons.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * This abstract class handles the pagination for any GUI that extends it.
 */
public abstract class PagedGUI implements Listener {

    // This inventory instance should be used by any classes inheriting from the class.
    protected final Inventory inventory;
    protected final int storageSlots;
    private List<ItemStack> itemList;
    private UUID userUUID;
    private int page;

    /**
     * Main constructor for the PagedGUI. Create the inventory and set the items inside it.
     *
     * @param inventoryName The title of the inventory.
     * @param inventorySize The base inventory size, where items can be stored.
     * @param userUUID      The UUID of the player who is using this instance of the GUI
     */
    public PagedGUI(String inventoryName, int inventorySize, List<ItemStack> items, UUID userUUID) {
        this(inventoryName, inventorySize, userUUID);
        this.itemList = items;
        this.userUUID = userUUID;
        this.sendToPage(this.page);
        this.registerListeners();
    }

    /**
     * This constructor allows one to set the item list manually after the GUI has been created.
     *
     * @param inventoryName The title of the inventory.
     * @param inventorySize The base inventory size, where items can be stored.
     * @param userUUID      The UUID of the player who is using this instance of the GUI
     */
    public PagedGUI(String inventoryName, int inventorySize, UUID userUUID) {
        this.inventory = Bukkit.createInventory(null, inventorySize + 9, inventoryName);
        this.storageSlots = inventorySize - 1;
        this.userUUID = userUUID;
        this.page = 1;
        this.registerListeners();
    }

    /**
     * Reloads the inventory, re-sending the GUI to the current page.
     */
    public void reload() {
        this.sendToPage(this.page);
    }

    public List<ItemStack> getItems() {
        return this.itemList;
    }

    public void setItems(List<ItemStack> itemList) {
        this.itemList = itemList;
    }

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

        // Checks if a shift click was performed on the player inventory whilst the GUI is already full, thus sending the item to the unused 7 slots between the buttons.
        if (instanceEvaluation && Collections.frequency(Arrays.asList(this.inventory.getContents()), null) == 7 && event.isShiftClick() && event.getRawSlot() >= this.inventory.getSize()) {
            event.setCancelled(true);
        }

        // Checks if the click was performed on the GUI, so the button clicks can be processed.
        if (instanceEvaluation && event.getRawSlot() > this.storageSlots && event.getRawSlot() <= this.inventory.getSize())
            event.setCancelled(true);
        else return;

        if (event.isShiftClick()) event.setCancelled(true);
        if (event.getSlot() == this.storageSlots + 9) this.sendToPage(this.page + 1);
        if (event.getSlot() == this.storageSlots + 1) this.sendToPage(this.page - 1);
        if (event.getSlot() == this.storageSlots + 5) this.goBack();
    }

    /**
     * Prevents an item from being dragged on, to prevent exploits.
     *
     * @param event InventoryDragEvent
     */
    @EventHandler
    public void onItemDrag(InventoryDragEvent event) {
        if (event.getInventory().equals(this.inventory) && event.getWhoClicked().getUniqueId().equals(this.userUUID))
            event.setCancelled(true);
    }

    /**
     * Unregisters all event listeners present in an instance of this GUI to save resources.
     *
     * @param event InventoryCloseEvent
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(this.inventory) && event.getPlayer().getUniqueId().equals(this.userUUID))
            HandlerList.unregisterAll(this);
    }

    /**
     * This method sends a GUI to a specific page in the PagedGUI.
     *
     * @param page The page to send the GUI to.
     */
    protected void sendToPage(int page) {

        if (page <= 0) return; // Paging starts at 1.

        // These indexes represent the first and last items' location from inside the GUI page to the itemlist.
        int firstItemIndex = (page - 1) * (this.storageSlots + 1);  // There's a slight offset equal to the page for the first index per page, so fix it.
        int lastItemIndex = firstItemIndex + this.storageSlots;

        // Ignores the sendToPage call if list of items doesn't account for this many pages.
        if (this.itemList.size() - 1 < firstItemIndex && !this.itemList.isEmpty()) return;

        // Creates a list of items to display on the page, starting from the first item and ending at the last item indexes on the item list.
        List<ItemStack> itemsForPage = new ArrayList<>();
        for (int i = firstItemIndex; i <= lastItemIndex; i++) {
            if (i - firstItemIndex >= this.storageSlots + 1 || this.itemList.size() <= i) break;
            itemsForPage.add(this.itemList.get(i));
        }

        this.page = !this.itemList.isEmpty() ? page : 1;
        this.setPageItems(itemsForPage);
        this.setPagingButtons();
    }

    /**
     * This method is called when the "Go Back" button is pressed.
     */
    protected abstract void goBack();

    /**
     * @return The current page of the PagedGUI.
     */
    protected int getPage() {
        return this.page;
    }

    /**
     * Evaluates whether there is a previous page in the GUI or not.
     *
     * @return boolean
     */
    private boolean hasPreviousPage() {
        return page > 1;
    }

    /**
     * Evaluates whether there is a next page in the GUI or not.
     *
     * @return boolean
     */
    private boolean hasNextPage() {
        return this.itemList.size() > (this.page) * (this.storageSlots + 1);
    }

    /**
     * Sets the next/previous page button items in the GUI. The items set for the buttons
     * will depend on whether there is a previous or next page.
     * <br>
     * <ul>
     *     <li>GREEN Dye - There is a next/previous page.</li>
     *     <li>RED Dye - There's no next/previous page.</li>
     * </ul>
     */
    private void setPagingButtons() {

        ItemStack limeDye = Material.getMaterial("LIME_DYE") != null ?
                GUIUtils.createItemPlaceholder(Material.getMaterial("LIME_DYE"), "tmp") :
                GUIUtils.createItemPlaceholder(Material.getMaterial("INK_SACK"), "tmp", null, (short) 10);

        ItemStack grayDye = Material.getMaterial("GRAY_DYE") != null ?
                GUIUtils.createItemPlaceholder(Material.getMaterial("GRAY_DYE"), "tmp") :
                GUIUtils.createItemPlaceholder(Material.getMaterial("INK_SACK"), "tmp", null, (short) 8);

        ItemStack orangeDye = Material.getMaterial("ORANGE_DYE") != null ?
                GUIUtils.createItemPlaceholder(Material.getMaterial("ORANGE_DYE"), "§bGo Back") :
                GUIUtils.createItemPlaceholder(Material.getMaterial("INK_SACK"), "§bGo Back", null, (short) 14);

        this.inventory.setItem(this.storageSlots + 1, this.hasPreviousPage() ? ItemStackUtils.getRenamed(limeDye, "§bPrevious Page") : ItemStackUtils.getRenamed(grayDye, "§bPrevious Page"));
        this.inventory.setItem(this.storageSlots + 5, orangeDye);
        this.inventory.setItem(this.storageSlots + 9, this.hasNextPage() ? ItemStackUtils.getRenamed(limeDye, "§bNext Page") : ItemStackUtils.getRenamed(grayDye, "§bNext Page"));
    }

    /**
     * Sets the items in the given list of ItemStacks into the inventory page.
     *
     * @param items The list of items to be set
     */
    private void setPageItems(List<ItemStack> items) {

        this.inventory.setContents(new ItemStack[this.inventory.getSize()]);  // Clears the inventory.

        for (int i = 0; i < items.size(); i++) {
            this.inventory.setItem(i, items.get(i));
        }
    }

    /**
     * Registers all event listeners used by an instance of this GUI.
     */
    private void registerListeners() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("BountySeekers");
        assert plugin != null;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}

