package com.pieisnotpi.engine.input.keyboard;

public class Keybind
{
    public int key;
    public boolean lastStatus = false, active = true;

    public KeyPress pressHandler;
    public KeyHold holdHandler;
    public KeyRelease releaseHandler;

    public Keybind(int key, KeyPress press, KeyHold hold, KeyRelease release)
    {
        this.key = key;

        pressHandler = press;
        holdHandler = hold;
        releaseHandler = release;
    }

    public void press()
    {
        if(pressHandler != null && !lastStatus) pressHandler.handle();
        lastStatus = true;
    }

    public void hold(float timeStep)
    {
        if(holdHandler != null) holdHandler.handle(timeStep);
    }

    public void release()
    {
        if(releaseHandler != null && lastStatus) releaseHandler.handle();
        lastStatus = false;
    }
}

