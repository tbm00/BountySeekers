package com.mrkelpy.bountyseekers.commons.configuration;

import com.mrkelpy.bountyseekers.commons.utils.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * This class implements an internal configuraton that stores any needed values.
 */
public class InternalConfigs {

    public static final InternalConfigs INSTANCE = new InternalConfigs();
    private final File configFile = new File(FileUtils.makeDirectory("data"), "internal.yml");
    private final YamlConfiguration config = YamlConfiguration.loadConfiguration(this.configFile);

    /**
     * Main constructor for the InternalConfigs class. Just adds the default values to the config
     * and saves it.
     */
    public InternalConfigs() {
        this.addDefaults();
        this.save();
    }

    /**
     * Returns the YamlConfiguration object to use to get and set values.
     *
     * @return The player's name
     */
    public YamlConfiguration getConfig() {
        return this.config;
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

        if (!this.getConfig().contains("reward-limit"))
            this.getConfig().set("reward-limit", -1);

        if (!this.getConfig().contains("reward-filter"))
            this.getConfig().set("reward-filter", null);
    }

}

