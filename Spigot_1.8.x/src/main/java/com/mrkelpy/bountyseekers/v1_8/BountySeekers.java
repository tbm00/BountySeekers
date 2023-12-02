package com.mrkelpy.bountyseekers.v1_8;

import com.mrkelpy.bountyseekers.commons.commands.PluginCommandHandler;
import com.mrkelpy.bountyseekers.commons.configuration.InternalConfigs;
import com.mrkelpy.bountyseekers.commons.configuration.ConfigurableTextHandler;
import com.mrkelpy.bountyseekers.commons.enums.CompatibilityMode;
import com.mrkelpy.bountyseekers.commons.events.PlayerJoinListener;
import com.mrkelpy.bountyseekers.commons.events.PlayerKillListener;
import com.mrkelpy.bountyseekers.commons.utils.PluginConstants;
import com.mrkelpy.bountyseekers.commons.utils.SerializationUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class for BountySeekers plugin, the entrypoint to registration and kickstarting of any needed
 * tasks.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class BountySeekers extends JavaPlugin {

    public static File DATA_FOLDER;
    public static CompatibilityMode compatibility = CompatibilityMode.v1_7;
    public static final SerializationUtils SERIALIZER = new SerializationUtils(compatibility);

    @Override
    public void onEnable() {
        DATA_FOLDER = this.getDataFolder();
        if (!DATA_FOLDER.exists()) DATA_FOLDER.mkdirs();

        getCommand("bounty").setExecutor(new PluginCommandHandler(new CommandImplementations()));
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerKillListener(BountySeekers.compatibility), this);

        PluginConstants.LOGGER.info(String.format("Enabled %s v%s", PluginConstants.PLUGIN_NAME, this.getDescription().getVersion()));
        loadConfigs();
    }

    @Override
    public void onDisable() {
        PluginConstants.LOGGER.info(String.format("Disabled %s v%s", PluginConstants.PLUGIN_NAME, this.getDescription().getVersion()));
    }

    /**
     * Loads all the configs and caches.
     */
    public static void loadConfigs() {
        InternalConfigs.INSTANCE.save();
        ConfigurableTextHandler.INSTANCE.save();
    }

}

