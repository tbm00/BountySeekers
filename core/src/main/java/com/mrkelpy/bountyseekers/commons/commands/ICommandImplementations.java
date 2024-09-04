package com.mrkelpy.bountyseekers.commons.commands;

import com.mrkelpy.bountyseekers.commons.enums.CommandRegistry;
import com.mrkelpy.bountyseekers.commons.utils.PluginConstants;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * This interface allows for an explicit declaration of the methods that will contain the actual method implementations,
 * and is needed to ensure that they exist, and for them to be called (in their actual implementation) through Reflection in the PluginCommandHandler.
 */
public interface ICommandImplementations {

    boolean listCommand(CommandSender commandSender, String[] args);

    boolean checkCommand(CommandSender commandSender, String[] args);

    boolean raiseCommand(CommandSender commandSender, String[] args);

    boolean silentRaiseCommand(CommandSender commandSender, String[] args);

    boolean resetCommand(CommandSender commandSender, String[] args);

    boolean setRewardFiltersCommand(CommandSender commandSender, String[] args);

    boolean setRewardLimitCommand(CommandSender commandSender, String[] args);

    /**
     * The default implementation of this method iterates through every registered command and
     * builds a help menu command with the command usage, the description, and the needed permissions for every command.
     *
     * @param commandSender The sender of the command.
     * @param args          The command arguments.
     * @return Boolean, Feedback to the sender.
     */
    default boolean helpCommand(CommandSender commandSender, String[] args) {

        commandSender.sendMessage(String.format("§e----- §c%s Command List§e-----", PluginConstants.PLUGIN_NAME));

        for (CommandRegistry command : CommandRegistry.values()) {
            commandSender.sendMessage(String.format("§e%s §7-> §f%s", command.getUsage(), command.getDescription()) + "\n");
        }

        commandSender.sendMessage(Bukkit.getVersion());
        commandSender.sendMessage(Bukkit.getServer().getVersion());

        return true;
    }

}

