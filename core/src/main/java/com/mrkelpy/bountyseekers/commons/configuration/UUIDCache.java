package com.mrkelpy.bountyseekers.commons.configuration;

import com.mrkelpy.bountyseekers.commons.utils.FileUtils;
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
            e.printStackTrace();
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
        return this.cache.getString(string) != null ? UUID.fromString(this.cache.getString(string)) : null;
    }

    /**
     * Caches a player's UUID and name.
     */
    public void set(UUID uuid, String name) {
        this.cache.set(uuid.toString().toLowerCase(), name);
        this.cache.set(name, uuid.toString());

        try {
            cache.save(this.uuidCacheFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads the cache
     */
    public void reload() {
        this.cache = this.load();
    }

}

