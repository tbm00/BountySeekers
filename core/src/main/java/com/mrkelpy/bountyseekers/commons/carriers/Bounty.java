package com.mrkelpy.bountyseekers.commons.carriers;

import com.mrkelpy.bountyseekers.commons.configuration.UUIDCache;
import com.mrkelpy.bountyseekers.commons.enums.CompatibilityMode;
import com.mrkelpy.bountyseekers.commons.utils.FileUtils;
import com.mrkelpy.bountyseekers.commons.utils.ItemStackUtils;
import com.mrkelpy.bountyseekers.commons.utils.SerializationUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

/**
 * This class creates a convenient method to hold and perform operations
 * centered around a player's bounty.
 */
public class Bounty {

    /**
     * The player who has the bounty.
     */
    private final String target;

    /**
     * The target's UUID
     */
    private final UUID targetUUID;

    /**
     * The items offered to the one who claims the bounty.
     */
    private final List<ItemStack> rewards;

    /**
     * The amount of rewards to the bounty since the object initialization
     */
    private int raisedStackCount;

    /**
     * The .bounty file that holds the bounty information
     */
    private final File bountyFile;

    /**
     * The serialization utility that serializes and deserializes the bounty file
     */
    private final SerializationUtils serializationUtils;

    /**
     * Main constructor for the Bounty class. From a player's name, go to their bounty file and extract the needed
     * information for the Bounty class. This automatically handles the base64 to List conversion for the rewards.
     *
     * @param playerUUID The target player's UUID.
     * @param compat     The compatibility mode to use for the Bounty class.
     */
    public Bounty(UUID playerUUID, CompatibilityMode compat) {
        this.target = UUIDCache.INSTANCE.getName(playerUUID);
        this.targetUUID = playerUUID;
        this.serializationUtils = new SerializationUtils(compat);

        this.bountyFile = new File(FileUtils.makeDirectory("bounties"), playerUUID + ".bounty");
        String bountyInformation = this.bountyFile.exists() ? FileUtils.readFile(this.bountyFile) : null;

        ItemStack[] rewardArray = bountyInformation != null ? this.serializationUtils.itemStackArrayFromBase64(bountyInformation) : null;
        this.rewards = bountyInformation != null && rewardArray != null ? new ArrayList<>(Arrays.asList(rewardArray)) : new ArrayList<>();
        this.raisedStackCount = 0;
    }

    public String getTarget() {
        return this.target;
    }

    public List<ItemStack> getRewards() {
        return this.rewards;
    }

    public int getAdditionCount() {
        return this.raisedStackCount;
    }

    /**
     * Raises the bounty for the target by adding the reward to the bounty.
     *
     * @param reward The reward to add to the bounty.
     */
    public void addReward(ItemStack reward) {

        if (reward != null && reward.getType() != Material.AIR) {
            this.rewards.add(reward);
            this.raisedStackCount += 1;
        }
    }

    /**
     * Adds all the items present in the rewards List to the given hunter's inventory.
     * This will also reset the bounty for the hunter's inventory
     *
     * @param hunter The bounty hunter that claimed the bounty.
     */
    public void claimBounty(Player hunter) {

        for (ItemStack reward : this.rewards) {

            if (!Arrays.stream(hunter.getInventory().getContents()).allMatch(Objects::nonNull))
                hunter.getInventory().addItem(reward);

                // If the inventory is full, start dropping the rewards on the ground.
            else {
                hunter.getWorld().dropItem(hunter.getLocation(), reward);
            }
        }

        this.reset();
    }

    /**
     * Resets a player's bounty. This will delete the bounty file and null out the current instance's fields.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void reset() {

        this.rewards.clear();
        this.bountyFile.delete();
    }

    /**
     * Save the bounty to the .bounty file.
     */
    public void save() {
        List<ItemStack> compressedRewards = ItemStackUtils.compress(this.rewards);

        if (compressedRewards.size() > 0)
            FileUtils.writeFile(this.bountyFile, this.serializationUtils.itemStackArrayToBase64(compressedRewards.toArray(new ItemStack[0])));

        UUIDCache.INSTANCE.set(this.targetUUID, this.target);
    }

}

