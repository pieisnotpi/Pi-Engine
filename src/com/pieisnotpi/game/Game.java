package com.pieisnotpi.game;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.output.Logger;
import org.lwjgl.opengl.GL;

public class Game
{
    public static void main(String[] args)
    {
        if(args.length > 0 && args[0].toLowerCase().equals("debug"))
        {
            PiEngine.debug = true;
            Logger.SYSTEM.log("Debug mode active");
        }

        System.setProperty("org.lwjgl.librarypath", "natives");

        PiEngine.start(new MainInstance());

        GL.destroy();
    }
}
