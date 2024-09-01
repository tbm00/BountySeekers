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


}


