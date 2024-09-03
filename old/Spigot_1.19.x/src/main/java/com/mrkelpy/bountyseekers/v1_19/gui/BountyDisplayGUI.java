package com.mrkelpy.bountyseekers.v1_19.gui;

import com.mrkelpy.bountyseekers.commons.configuration.ConfigurableTextHandler;
import com.mrkelpy.bountyseekers.commons.gui.PagedGUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * This class implements a GUI capable of displaying a selected bounty.
 */
public class BountyDisplayGUI extends PagedGUI {

    /**
     * The player who is viewing the GUI.
     */
    private final Player player;

    /**
     * Main constructor for the BountyDisplayGUI.
     *
     * @param items The bounty rewards to display.
     */
    public BountyDisplayGUI(Player player, ItemStack[] items) {
        super(ConfigurableTextHandler.INSTANCE.getValue("bounty.display.title"), 27, Arrays.asList(items), player.getUniqueId());
        this.player = player;
    }

    /**
     * Open the GUI for the player.
     */
    public void openInventory() {
        this.player.openInventory(this.inventory);
    }

    @Override
    protected void goBack() {
        new BountyListDisplayGUI(this.player).openInventory();
    }
}

