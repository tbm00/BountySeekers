package com.mrkelpy.bountyseekers.drivers.resources;

import com.mrkelpy.bountyseekers.commons.MinecraftVersion;
import com.mrkelpy.bountyseekers.commons.utils.PluginConstants;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This generic abstract class serves as the base class for all drivers, returning T
 * from its methods, and implementing one used to call the correct method based on
 * the version provided and the method annotations.
 *
 * Any classes inheriting from this one will be called "Drivers" and their methods, "Handlers".
 *
 * @param <T> The return value of every method in the driver
 */
public abstract class FeatureDriver<T> {

    /**
     * Works intimately with DriverVersionTarget.
     * Reflectively searches the class for all methods annotated with a target version,
     * and calls the downwards closest one relative to the current version
     *
     * @return The return value from the called method
     */
    public T call(Object... args) {

        // Gets the basic resources for
        String version = this.getVersion();
        SortedMap<MinecraftVersion, Method> handlers = this.mapVersionsToHandlers();
        MinecraftVersion mcVersion = new MinecraftVersion(version);

        // If the key doesn't exist inside the handlers list, add it in as a pivot
        if (!handlers.containsKey(mcVersion)) {
            handlers.put(mcVersion, null);
        }

        // Gets the index of a key with the same version as the current one
        MinecraftVersion key = (MinecraftVersion) handlers.keySet().stream().filter(x -> x.getVersion().equals(version)).toArray()[0];
        int keyIndex = handlers.keySet().stream().collect(Collectors.toList()).indexOf(key);

        // If not, get the previous key and call its method.
        MinecraftVersion previousKey = handlers.keySet().stream().collect(Collectors.toList()).get(--keyIndex < 0 ? 0 : keyIndex);
        Method handler = handlers.get(previousKey);

        // If the index is 0 with no handler available, then return null.
        if (keyIndex == 0 && handler == null) return null;

        try {
            return (T) handler.invoke(this, new Object[] {args});

        // If there's any issue with the called invocation, log it and return null
        } catch (IllegalAccessException | InvocationTargetException e) {
            PluginConstants.LOGGER.severe("Could not call driver for '" +
                    this.getClass().getName() + "' on version '" + this.getVersion() + "'");

            PluginConstants.LOGGER.severe(e.getCause().toString());
            PluginConstants.LOGGER.severe(e.toString());
            return null;
        }
    }

    /**
     * Gets the version of the server currently being used by extracting it
     * with regex from Bukkit.
     *
     * @return A XX.XX.XX formatted version string
     */
    protected String getVersion() {

        String versionInformation = Bukkit.getVersion();

        Pattern regex = Pattern.compile("(?<=MC: ).*(?=\\))");
        Matcher match = regex.matcher(versionInformation);
        match.find();

        return match.group(0);
    }

    /**
     * Collects all the version targeted methods in the driver class by their annotations and
     * creates an mc-accurate sorted map, mapping the versions to their methods, ascending.
     *
     * @return A sorted map with the registered mc versions of the handlers mapped to their methods.
     */
    protected SortedMap<MinecraftVersion, Method> mapVersionsToHandlers() {

        // Filters all the methods in the class removing non-annotated ones
        Method[] handlerMethods = Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(DriverVersionTarget.class))
                .toArray(Method[]::new);

        // Map the minecraft versions to their respective methods
        SortedMap<MinecraftVersion, Method> handlerMappings = new TreeMap<MinecraftVersion, Method>();

        for (int i = 0; i < handlerMethods.length; i++) {

            // Goes through every method and adds it to the mapping
            Method handler = handlerMethods[i];
            DriverVersionTarget annotation = handler.getAnnotation(DriverVersionTarget.class);
            MinecraftVersion version = new MinecraftVersion(annotation.value());

            handlerMappings.put(version, handler);
        }

        return handlerMappings;
    }

}
