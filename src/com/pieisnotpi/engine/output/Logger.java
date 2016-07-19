package com.pieisnotpi.engine.output;

import com.pieisnotpi.engine.PiEngine;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger
{
    public static final Logger TEXTURES = new Logger("TEXTURES"), SHADER_COMPILER = new Logger("SHADER_COMPILER"), SHADER_PROGRAM = new Logger("SHADER_PROGRAM"), SYSTEM = new Logger("SYSTEM"), AUDIO = new Logger("Audio");

    private String identity;
    private static final String LOG = "LOG", ERR = "ERR", DEBUG = "DEBUG", DEBUG_ERR = "DEBUG_ERR";

    /**
     * logFormat: String used to determine the format that the logger uses
     * timeFormat: String used with var dateFormat
     * dateFormat: Date formatter used for parameter %3
     * %1 == Message type (I.E. Debug/Error/Log/etc.)
     * %2 == Logger Identity (Identity set at initialization)
     * %3 == Current Time (Current system time, uses String 'timeFormat')
     * %4 == Message (Message given when logging something)
     */

    private static String logFormat = "[%2|%3|%1]: %4", timeFormat = "kk:mm:ss";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat);

    public Logger(String identity)
    {
        this.identity = identity;
    }

    // Standard log
    public void log(String message)
    {
        System.out.println(format(LOG, message));
    }

    // Error log
    public void err(String message)
    {
        System.err.println(format(ERR, message));
    }

    // Debug log, only logs if debug mode is on
    public void debug(String message)
    {
        if(PiEngine.debug) System.out.println(format(DEBUG, message));
    }

    // Debug error log, only logs if debug mode is on
    public void debugErr(String message)
    {
        if(PiEngine.debug) System.err.println(format(DEBUG_ERR, message));
    }

    // Formats a message for printing
    private String format(String type, String message)
    {
        return logFormat.replaceAll("%1", type).replaceAll("%2", identity).replaceAll("%3", dateFormat.format(Calendar.getInstance().getTime())).replaceAll("%4", message);
    }

    // Sets the loggers format
    public static void setLogFormat(String format)
    {
        logFormat = format;
    }

    // Sets the time format
    public static void setTimeFormat(String format)
    {
        timeFormat = format;
        dateFormat.applyPattern(timeFormat);
    }
}
