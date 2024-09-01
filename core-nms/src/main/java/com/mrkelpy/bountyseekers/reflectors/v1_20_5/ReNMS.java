package com.mrkelpy.bountyseekers.reflectors.v1_20_5;

import com.mrkelpy.bountyseekers.interfaces.INMSReflector;
import net.minecraft.world.item.ItemStack;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class handles the needed classes related to NMS using reflection.<br>
 * <font color="yellow"> This class is compatible with Minecraft Version 1.11.x-1.16.x</font>
 */
public class ReNMS implements INMSReflector {

    public final static ReNMS INSTANCE = new ReNMS();

    /**
     * Uses reflection to get the asked NMS class. This is possible since after mc 1.17 all class
     * locations are at a common point.
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
                net.minecraft.world.item.ItemStack.
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

        public final Class<?> CLASS = ReNMS.INSTANCE.getNMSClass("nbt.NBTCompressedStreamTools");

        /**
         * Reflection of the NBTCompressedStreamTools.a(NBTTagCompound, OutputStream) method.
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
         * Reflection of the NBTCompressedStreamTools.a(DataInput) method.
         * <br>
         * This method reads the NBTTagCompound from the DataInputStream.
         *
         * @param dataInputStream The DataInputStream to read from.
         * @return The NBTTagCompound.
         */
        public Object read(DataInputStream dataInputStream) {
            try {
                return CLASS.getMethod("a", DataInput.class).invoke(CLASS, dataInputStream);

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

        public final Class<?> CLASS = ReNMS.INSTANCE.getNMSClass("world.item.ItemStack");

        /**
         * Reflection of the ItemStack(NBTTagCompound) constructor.
         * <br>
         * This method creates a new ItemStack from the given NBTTagCompound.
         *
         * @param nbtTagCompound The NBTTagCompound to create the ItemStack from.
         * @return The ItemStack with the given NBTTagCompound.
         */
        public Object create(Object nbtTagCompound) {
            try {
                Constructor<?> ctor = CLASS.getDeclaredConstructor(ReNMS.INSTANCE.new NBTTagCompound().CLASS);
                ctor.setAccessible(true);
                return ctor.newInstance(nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Reflection of the ItemStack.b(NBTTagCompound) method.
         * <br>
         * This method deposits the given NBTTagCompound data into the ItemStack.
         *
         * @param itemStack      The ItemStack to save the NBTTagCompound into.
         * @param nbtTagCompound The NBTTagCompound to deposit.
         * @return The NBT-Filled ItemStack.
         */
        public Object save(Object itemStack, Object nbtTagCompound) {
            try {
                return itemStack.getClass().getMethod("b", ReNMS.INSTANCE.new NBTTagCompound().CLASS).invoke(itemStack, nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

}

