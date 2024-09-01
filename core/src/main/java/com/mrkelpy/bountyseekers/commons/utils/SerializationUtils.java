package com.mrkelpy.bountyseekers.commons.utils;

import com.mrkelpy.bountyseekers.commons.enums.CompatibilityMode;
import com.mrkelpy.bountyseekers.interfaces.IBukkitReflector;
import com.mrkelpy.bountyseekers.interfaces.INMSReflector;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Base64;

/**
 * This class implements a series of methods useful for serializing/deserializing objects used
 * in Minecraft into base64.
 */
@SuppressWarnings("unused")
public class SerializationUtils {

    /**
     * Turns the actual ItemStack's class into a bytearray that is then encoded into base64.
     * @param item The item to encode
     * @return A base64 string representing the item
     */
    public static String itemToBase64(ItemStack item) {

        try {
            // Creates the conversion streams and writes the item into it
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream objectStream = new BukkitObjectOutputStream(byteStream);
            objectStream.writeObject(item);
            objectStream.flush();

            // Gets the byte[] from the byte stream
            byte[] itemBytes = byteStream.toByteArray();
            String base64 = new String(Base64.getEncoder().encode(itemBytes));

            return base64;
        }

        catch (IOException e) {
            PluginConstants.LOGGER.warning("Could not encode item into base64.: " + e.getMessage());
            return "";
        }
    }

    /**
     * Decodes the provided base64 string and turns it into an ItemStack object
     * @param item The base64 string to decode into an item
     * @return An ItemStack perfectly copying the encoded item
     */
    public static ItemStack itemFromBase64(String base64) {

        try {
            // Decodes the base64 string into bytes and starts the decoding streams
            byte[] itemBytes = Base64.getDecoder().decode(base64);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(itemBytes);
            BukkitObjectInputStream objectStream = new BukkitObjectInputStream(inputStream);

            // Reads the object from bytes and returns the ItemStack
            ItemStack item = (ItemStack) objectStream.readObject();
            return item;
        }

        catch (IOException | ClassNotFoundException e) {
            PluginConstants.LOGGER.warning("Could not decode base64 string into item.: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converts an ItemStack[] into a base64 string by encoding all the inner ItemStacks into
     * base64, and then using a DataStream wrapping a ByteArrayStream to get the global base64.
     * @param stack The item stack to be encoded
     * @return The base64 string representing the encoded ItemStack[]
     */
    public static String itemStackArrayToBase64(ItemStack[] stack) {

        try {
            // Initialises the conversion streams
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            DataOutputStream dataStream = new DataOutputStream(byteStream);

            // Writes the stack length into the data stream, followed by iterating over
            // All the item stacks, adding their base64 into the mix aswell.
            dataStream.writeInt(stack.length);

            for (int i = 0; i < stack.length; i++) {
                dataStream.writeUTF(itemToBase64(stack[i]));
            }

            // Converts the underlying byte stream into base64 and returns it
            return new String(Base64.getEncoder().encode(byteStream.toByteArray()));

        } catch (IOException e) {
            PluginConstants.LOGGER.warning("Could not encode item stack array into base64.: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converts a base64 string into an ItemStack[], just by doing the inverse of the method above.
     * @param base64 The base64 string to be decoded into an ItemStack[]
     * @return The ItemStack[] encoded into the base64 string
     */
    public static ItemStack[] itemStackArrayFromBase64(String base64) {

        try {

            // Decodes the base64 data from the string and initialises the conversion streams
            byte[] itemBytes = Base64.getDecoder().decode(base64);
            ByteArrayInputStream byteStream = new ByteArrayInputStream(itemBytes);
            DataInputStream dataStream = new DataInputStream(byteStream);

            // Gets the amount of items in the stack
            int stackSize = dataStream.readInt();
            ItemStack[] result = new ItemStack[stackSize];

            // Iterates over the stack, converting the base64 into items and returning it
            for (int i = 0; i < stackSize; i++) {
                result[i] = itemFromBase64(dataStream.readUTF());
            }

            return result;

        } catch (IOException e) {
            PluginConstants.LOGGER.warning("Could not decode the base64 string into an item stack array.: " + e.getMessage());
            return null;
        }
    }


}


