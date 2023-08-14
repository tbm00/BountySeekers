package com.mrkelpy.bountyseekers.commons.enums;

/**
 * This class holds all the existent commands, their needed permissions, the description, and
 * usage.
 */
public enum CommandRegistry {

    MASTER("/bounty", "bounty", "Master command", false),
    BOUNTY_LIST("/bounty list", "bounty.list", "Displays the list of active bounties."),
    CHECK("/bounty check <target player>", "bounty.check", "Checks the bounty on a player."),
    BOUNTY_RAISE("/bounty raise <target player>", "bounty.raise", "Raises the bounty on a player by contributing to the player's bounty reward."),
    BOUNTY_SILENT_RAISE("/bounty silentraise <target player>", "bounty.raise.silent", "Raises the bounty for a player, hiding the identity of the benefactor."),
    BOUNTY_RESET("/bounty reset <target player>", "bounty.reset", "Resets the bounty on a player."),
    SET_REWARD_LIMIT("/bounty setrewardlimit <limit>", "bounty.admin.setrewardlimit", "Sets the maximum amount of rewards a player can receive for their bounty."),
    SET_REWARD_FILTERS("/bounty setrewardfilters", "bounty.admin.setrewardfilters", "Sets the items that can go into a bounty's rewards."),
    HELP("/bounty help", "bounty.help", "Displays the help menu.");

    private final String usage;
    private final String permission;
    private final String description;
    private final boolean visible;

    /**
     * Main constructor for the CommandRegistry enum.
     *
     * @param usage       The usage of the command.
     * @param permission  The permission needed to execute the command.
     * @param description The command description.
     * @param visible     Whether the command should be visible in the help menu or not.
     */
    CommandRegistry(String usage, String permission, String description, boolean visible) {
        this.usage = usage;
        this.permission = permission;
        this.description = description;
        this.visible = visible;
    }

    /**
     * Shortcut constructor for the CommandRegistry enum. Sets visibility to true.
     *
     * @param usage       The usage of the command.
     * @param permission  The permission needed to execute the command.
     * @param description The command description.
     */
    CommandRegistry(String usage, String permission, String description) {
        this(usage, permission, description, true);
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    public boolean isVisible() {
        return visible;
    }

}

