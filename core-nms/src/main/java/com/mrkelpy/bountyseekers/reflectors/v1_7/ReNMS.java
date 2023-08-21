package com.mrkelpy.bountyseekers.reflectors.v1_7;

import com.mrkelpy.bountyseekers.interfaces.INMSReflector;
import org.bukkit.Bukkit;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.lang.reflect.InvocationTargetException;

/**
 * This class handles the needed classes related to NMS using reflection.<br>
 * <font color="yellow"> This class is compatible with Minecraft Version 1.7.x-1.11</font>
 */
public class ReNMS implements INMSReflector {

    public final static ReNMS INSTANCE = new ReNMS();

    /**
     * Uses reflection to get the current NMS class for the given server version. This is possible since all that changes
     * between NMS versions is the package version name, and the class names stay mostly the same.
     *
     * @param name The name of the class to get.
     * @return The reflected class.
     */
    @Override
    public Class<?> getNMSClass(String name) {

        try {
            return Class.forName("net.minecraft.server." +
                    Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            // If an error happens, print it into console and return null.
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public INMSReflector.NBTTagCompound getNBTTagCompound() {
        return INSTANCE.new NBTTagCompound();
    }

    @Override
    public INMSReflector.NBTCompressedStreamTools getNBTCompressedStreamTools() {
        return INSTANCE.new NBTCompressedStreamTools();
    }

    @Override
    public INMSReflector.ItemStack getItemStack() {
        return INSTANCE.new ItemStack();
    }

    /**
     * This class holds every supported method for the reflected NBTTagCompound class.
     */
    public class NBTTagCompound extends INMSReflector.NBTTagCompound {

        public final Class<?> CLASS = getNMSClass("NBTTagCompound");

        /**
         * Returns an NBTTagCompound instance from the given NMS ItemStack.
         *
         * @param nmsItemStack The NMS ItemStack to get the NBTTagCompound from.
         * @return The NBTTagCompound.
         */
        public Object create(Object nmsItemStack) {
            try {
                return nmsItemStack.getClass().getMethod("getTag").invoke(nmsItemStack);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * @return A Reflected  empty NBTTagCompound instance.
         */
        public Object create() {
            try {
                return CLASS.getConstructor().newInstance();

            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    /**
     * This class holds every supported method for the reflected NBTCompressionStreamTools class.
     */
    @SuppressWarnings("InnerClassMayBeStatic")
    public class NBTCompressedStreamTools extends INMSReflector.NBTCompressedStreamTools {

        public final Class<?> CLASS = ReNMS.INSTANCE.getNMSClass("NBTCompressedStreamTools");

        /**
         * Reflection of the NBTCompressedStreamTools.a(NBTTagCompound, DataOutput) method.
         * <br>
         * This method writes the given NBTTagCompound into the DataOutputStream.
         */
        public void write(Object nbtTagCompound, DataOutput dataOutput) {
            try {
                CLASS.getMethod("a", ReNMS.INSTANCE.new NBTTagCompound().CLASS, DataOutput.class).invoke(CLASS, nbtTagCompound, dataOutput);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        /**
         * Reflection of the NBTCompressedStreamTools.a(DataInputStream) method.
         * <br>
         * This method reads the NBTTagCompound from the DataInputStream.
         *
         * @param dataInputStream The DataInputStream to read from.
         * @return The NBTTagCompound.
         */
        public Object read(DataInputStream dataInputStream) {
            try {
                return CLASS.getMethod("a", DataInputStream.class).invoke(CLASS, dataInputStream);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * This class holds every supported method for the reflected ItemStack class.
     */
    @SuppressWarnings("InnerClassMayBeStatic")
    public class ItemStack extends INMSReflector.ItemStack {

        public final Class<?> CLASS = ReNMS.INSTANCE.getNMSClass("ItemStack");

        /**
         * Reflection of the ItemStack.createStack(NBTTagCompound) method.
         * <br>
         * This method creates a new ItemStack from the given NBTTagCompound.
         *
         * @param nbtTagCompound The NBTTagCompound to create the ItemStack from.
         * @return The ItemStack with the given NBTTagCompound.
         */
        public Object create(Object nbtTagCompound) {
            try {
                return CLASS.getMethod("createStack", ReNMS.INSTANCE.new NBTTagCompound().CLASS).invoke(CLASS, nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Reflection of the ItemStack.save(NBTTagCompound) method.
         * <br>
         * This method deposits the given NBTTagCompound data into the ItemStack.
         *
         * @param itemStack      The ItemStack to save the NBTTagCompound into.
         * @param nbtTagCompound The NBTTagCompound to deposit.
         * @return The NBT-Filled ItemStack.
         */
        public Object save(Object itemStack, Object nbtTagCompound) {
            try {
                return itemStack.getClass().getMethod("save", ReNMS.INSTANCE.new NBTTagCompound().CLASS).invoke(itemStack, nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

}

