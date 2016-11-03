package com.pieisnotpi.game;

import com.pieisnotpi.engine.PiEngine;
import org.lwjgl.opengl.GL;

public class Game
{
    public static void main(String[] args)
    {
        for(String arg : args)
        {
            switch(arg.toLowerCase())
            {
                case "debug": PiEngine.debug = true; break;
                case "l_debug": PiEngine.lwjgl_debug = true; break;
                case "gl_debug": PiEngine.gl_debug = true; break;
            }
        }

        if(System.getProperty("org.lwjgl.librarypath") == null) System.setProperty("org.lwjgl.librarypath", "natives");

        PiEngine.start(new MainInstance());

        GL.destroy();
    }
}
