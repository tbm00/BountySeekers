package com.mrkelpy.bountyseekers.events;

import com.mrkelpy.bountyseekers.commons.configuration.UUIDCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    /**
     * Caches the player into the UUID cache when they join the server.
     *
     * @param event PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUIDCache.INSTANCE.set(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }

}

