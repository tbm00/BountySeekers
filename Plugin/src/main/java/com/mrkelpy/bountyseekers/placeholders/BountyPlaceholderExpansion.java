package com.mrkelpy.bountyseekers.placeholders;

import com.mrkelpy.bountyseekers.commons.carriers.Bounty;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * This class handles the bounty placeholders, estabilishing compatibility with the Placeholder API.
 */
public class BountyPlaceholderExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull @NonNls String getIdentifier() {
        return "bounty";
    }

    @Override
    public @NotNull @NonNls String getAuthor() {
        return "MrKelpy";
    }

    @Override
    public @NotNull @NonNls String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() { return true; }

    /**
     * Handles the requests coming from users using the placeholder API with the "bounty"
     * identifier.
     * @param player The player invoking the request
     * @param identifier The identifier used to direct the requests to this class
     * @return The response to the placeholder
     */
    @Override
    public @NonNls String onRequest(OfflinePlayer player, String identifier) {

        // Gets the player's bounty, the basis for all placeholders
        Bounty bounty = new Bounty(player.getUniqueId());

        if (identifier.equals("rewardcount")) {
            return String.valueOf(bounty.getRewards().stream().count());
        }

        return null;
    }

}
