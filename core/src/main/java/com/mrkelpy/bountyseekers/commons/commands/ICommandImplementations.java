package com.mrkelpy.bountyseekers.commons.commands;

import com.mrkelpy.bountyseekers.commons.configuration.InternalConfigs;
import com.mrkelpy.bountyseekers.commons.enums.CommandRegistry;
import com.mrkelpy.bountyseekers.commons.utils.ChatUtils;
import com.mrkelpy.bountyseekers.commons.utils.PluginConstants;
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

    /**
     * Changes the configured reward limit for bounties.
     *
     * @param commandSender The sender of the command
     * @param args          The arguments of the command
     * @return Boolean, feedback to the caller
     */
    default boolean setRewardLimitCommand(CommandSender commandSender, String[] args) {

        if (!PluginCommandHandler.checkPermission(CommandRegistry.SET_REWARD_LIMIT.getPermission(), commandSender))
            return true;

        if (args.length != 1) {
            commandSender.sendMessage("§cUsage:" + CommandRegistry.SET_REWARD_LIMIT.getUsage());
            return true;
        }

        try {
            // Changes the reward limit to the amount specified
            InternalConfigs.INSTANCE.getConfig().set("reward-limit", Integer.parseInt(args[0]));
            InternalConfigs.INSTANCE.save();
            commandSender.sendMessage(ChatUtils.sendMessage(null, "Reward limit set to " + Integer.parseInt(args[0])));
            return true;

        } catch (NumberFormatException e) {
            // If the argument is not a number, send an error message
            commandSender.sendMessage("Limit must be numeric.");
            return true;
        }

    }

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

        return true;
    }

}

