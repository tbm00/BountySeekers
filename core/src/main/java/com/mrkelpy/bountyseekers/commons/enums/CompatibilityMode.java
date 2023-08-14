package com.mrkelpy.bountyseekers.commons.enums;

import com.mrkelpy.bountyseekers.interfaces.IBukkitReflector;
import com.mrkelpy.bountyseekers.interfaces.INMSReflector;

/**
 * This enum allows for any version of the plugin to declare which NMS and Bukkit
 * version it is MINIMALLY compatible with. Every different compatibility version will be represented
 * in this enum, mapped to the two Reflector objects that it should use.
 * <br>
 * <font color="aqua">Minimal Compatibility Example: 1.9 uses the v1_7 compatibility because there were no changes in NMS between those versions. </font>
 */
public enum CompatibilityMode {

    /**
     * ENUM DECLARATION
     */
    v1_18(com.mrkelpy.bountyseekers.reflectors.v1_17.ReBukkit.INSTANCE,
            com.mrkelpy.bountyseekers.reflectors.v1_18.ReNMS.INSTANCE), // v1_17 Bukkit and v1_18 NMS reflectors

    v1_17(com.mrkelpy.bountyseekers.reflectors.v1_17.ReBukkit.INSTANCE,
            com.mrkelpy.bountyseekers.reflectors.v1_17.ReNMS.INSTANCE), // v1_17 Bukkit and NMS reflectors

    v1_11(com.mrkelpy.bountyseekers.reflectors.v1_7.ReBukkit.INSTANCE,
            com.mrkelpy.bountyseekers.reflectors.v1_11.ReNMS.INSTANCE),  // v1_7 Bukkit and v1_11 NMS reflectors

    v1_7(com.mrkelpy.bountyseekers.reflectors.v1_7.ReBukkit.INSTANCE,
            com.mrkelpy.bountyseekers.reflectors.v1_7.ReNMS.INSTANCE),  // v1_7 Bukkit and NMS reflectors

    vCR1_7_10(com.mrkelpy.bountyseekers.reflectors.vCR1_7_10.ReBukkit.INSTANCE,
            com.mrkelpy.bountyseekers.reflectors.vCR1_7_10.ReCrucibleNMS.INSTANCE);  // vCR1_7_10 Bukkit and NMS Reflectors

    private final IBukkitReflector bukkitReflector;
    private final INMSReflector nmsReflector;

    CompatibilityMode(IBukkitReflector bukkitReflector, INMSReflector nmsReflector) {
        this.bukkitReflector = bukkitReflector;
        this.nmsReflector = nmsReflector;
    }

    /**
     * Gets the IBukkitReflector object that should be used for the compatibility mode.
     */
    public IBukkitReflector getBukkitReflector() {
        return bukkitReflector;
    }

    /**
     * Gets the INMSReflector object that should be used for the compatibility mode.
     */
    public INMSReflector getNMSReflector() {
        return nmsReflector;
    }

}
