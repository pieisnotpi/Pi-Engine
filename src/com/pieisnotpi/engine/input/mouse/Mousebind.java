package com.pieisnotpi.engine.input.mouse;

public class Mousebind
{
    public int button;
    public boolean lastStatus = false, active = true;
    public MousePress pressHandler;
    public MouseHold holdHandler;
    public MouseRelease releaseHandler;

    public Mousebind(int button, MousePress press, MouseHold hold, MouseRelease release)
    {
        this.button = button;

        pressHandler = press;
        holdHandler = hold;
        releaseHandler = release;
    }

    public void press(int xPos, int yPos) throws Exception
    {
        if(pressHandler != null && !lastStatus) pressHandler.handle(xPos, yPos);
        lastStatus = true;
    }

    public void hold(int xPos, int yPos, float timeStep) throws Exception
    {
        if(holdHandler != null) holdHandler.handle(xPos, yPos, timeStep);
    }

    public void release(int xPos, int yPos) throws Exception
    {
        if(releaseHandler != null && lastStatus) releaseHandler.handle(xPos, yPos);
        lastStatus = false;
    }
}

