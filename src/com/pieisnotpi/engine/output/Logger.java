package com.pieisnotpi.engine.output;

import com.pieisnotpi.engine.PiEngine;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger
{
    /**
     * logFormat: String used to determine the format that the logger uses
     * timeFormat: String used with var dateFormat
     * dateFormat: Date formatter used for parameter %3
     * %1 == Message type (I.E. Debug/Error/Log/etc.)
     * %2 == Logger Identity (Identity set at initialization)
     * %3 == Current Time (Current system time, uses String 'timeFormat')
     * %4 == Message (Message given when logging something)
     * %5 == GL Instance
     */

    public static String standardLogFormat = "[%2|%3|%1]: %4", timeFormat = "kk:mm:ss";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat);

    private static final String LOG = "LOG", ERR = "ERR", DEBUG = "DEBUG", DEBUG_ERR = "DEBUG_ERR";
    public static final Logger TEXTURES = new Logger("TEXTURES"), OPENGL = new Logger("OPENGL"), SHADER_COMPILER = new Logger("SHADER_COMPILER"), SHADER_PROGRAM = new Logger("SHADER_PROGRAM"), SYSTEM = new Logger("SYSTEM"), AUDIO = new Logger("Audio");

    private String identity, logFormat;

    public Logger(String identity)
    {
        this.identity = identity;
        logFormat = standardLogFormat;
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

    public void setLogFormat(String format)
    {
        if(format != null) logFormat = format;
    }

    // Formats a message for printing
    private String format(String type, String message)
    {
        return logFormat.replaceAll("%1", type)
                        .replaceAll("%2", identity)
                        .replaceAll("%3", dateFormat.format(Calendar.getInstance().getTime()))
                        .replaceAll("%4", message)
                        .replaceAll("%5", PiEngine.glInstance != null ? PiEngine.glInstance.windowHandle + "" : "NONE");
    }

    // Sets the loggers format
    public static void setStandardLogFormat(String format)
    {
        if(format != null) standardLogFormat = format;
    }

    // Sets the time format
    public static void setTimeFormat(String format)
    {
        if(format != null)
        {
            timeFormat = format;
            dateFormat.applyPattern(timeFormat);
        }
    }
}
