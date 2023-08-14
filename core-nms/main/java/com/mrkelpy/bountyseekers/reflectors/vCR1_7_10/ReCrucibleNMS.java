package com.mrkelpy.bountyseekers.reflectors.vCR1_7_10;

import com.mrkelpy.bountyseekers.interfaces.INMSReflector;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.lang.reflect.InvocationTargetException;

/**
 * This class handles the needed classes related to NMS using reflection.<br>
 * <font color="yellow"> This class is compatible with Crucible version 1.7.10, and uses the MCP Mappings.</font>
 */
public class ReCrucibleNMS implements INMSReflector {

    public final static ReCrucibleNMS INSTANCE = new ReCrucibleNMS();

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
            return Class.forName("net.minecraft." + name);

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

        public final Class<?> CLASS = getNMSClass("nbt.NBTTagCompound");

        /**
         * Returns an NBTTagCompound instance from the given NMS ItemStack.
         *
         * @param nmsItemStack The NMS ItemStack to get the NBTTagCompound from.
         * @return The NBTTagCompound.
         */
        public Object create(Object nmsItemStack) {
            try {
                return nmsItemStack.getClass().getMethod("getTagCompound").invoke(nmsItemStack);

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
    public class NBTCompressedStreamTools extends INMSReflector.NBTCompressedStreamTools {

        public final Class<?> CLASS = getNMSClass("nbt.CompressedStreamTools");

        /**
         * Reflection of the NBTCompressedStreamTools.func_74800_a(NBTTagCompound, DataOutput) method.
         * <br>
         * This method writes the given NBTTagCompound into the DataOutputStream.
         */
        public void write(Object nbtTagCompound, DataOutput dataOutput) {
            try {
                CLASS.getMethod("func_74800_a", ReCrucibleNMS.INSTANCE.new NBTTagCompound().CLASS, DataOutput.class).invoke(CLASS, nbtTagCompound, dataOutput);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        /**
         * Reflection of the NBTCompressedStreamTools.func_74794_a(DataInputStream) method.
         * <br>
         * This method reads the NBTTagCompound from the DataInputStream.
         *
         * @param dataInputStream The DataInputStream to read from.
         * @return The NBTTagCompound.
         */
        public Object read(DataInputStream dataInputStream) {
            try {
                return CLASS.getMethod("func_74794_a", DataInputStream.class).invoke(CLASS, dataInputStream);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * This class holds every supported method for the reflected ItemStack class.
     */
    public class ItemStack extends INMSReflector.ItemStack {

        public final Class<?> CLASS = getNMSClass("item.ItemStack");

        /**
         * Reflection of the ItemStack.func_77949_a(NBTTagCompound) method.
         * <br>
         * This method creates a new ItemStack from the given NBTTagCompound.
         *
         * @param nbtTagCompound The NBTTagCompound to create the ItemStack from.
         * @return The ItemStack with the given NBTTagCompound.
         */
        public Object create(Object nbtTagCompound) {
            try {
                return CLASS.getMethod("func_77949_a", ReCrucibleNMS.INSTANCE.new NBTTagCompound().CLASS).invoke(CLASS, nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Reflection of the ItemStack.func_77955_b(NBTTagCompound) method.
         * <br>
         * This method deposits the given NBTTagCompound data into the ItemStack.
         *
         * @param itemStack      The ItemStack to save the NBTTagCompound into.
         * @param nbtTagCompound The NBTTagCompound to deposit.
         * @return The NBT-Filled ItemStack.
         */
        public Object save(Object itemStack, Object nbtTagCompound) {
            try {
                return itemStack.getClass().getMethod("func_77955_b", ReCrucibleNMS.INSTANCE.new NBTTagCompound().CLASS).invoke(itemStack, nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

}

