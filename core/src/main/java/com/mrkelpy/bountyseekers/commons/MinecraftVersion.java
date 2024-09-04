package com.mrkelpy.bountyseekers.commons;

/**
 * A class representing a minecraft version. Useful for comparisons.
 */
public class MinecraftVersion implements Comparable {

    /**
     * The raw version string used inside the class.
     */
    private String version;

    /**
     * Main constructor for the MinecraftVersion class. Takes in a version
     * to be handled internally.
     * @param version The version being represented. Formatted as XX.XX.XX;
     *                Does not handle snapshots.
     */
    public MinecraftVersion(String version) {
        this.version = version;
    }

    /**
     * @return The raw version stored in the class
     */
    public String getVersion() {
        return this.version;
    }


    /**
     * Comparable implementation; Used to compare versions such that 1.7.10 < 1.20.1
     * @param version the object to be compared.
     * @return The comparison value
     */
    @Override
    public int compareTo(Object version) {

        // Gets the partitions of both versions
        int[] mainVersion = this.convertToIntArray(this.version.split("."));
        int[] comparisonVersion = this.convertToIntArray(this.version.split("."));

        // Compare each main partition in cascade, checking if any of its parts are
        // either greater or lesser than comparison one.
        if (mainVersion[0] > comparisonVersion[0]) return 1;
        if (mainVersion[0] < comparisonVersion[0]) return -1;

        if (mainVersion[1] > comparisonVersion[1]) return 1;
        if (mainVersion[1] < comparisonVersion[1]) return -1;

        if (mainVersion[2] > comparisonVersion[2]) return 1;
        if (mainVersion[2] < comparisonVersion[2]) return -1;

        // If the method still hasn't returned, the versions are the same
        return 0;
    }

    /**
     * Converts a String[] into an int[], which is extremely useful for version comparisons
     * @param versionPartitions The String[] of version parts to convert
     * @return The int[] equivalent to the input array.
     */
    private int[] convertToIntArray(String[] versionPartitions) {

        int[] intArray = new int[versionPartitions.length];

        // Iterate over all the version partitions and parse them into ints
        for (int i = 0; i < versionPartitions.length; i++) {
            intArray[i] = Integer.parseInt(versionPartitions[i]);
        }

        return intArray;
    }
}
