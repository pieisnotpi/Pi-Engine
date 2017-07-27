package com.pieisnotpi.test;

import com.pieisnotpi.engine.PiEngine;

public class Game
{
    public static void main(String[] args)
    {
        if(System.getProperty("org.lwjgl.librarypath") == null) System.setProperty("org.lwjgl.librarypath", "natives");

        PiEngine.start(new MainInstance());
    }
}
