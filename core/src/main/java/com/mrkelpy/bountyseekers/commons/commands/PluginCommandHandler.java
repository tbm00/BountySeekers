package com.mrkelpy.bountyseekers.commons.commands;

import com.mrkelpy.bountyseekers.commons.enums.CommandRegistry;
import com.mrkelpy.bountyseekers.commons.utils.LoggingUtils;
import com.mrkelpy.bountyseekers.commons.utils.PluginConstants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class handles all the plugin commands that are sent to the server. This is the class that
 * should be registered in the plugin in order to use the command system.
 */
@SuppressWarnings("unused")
public class PluginCommandHandler implements CommandExecutor {

    private final ICommandImplementations commands;

    public PluginCommandHandler(ICommandImplementations commands) {
        this.commands = commands;
    }

    /**
     * Listens for commands sent to the server.
     *
     * @param commandSender The sender of the command.
     * @param command       The command that was sent.
     * @param s             The command as a string
     * @param args          The arguments of the command.
     * @return (Boolean) Feedback to the caller.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (Objects.equals(command.getName(), CommandRegistry.MASTER.getUsage().substring(1))) {
            this.parseCommands(commandSender, args);
        }

        return true;
    }

    /**
     * Checks if a player has permission to use a command. If not, send a message to the player telling
     * them they do not have permission.
     *
     * @param permission The permission to check for
     * @param sender     The sender to check for the permission
     * @param warn       Whether to send a 'no perms' message to the player or not
     * @return Whether the player has permission or not
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean checkPermission(String permission, CommandSender sender, boolean warn) {
        if (sender.hasPermission(CommandRegistry.MASTER.getPermission() + ".*") || sender.isOp() || sender.hasPermission(permission))
            return true;

        if (warn) sender.sendMessage("§cYou do not have permission to use this command");
        return false;
    }

    /**
     * Overloaded method to check for permission, with the default value of "warn" set to true.
     * @param permission The permission to check for
     * @param sender    The sender to check for the permission
     * @return Whether the player has permission or not
     */
    public static boolean checkPermission(String permission, CommandSender sender) {
        return checkPermission(permission, sender, true);
    }

    /**
     * Since there's a base command, this method takes in the first argument after that prefix and parses the command
     * normally from there. This serves to prevent the command from being used by other plugins, and to have a "command space"
     * for the plugin.
     * The "args" array will also be modified, removing the first element, because that's the command to be called.
     *
     * @param commandSender The sender of the command
     * @param args          The arguments of the command
     * @return Boolean, feedback to the caller
     */
    //supress unused return value
    @SuppressWarnings("UnusedReturnValue")
    private boolean parseCommands(CommandSender commandSender, String[] args) {

        if (args.length < 1) args = new String[]{"help"};

        // Process the arguments and remove the first element
        String command = args[0];
        List<String> argumentProcessing = new LinkedList<>(Arrays.asList(args));
        argumentProcessing.remove(0);
        args = Arrays.copyOf(argumentProcessing.toArray(), args.length - 1, String[].class);

        try {
            // Reflect the method that corresponds to the command implementation and call it
            this.getCommandMethodReflection(command).invoke(this.commands, commandSender, args);
            return true;

        } catch (InvocationTargetException e) {
            commandSender.sendMessage("§cAn internal error happened whilst running this command. Please report this issue to the plugin developer.");
            PluginConstants.LOGGER.warning("An internal error happened whilst running <" + command + "> with arguments [" + String.join(", ", args) + "]");
            PluginConstants.LOGGER.warning(LoggingUtils.getStackTrace(e));
            return true;

        } catch (NoSuchMethodException | IllegalAccessException ignored) {
        }

        commandSender.sendMessage("§cUnknown command. Use " + CommandRegistry.HELP.getUsage() + " for a list of available commands");
        return true;
    }

    /**
     * Retrieves the method reflection from the command implementation class.
     *
     * @param command The command name to locate the implementation method
     * @return The method reflection
     */
    private Method getCommandMethodReflection(String command) throws NoSuchMethodException {

        for (Method method : this.commands.getClass().getMethods()) {
            if (method.getName().equalsIgnoreCase(command + "Command")) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }


}

