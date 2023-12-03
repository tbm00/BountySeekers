package com.mrkelpy.bountyseekers.commons.utils;

import com.mrkelpy.bountyseekers.commons.enums.CompatibilityMode;
import com.mrkelpy.bountyseekers.interfaces.IBukkitReflector;
import com.mrkelpy.bountyseekers.interfaces.INMSReflector;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

/**
 * This class implements a series of methods useful for serializing/deserializing objects used
 * in Minecraft.
 * <br>
 */
@SuppressWarnings("unused")
public class SerializationUtils {

    private final IBukkitReflector.CraftItemStack RCraftItemStack;
    private final INMSReflector.ItemStack RItemStack;
    private final INMSReflector.NBTTagCompound RNBTTagCompound;
    private final INMSReflector.NBTCompressedStreamTools RNBTCompressedStreamTools;

    /**
     * Main constructor for the SerializationUtils class. This constructor obtains the Bukkit and NMS reflectors
     * from the compatibility mode enum and loads the necessary classes into properties to be used for serialization.
     *
     * @param compatibilityMode The compatibility mode enum to use for the SerializationUtils object.
     */
    public SerializationUtils(CompatibilityMode compatibilityMode) {
        IBukkitReflector bukkitReflector = compatibilityMode.getBukkitReflector();
        INMSReflector nmsReflector = compatibilityMode.getNMSReflector();

        this.RCraftItemStack = bukkitReflector.getCraftItemStack();
        this.RItemStack = nmsReflector.getItemStack();
        this.RNBTTagCompound = nmsReflector.getNBTTagCompound();
        this.RNBTCompressedStreamTools = nmsReflector.getNBTCompressedStreamTools();
    }

    /**
     * Converts an ItemStack into a sign-magnitude string of base32. This is a type of serialization that not only is smaller
     * than just serializing the inventory to JSON, but also cleaner, and one that retains all the information.
     *
     * @param item The ItemStack to convert.
     * @return A sign-magnitude string of base32 string representing the ItemStack.
     */
    public String itemStackToMagBase32(ItemStack item) {

        if (item == null) return null;

        // Opens a ByteArrayOutputStream and a DataOutputStream to write the data into.
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             DataOutputStream dataOutput = new DataOutputStream(outputStream)) {

            // Converts the ItemStack into an NMS copy (Thus preserving all the NBT data) and saves it into the DataOutputStream.
            Object ReNMSItem = RCraftItemStack.asNMSCopy(item);
            RNBTCompressedStreamTools.write(RItemStack.save(ReNMSItem, RNBTTagCompound.create()), dataOutput);

            // Encode the bytearray from the DataOutputStream into a sign-magnitude integer, and convert it to a base32 string.
            // We can't use base64 here because the number of bits would not be enough to decode later.
            return new BigInteger(1, outputStream.toByteArray()).toString(32);

        } catch (IOException | NullPointerException e) {
            // If an error occurs for some reason, just return null.
            return null;
        }
    }

    /**
     * Converts a sign-magnitude string of base32 into an ItemStack. This method expects a base32 string coming from
     * {@link #itemStackToMagBase32(ItemStack)}.
     *
     * @param data The base32 string to convert.
     * @return The ItemStack represented by the base32 string.
     */
    public ItemStack itemStackFromMagBase32(String data) {

        // Opens a ByteArrayInputStream and a DataInputStream to read the bytes from the base32 string.
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());
             DataInputStream dataInput = new DataInputStream(inputStream)) {

            // Reads the data from the DataInputStream, in the form of an NBT compound, and creates an ItemStack from it.
            Object itemData = RNBTCompressedStreamTools.read(dataInput);
            Object ReNMSItemStack = RItemStack.create(itemData);

            // Return a bukkit copy of the ReNMS itemStack.
            return RCraftItemStack.asBukkitCopy(ReNMSItemStack);

        } catch (IOException | NullPointerException e) {
            // If an error occurs for some reason, just return null.
            return null;
        }
    }

    /**
     * Converts an itemStack[] into a base64 string. This is a shortcut for manually converting each item through
     * {@link #itemStackToMagBase32(ItemStack)} and then concatenating them together.
     *
     * @param stackArray The itemStack[] to convert.
     * @return A base64 string representing the ItemStack[].
     */
    public String itemStackArrayToBase64(ItemStack[] stackArray) {

        // Open a ByteArrayOutputStream and an ObjectOutputStream to write the data into.
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(stackArray.length);  // Write the size of the inventory into the stream, so it can be decoded later

            // Loop through each item in the inventory and convert it to a base32 string.
            for (ItemStack itemStack : stackArray) {
                String base64ItemStack = this.itemStackToMagBase32(itemStack);

                // Check if the base32 string is null and if not, write it to the stream.
                if (base64ItemStack != null) {
                    dataOutput.writeObject(base64ItemStack);
                    continue;
                }

                // Writes a null string to the stream if the base64 string is null.
                dataOutput.writeObject(null);
            }

            dataOutput.close();
            // Return the base64 string of the inventory. We can use base64 here, since everything is pretty much already handled
            // by the Mag32 strings, so let's do it.
            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (IOException e) {
            // If an error occurs for some reason, just return null.
            return null;
        }
    }

    /**
     * Converts a base64 string into an itemStack[]. This is a shortcut for manually converting each item through
     * {@link #itemStackFromMagBase32(String)}, and is meant to be used with inventories.
     *
     * @param data The base64 string to convert.
     * @return The ItemStack[] represented by the base64 string.
     */
    public ItemStack[] itemStackArrayFromBase64(String data) {

        // Open a ByteArrayInputStream and an ObjectInputStream to read the data from the base64 string.
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            // Read the size of the inventory from the stream, and create an instance of PlayerInventory with that size.
            ItemStack[] inventoryContents = new ItemStack[dataInput.readInt()];

            // Loop through each item in the inventory and convert them from a base64 string.
            for (int i = 0; inventoryContents.length > i; i++) {
                Object base64Data = dataInput.readObject();

                // If the itemStack is not null, set the item in the inventory at the current index to the itemStack.
                if (base64Data != null)
                    inventoryContents[i] = this.itemStackFromMagBase32((String) base64Data);
            }

            dataInput.close();
            return inventoryContents;

        } catch (IOException | ClassNotFoundException e) {
            // If an error occurs for some reason, just return null.
            return null;
        }
    }

}


