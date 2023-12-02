package com.mrkelpy.bountyseekers.commons.configuration;

import com.mrkelpy.bountyseekers.commons.utils.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * This class implements a configuration for the messages displayed throughout the plugin.
 */
public class ConfigurableTextHandler {

    public static final ConfigurableTextHandler INSTANCE = new ConfigurableTextHandler();
    private final File configFile = new File(FileUtils.makeDirectory("data"), "messages.yml");
    private final YamlConfiguration config = YamlConfiguration.loadConfiguration(this.configFile);

    /**
     * Main constructor for the MessagesConfigHandler class.
     * Adds the default values to the config and saves it.
     */
    public ConfigurableTextHandler() {
        this.addDefaults();
        this.save();
    }

    /**
     * Returns a value from the config
     * @param configName The name of the config to get the value from
     * @param hunter The hunter's name
     * @param target The target's name
     * @return The player's name
     */
    public String getValueFormatted(String configName, String hunter, String target) {

        if (hunter == null) hunter = "";
        if (target == null) target = "";

        return this.config.getString(configName, "").replace("%h", hunter).replace("%t", target);
    }

    /**
     * Saves the config into memory.
     */
    public void save() {
        try {
            this.config.save(this.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the default data values to the file.
     */
    private void addDefaults() {

        if (!this.config.contains("bounty.claim"))
            this.config.set("bounty.claim.message", "%h has claimed %t's bounty!");

        if (!this.config.contains("bounty.raise.loud"))
            this.config.set("bounty.raise.loud", "%h has raised %t's bounty!");

        if (!this.config.contains("bounty.raise.silent"))
            this.config.set("bounty.raise.silent", "A player has raised %t's bounty!");

        if (!this.config.contains("bounty.raise.denied"))
            this.config.set("bounty.raise.denied", "Some items were returned to you because they are not allowed as bounties!");

        if (!this.config.contains("bounty.raise.title"))
            this.config.set("bounty.raise.title", "Raise %t's bounty");


    }
}
