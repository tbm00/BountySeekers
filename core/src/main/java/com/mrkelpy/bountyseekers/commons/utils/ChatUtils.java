package com.mrkelpy.bountyseekers.commons.utils;

import com.mrkelpy.bountyseekers.commons.configuration.PluginConfiguration;
import org.bukkit.entity.Player;

public class ChatUtils {

    /**
     * Sends an ingame chat message to a player with the custom prefix and formatting
     * of the plugin.
     *
     * @param player  The player to send the message to.
     * @param message The message to send.
     * @return The message that was sent.
     */
    public static String sendMessage(Player player, String message) {

        // Get the formatting colours from the configuration
        String prefix = PluginConfiguration.INSTANCE.getConfig().getString("general.formatting.broadcast-prefix");

        // Replaces "&" with "§" for Minecraft formatting
        prefix = prefix.replace("&", "§");

        // Builds the formatted message with the colours and the plugin name
        String formattedMessage = prefix+message;

        if (player != null) player.sendMessage(formattedMessage);
        return formattedMessage;
    }

}

