package com.mrkelpy.bountyseekers.drivers;

/**
 * This annotation serves as an indicator of which version a method inside a Driver
 * class is best suited for.
 */
public @interface DriverVersionTarget {
    String version();
}
