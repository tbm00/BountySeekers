package com.mrkelpy.bountyseekers.reflectors.v1_17;

import com.mrkelpy.bountyseekers.interfaces.IBukkitReflector;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * This class implements reflections for any needed bukkit methods. <br>
 * <font color="yellow"> This class is compatible with Minecraft Version 1.17.x-x</font>
 */
public class ReBukkit implements IBukkitReflector {

    public static final ReBukkit INSTANCE = new ReBukkit();

    /**
     * Uses reflection to get the current Bukkit class for the given server version.
     *
     * @param name The name of the class to get.
     * @return The reflected class.
     */
    @Override
    public Class<?> getBukkitClass(String name) {

        try {
            return Class.forName("org.bukkit.craftbukkit." +
                    Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            // If an error happens, print it into console and return null.
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IBukkitReflector.CraftItemStack getCraftItemStack() {
        return INSTANCE.new CraftItemStack();
    }

    /**
     * This class holds every supported method for the reflected CraftItemStack class.
     */
    @SuppressWarnings("InnerClassMayBeStatic")
    public class CraftItemStack extends IBukkitReflector.CraftItemStack {

        private final Class<?> CLASS = ReBukkit.INSTANCE.getBukkitClass("inventory.CraftItemStack");

        /**
         * Reflection of the CraftItemStack.asNMSCopy(ItemStack) method.
         * <br>
         * This method creates a new NMS ItemStack from the given ItemStack.
         *
         * @param itemStack The Bukkit ItemStack to create the NMS ItemStack from.
         * @return The NMS ItemStack.
         */
        public Object asNMSCopy(ItemStack itemStack) {
            try {
                return CLASS.getMethod("asNMSCopy", ItemStack.class).invoke(CLASS, itemStack);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Reflection of the CraftItemStack.asBukkitCopy(ItemStack) method.
         * <br>
         * This method creates a new Bukkit ItemStack from the given NMS ItemStack.
         *
         * @param nmsItemStack The NMS ItemStack to create the Bukkit ItemStack from.
         * @return The Bukkit ItemStack.
         */
        public ItemStack asBukkitCopy(Object nmsItemStack) {
            try {
                return (ItemStack) CLASS.getMethod("asBukkitCopy", ReNMS.INSTANCE.new ItemStack().CLASS).invoke(CLASS, nmsItemStack);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}

