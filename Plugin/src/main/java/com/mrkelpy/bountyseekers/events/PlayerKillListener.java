package com.mrkelpy.bountyseekers.events;

import com.mrkelpy.bountyseekers.commons.carriers.Bounty;
import com.mrkelpy.bountyseekers.commons.commands.PluginCommandHandler;
import com.mrkelpy.bountyseekers.commons.configuration.ConfigurableTextHandler;
import com.mrkelpy.bountyseekers.commons.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKillListener implements Listener {


    /**
     * Handles the bounty claiming when a player is killed.
     * A bounty will be available for claiming if the killed player has a bounty
     * and the killer has the permission to claim bounties.
     *
     * @param event The event to handle.
     */
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {

        Player killer = event.getEntity().getKiller();
        if (killer == null || !PluginCommandHandler.checkPermission("bounty.claim", killer, false)) return;
        if (killer.getUniqueId() == event.getEntity().getUniqueId()) return;

        Bounty bounty = new Bounty(event.getEntity().getUniqueId());
        if (bounty.getRewards().isEmpty()) return;

        String killedMessage =  ConfigurableTextHandler.INSTANCE.getValueFormatted("bounty.claim.message", killer.getName(), bounty.getTarget());
        Bukkit.broadcastMessage(ChatUtils.sendMessage(null, killedMessage));
        bounty.claimBounty(killer);
    }

}

