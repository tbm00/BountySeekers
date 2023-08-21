package com.mrkelpy.bountyseekers.interfaces;

import org.bukkit.inventory.ItemStack;

/**
 * This interface allows the SerializationUtils class to be able to use a different
 * version of the Reflection classes based on the server version. Every reflection class
 * should implement either this interface or the ReNMS interface.
 */
public interface IBukkitReflector {

    Class<?> getBukkitClass(String name);

    CraftItemStack getCraftItemStack();

    abstract class CraftItemStack {

        public abstract Object asNMSCopy(ItemStack itemStack);

        public abstract ItemStack asBukkitCopy(Object nmsItemStack);
    }
}

