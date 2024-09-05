package com.mrkelpy.bountyseekers.commons.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class implements useful methods to use when logging stuff
 */
public class LoggingUtils {

    /**
     * Gets the actual stack trace from a given exception, as if had been thrown.
     * @param e The exception to get the stack trace from
     * @return The stack trace as a string
     */
    public static String getStackTrace(Exception e) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        e.printStackTrace(pw);
        return sw.toString();
    }

}
