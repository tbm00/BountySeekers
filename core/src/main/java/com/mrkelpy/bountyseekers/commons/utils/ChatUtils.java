package com.mrkelpy.bountyseekers.commons.utils;

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

        String formattedMessage = String.format("§7[§c%s§7] §e" + message, PluginConstants.PLUGIN_NAME);

        if (player != null) player.sendMessage(formattedMessage);
        return formattedMessage;
    }

}

