package com.pieisnotpi.game;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.output.Logger;
import org.lwjgl.opengl.GL;

public class Game
{
    public static void main(String[] args)
    {
        for(String arg : args)
        {
            String l = arg.toLowerCase();

            if(l.equals("debug")) { PiEngine.debug = true; Logger.SYSTEM.log("Debug mode active"); }
            else if(l.equals("l_debug")) { System.setProperty("org.lwjgl.util.Debug", "true"); Logger.SYSTEM.log("LWJGL debug mode active"); }
        }

        System.setProperty("org.lwjgl.librarypath", "natives");

        PiEngine.start(new MainInstance());

        GL.destroy();
    }
}
