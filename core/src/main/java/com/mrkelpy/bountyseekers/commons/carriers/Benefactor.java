package com.mrkelpy.bountyseekers.commons.carriers;

import com.mrkelpy.bountyseekers.commons.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * Bean class for holding a Player and a boolean representing the benefactor and
 * whether they're anonymous or not.
 */
public class Benefactor {

    private final Player player;
    private final boolean anonymous;
    private final Inventory inventory;

    /**
     * Main constructor for the Benefactor class.
     *
     * @param player    The player to set.
     * @param anonymous Whether the player is anonymous or not.
     */
    public Benefactor(Player player, boolean anonymous) {
        this.player = player;
        this.anonymous = anonymous;
        this.inventory = Bukkit.createInventory(null, InventoryType.CHEST.getDefaultSize() + 9);
        this.inventory.setContents(ItemStackUtils.getStorageContents(this.player));
    }

    /**
     * @return The player.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * @return Whether the player is anonymous or not.
     */
    public boolean isAnonymous() {
        return this.anonymous;
    }

    /**
     * @return The inventory of the player.
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Returns the player's UUID with a suffix if they're anonymous, otherwise
     * return the UUID normally.
     *
     * @return The player's UUID.
     */
    @Override
    public String toString() {
        return this.isAnonymous() ? null : this.player.getUniqueId().toString();
    }


}

