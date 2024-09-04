package com.mrkelpy.bountyseekers.drivers;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This generic abstract class serves as the base class for all drivers, returning T
 * from its methods, and implementing one used to call the correct method based on
 * the version provided and the method annotations.
 * @param <T> The return value of every method in the driver
 */
public abstract class FeatureDriver<T> {

    /**
     * Works intimately with DriverVersionTarget.
     * Reflectively searches the class for all methods annotated with a target version,
     * and calls the downwards closest one relative to the current version
     * @return The return value from the called method
     */
    public T call() {

        String version = this.getVersion();
        // TODO: Add logic

        return null;
    }

    /**
     * Gets the version of the server currently being used by extracting it
     * with regex from Bukkit.
     * @return A XX.XX.XX formatted version string
     */
    protected String getVersion() {

        String versionInformation = Bukkit.getVersion();

        Pattern regex = Pattern.compile("(?<=MC: ).*(?=\\))");
        Matcher match = regex.matcher(versionInformation);
        return match.group(1);
    }

}
