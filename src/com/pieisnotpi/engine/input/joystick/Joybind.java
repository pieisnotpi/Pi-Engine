package com.pieisnotpi.engine.input.joystick;

public class Joybind
{
    public int joystick, axis;
    public boolean isButton, lastStatus, enabled = true;

    public JoyPress pressHandler;
    public JoyHold holdHandler;
    public JoyRelease releaseHandler;

    public Joybind(int joystick, int axis, boolean isButton, JoyPress press, JoyHold hold, JoyRelease release)
    {
        this.joystick = joystick;
        this.axis = axis;
        this.isButton = isButton;

        pressHandler = press;
        holdHandler = hold;
        releaseHandler = release;
    }

    public void press() throws Exception
    {
        if(pressHandler != null && !lastStatus) pressHandler.handle();
        lastStatus = true;
    }

    public void hold(float value, float timeStep) throws Exception
    {
        if(holdHandler != null) holdHandler.handle(value, timeStep);
    }

    public void release() throws Exception
    {
        if(releaseHandler != null && lastStatus) releaseHandler.handle();
        lastStatus = false;
    }
}

