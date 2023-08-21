package com.mrkelpy.bountyseekers.commons.carriers;

import com.mrkelpy.bountyseekers.commons.configuration.UUIDCache;

import java.util.UUID;

/**
 * This class implements a convenient way to hold a player's name and UUID.
 */
public class SimplePlayer {

    private final String name;
    private final UUID uuid;

    /**
     * Main constructor for the SimplePlayer class. This allows for manually
     * setting the name and UUID of a player.
     *
     * @param name The player's name.
     * @param uuid The player's UUID.
     */
    public SimplePlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    /**
     * Overloaded constructor for the SimplePlayer class. This automatically
     * fills in the UUID for a player based on their name and the UUIDCache.
     *
     * @param player The player's name
     */
    public SimplePlayer(String player) {
        this(player, UUIDCache.INSTANCE.getUUID(player));
    }

    /**
     * Overloaded constructor for the SimplePlayer class. This automatically
     * fills in the name for a player based on their UUID and the UUIDCache.
     *
     * @param uuid The player's UUID
     */
    public SimplePlayer(UUID uuid) {
        this(UUIDCache.INSTANCE.getName(uuid), uuid);
    }


    public String getName() {
        return name;
    }

    public UUID getUniqueId() {
        return uuid;
    }

}

