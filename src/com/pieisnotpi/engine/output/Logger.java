package com.pieisnotpi.engine.output;

import com.pieisnotpi.engine.PiEngine;

public class Logger
{
    /**
     * Uses standardized String.format formatting, thus arguments must formatted '%n$s'
     * where n is an argument index corresponding to the following, i.e. '%1$s' for argument #1
     *
     * #1 == Message type (I.E. Debug/Error/Log/etc.)
     * #2 == Logger Identity (Identity set at initialization)
     * #3 == Current Time (Current system time, uses String 'timeFormat')
     * #4 == Message (Message given when logging something)
     * #5 == GL Instance
     *
     * logFormat: String used to determine the format that the logger uses
     * timeFormat: String used with var dateFormat
     */

    private static String standardLogFormat = "[%2$s|%3$s|%1$s]: %4$s", standardTimeFormat = "%1$tH:%1$tM:%1$tS";

    private static final String LOG = "LOG", ERR = "ERR", DEBUG = "DEBUG", DEBUG_ERR = "DEBUG_ERR";
    public static final Logger OPENGL = new Logger("OPENGL"), SYSTEM = new Logger("SYSTEM"), AUDIO = new Logger("AUDIO");

    private String identity, logFormat, timeFormat;

    public Logger(String identity)
    {
        this.identity = identity;
        logFormat = standardLogFormat;
        timeFormat = standardTimeFormat;
    }

    // Standard log
    public void log(String message)
    {
        log(message, LOG);
    }
    
    // Standard log, w/ type
    public void log(String message, String type)
    {
        System.out.println(format(type, message));
    }

    // Error log
    public void err(String message)
    {
        err(message, ERR);
    }
    
    // Error log, w/ type
    public void err(String message, String type)
    {
        System.err.println(format(type, message));
    }

    // Debug log, only logs if debug mode is on
    public void debug(String message)
    {
        debug(message, DEBUG);
    }
    
    // Debug log, only logs if debug mode is on, w/ type
    public void debug(String message, String type)
    {
        if(PiEngine.debug) System.out.println(format(type, message));
    }

    // Debug error log, only logs if debug mode is on
    public void debugErr(String message)
    {
        debugErr(message, DEBUG_ERR);
    }
    
    // Debug error log, only logs if debug mode is on, w/ type
    public void debugErr(String message, String type)
    {
        if(PiEngine.debug) System.err.println(format(type, message));
    }
    
    public void debugStacktrace()
    {
        if(!PiEngine.debug) return;
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        err("Printing stack trace");
        for(int i = 2; i < stack.length; i++) { System.err.printf("\tat %s%n", stack[i]); }
    }

    public void setLogFormat(String format)
    {
        if(format != null) logFormat = format;
    }
    
    public void setTimeFormat(String format)
    {
        if(format != null) timeFormat = format;
    }

    // Formats a message for printing
    private String format(String type, String message)
    {
        return String.format(logFormat, type, identity, getTime(), message, PiEngine.glInstance != null ? PiEngine.glInstance.window.handle + "" : "NONE");
    }

    private String getTime()
    {
        return String.format(timeFormat, System.currentTimeMillis());
    }

    // Sets the logger's format
    public static void setStandardLogFormat(String format)
    {
        if(format != null) standardLogFormat = format;
    }

    // Sets the logger's format
    public static void setStandardTimeFormat(String format)
    {
        if(format != null) standardTimeFormat = format;
    }
}
