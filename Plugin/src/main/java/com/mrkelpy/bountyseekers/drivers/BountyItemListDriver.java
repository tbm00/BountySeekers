package com.mrkelpy.bountyseekers.drivers;

import com.mrkelpy.bountyseekers.commons.configuration.ConfigurableTextHandler;
import com.mrkelpy.bountyseekers.commons.configuration.UUIDCache;
import com.mrkelpy.bountyseekers.commons.utils.GUIUtils;
import com.mrkelpy.bountyseekers.drivers.resources.DriverVersionTarget;
import com.mrkelpy.bountyseekers.drivers.resources.FeatureDriver;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.UUID;

/**
 * This driver handles the creation of bounty item lists throughout the versions
 */
public class BountyItemListDriver extends FeatureDriver<ItemStack> {

    /**
     * Handles the creation of named arrow itemstacks for the bounty listings.
     * @param args The UUID of the player to create a head for
     * @return A renamed arrow to be put into the listing
     */
    @DriverVersionTarget("1.7.10")
    public ItemStack bountyItemListHandler17(Object... args) {

        UUID playerUUID = (UUID) args[0];
        String playername = UUIDCache.INSTANCE.getName(playerUUID);
        ItemStack item = GUIUtils.createItemPlaceholder(Material.getMaterial("ARROW"), playername);
        return item;
    }

    /**
     * Handles the creation of skinned and named player heads for the bounty listings
     * @param args The UUID of the player to create a head for
     * @return A renamed and skinned player head to be used on the listings
     */
    @DriverVersionTarget("1.16")
    public ItemStack bountyItemListHandler16(Object... args) {

        UUID playerUUID = (UUID) args[0];
        String playername = UUIDCache.INSTANCE.getName(playerUUID);
        String bountyViewText = ConfigurableTextHandler.INSTANCE.getValueFormatted("bounty.display.view", null, playername);

        ItemStack item = GUIUtils.getPlayerHeadPlaceholder(
                playerUUID, "§e" + playername, Collections.singletonList(bountyViewText));

        return item;
    }

}
