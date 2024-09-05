package com.mrkelpy.bountyseekers.commons.commands;

import com.mrkelpy.bountyseekers.commons.enums.CommandRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Using the command and permissioning system built into this architecture, automatically gathers the subcommands
 * and creates an autocompletion
 */
public class AutoTabCompleter implements TabCompleter {

    /**
     * Main event handling for auto tab completion.
     * This will present the various subcommands registered in the CommandRegistry enum automatically.
     * @param commandSender The sender of the command
     * @param command The current command in the console being executed
     * @param s The primary command name
     * @param strings The arguments provided by the user in an array
     * @return A list of suggestions provided by the tab completer
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        // Initialises the suggestions list and ensures the autocomplete only works at the start
        List<String> suggestions = new ArrayList<>();
        if (strings.length > 1) return suggestions;

        String masterCommand = CommandRegistry.MASTER.getUsage().replace("/", "");

        if (s.equals(masterCommand))
            suggestions.addAll(this.getPrimarySubCommands());

        return suggestions;

    }

    /**
     * Accesses the CommandRegistry enum and gets all of the primary subcommands from it.
     * @return A list containing the primary subcommands of the plugin
     */
    public List<String> getPrimarySubCommands() {

        CommandRegistry[] registry = CommandRegistry.values();
        return Arrays.stream(registry)
                .filter(x -> x.getUsage().split(" ").length >= 2)
                .map(x -> x.getUsage().split(" ")[1]).collect(Collectors.toList());
    }

}
