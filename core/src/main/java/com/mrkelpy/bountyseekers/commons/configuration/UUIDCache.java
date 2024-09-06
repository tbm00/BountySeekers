package com.mrkelpy.bountyseekers.commons.configuration;

import com.mrkelpy.bountyseekers.commons.utils.FileUtils;
import com.mrkelpy.bountyseekers.commons.utils.LoggingUtils;
import com.mrkelpy.bountyseekers.commons.utils.PluginConstants;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

/**
 * This class implements a cache that stores UUIDs bound to player names.
 */
@SuppressWarnings("unused")
public class UUIDCache {

    public static final UUIDCache INSTANCE = new UUIDCache();
    private final File uuidCacheFile = new File(FileUtils.makeDirectory("data"), "uuid-cache.yml");
    private YamlConfiguration cache = this.load();

    /**
     * Loads up the cache from the file
     */
    public YamlConfiguration load() {
        YamlConfiguration cache = YamlConfiguration.loadConfiguration(this.uuidCacheFile);
        cache.options().copyDefaults(false);

        try {
            cache.save(this.uuidCacheFile);
        } catch (Exception e) {
            PluginConstants.LOGGER.severe(e.getCause().getMessage());
            PluginConstants.LOGGER.severe(LoggingUtils.getStackTrace(e));
        }

        return cache;
    }

    /**
     * Returns a player's name from the UUID
     *
     * @return The player's name
     */
    public String getName(UUID uuid) {
        return this.cache.getString(uuid.toString()) != null ? this.cache.getString(uuid.toString()) : null;
    }

    /**
     * Returns a player's UUID or name from a string. The string can be the UUID or name.
     *
     * @return The player's UUID
     */
    public UUID getUUID(String string) {

        // Permament case-insensitive UUID check, to return post 2.0 uuids
        String insensitiveUUID = this.cache.getString(string.toLowerCase());
        if (insensitiveUUID != null) return UUID.fromString(insensitiveUUID);

        // Backwards compatibility case sensitive check
        return this.cache.getString(string) != null ? UUID.fromString(this.cache.getString(string)) : null;
    }

    /**
     * Caches a player's UUID and name.
     */
    public void set(UUID uuid, String name) {

        // Backwards compatible case sensitive entry removal
        if (this.cache.contains(name)) {
            this.cache.set(name, null);
        }

        // Add names in case insensitive form
        this.cache.set(uuid.toString(), name.toLowerCase());
        this.cache.set(name.toLowerCase(), uuid.toString());

        try {
            cache.save(this.uuidCacheFile);
        } catch (Exception e) {
            PluginConstants.LOGGER.severe(e.getCause().getMessage());
            PluginConstants.LOGGER.severe(LoggingUtils.getStackTrace(e));
        }
    }

    /**
     * Reloads the cache
     */
    public void reload() {
        this.cache = this.load();
    }

}

