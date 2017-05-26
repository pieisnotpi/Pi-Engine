package com.pieisnotpi.engine.utility;

import com.pieisnotpi.engine.output.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileUtility
{
    public static final Logger logger = new Logger("FILE_LOADER");
    
    /**
     * Finds a file, checking both external (real) and internal (JAR/compiled code) directories
     * Inside directories are relative to the given class
     * @param path The supplied path of the file
     * @param relative The relative class for internal directories
     * @return The file input stream. Returns null if not found.
     */
    
    public static InputStream findStream(String path, Class relative)
    {
        path = path.replaceAll("\\\\", "/");
        InputStream s = relative.getResourceAsStream(path);
        
        if(s == null && path.charAt(0) != '/') s = relative.getResourceAsStream("/" + path);
        if(s == null)
            try { s = new FileInputStream(path); }
            catch(FileNotFoundException e) { Logger.SYSTEM.err("File not found " + path); }
    
        return s;
    }
    
    public static File findFile(String path)
    {
        path = path.replaceAll("\\\\", "/");
        File s;
        try
        {
            s = new File(FileUtility.class.getResource(path).toURI());
            if(!s.exists() && path.charAt(0) != '/')
                s = new File(FileUtility.class.getResource("/" + path).toExternalForm());
            if(!s.exists()) s = new File(path);
        }
        catch(Exception e) { s = new File(path); }

        if(!s.exists()) return null;
        else return s;
    }
}
