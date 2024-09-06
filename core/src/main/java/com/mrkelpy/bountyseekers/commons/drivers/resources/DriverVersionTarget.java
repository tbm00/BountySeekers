package com.mrkelpy.bountyseekers.commons.drivers.resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation serves as an indicator of which version a method inside a Driver
 * class is best suited for.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DriverVersionTarget {
    String value();
}
