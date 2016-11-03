package com.pieisnotpi.engine.input;

public class Mousebind
{
    public int button;
    public boolean lastStatus = false, active = true;
    private InputHandler press, hold, release;

    public Mousebind(int button, InputHandler onPress, InputHandler onHold, InputHandler onRelease)
    {
        this.button = button;

        press = onPress;
        hold = onHold;
        release = onRelease;
    }

    public void press()
    {
        if(press != null && !lastStatus) press.handle(1, 1);
        lastStatus = true;
    }

    public void hold(float timeStep)
    {
        if(hold != null) hold.handle(1, timeStep);
    }

    public void release()
    {
        if(release != null && lastStatus) release.handle(0, 1);
        lastStatus = false;
    }
}
