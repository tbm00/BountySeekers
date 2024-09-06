package com.mrkelpy.bountyseekers;

import com.mrkelpy.bountyseekers.commons.commands.AutoTabCompleter;
import com.mrkelpy.bountyseekers.commons.commands.PluginCommandHandler;
import com.mrkelpy.bountyseekers.commons.configuration.ConfigurableTextHandler;
import com.mrkelpy.bountyseekers.commons.configuration.InternalConfigs;
import com.mrkelpy.bountyseekers.commons.configuration.PluginConfiguration;
import com.mrkelpy.bountyseekers.commons.utils.PluginConstants;
import com.mrkelpy.bountyseekers.events.PlayerJoinListener;
import com.mrkelpy.bountyseekers.events.PlayerKillListener;
import com.mrkelpy.bountyseekers.placeholders.BountyPlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class for BountySeekers plugin, the entrypoint to registration and kickstarting of any needed
 * tasks.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class BountySeekers extends JavaPlugin {

    public static File DATA_FOLDER;

    @Override
    public void onEnable() {
        DATA_FOLDER = this.getDataFolder();
        if (!DATA_FOLDER.exists()) DATA_FOLDER.mkdirs();

        this.getCommand("bounty").setExecutor(new PluginCommandHandler(new CommandImplementations()));
        this.getCommand("bounty").setTabCompleter(new AutoTabCompleter());

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerKillListener(), this);

        PluginConstants.LOGGER.info(String.format("Enabled %s v%s", PluginConstants.PLUGIN_NAME, this.getDescription().getVersion()));
        this.hookPlaceholderAPI();
        loadConfigs();
    }

    @Override
    public void onDisable() {
        PluginConstants.LOGGER.info(String.format("Disabled %s v%s", PluginConstants.PLUGIN_NAME, this.getDescription().getVersion()));
    }

    /**
     * Hooks and registers the PlaceholderAPI resources and plugin
     */
    public void hookPlaceholderAPI() {

        // Skips the placeholder API hooking if it isn't installed
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            PluginConstants.LOGGER.info("Placeholder API not found - Skipping hook.");
            return;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new BountyPlaceholderExpansion().register();
        }

    }

    /**
     * Loads all the configs and caches.
     */
    public static void loadConfigs() {
        InternalConfigs.INSTANCE.save();
        ConfigurableTextHandler.INSTANCE.save();
        PluginConfiguration.INSTANCE.save();
    }
}

