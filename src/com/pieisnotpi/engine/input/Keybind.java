package com.pieisnotpi.engine.input;

public class Keybind
{
    public int key;
    public boolean lastStatus = false, active = true;
    private InputHandler press, hold, release;

    public Keybind(int key, InputHandler onPress, InputHandler onHold, InputHandler onRelease)
    {
        this.key = key;

        press = onPress;
        hold = onHold;
        release = onRelease;
    }

    public void press()
    {
        if(press != null && !lastStatus) press.handle(1);
        lastStatus = true;
    }

    public void hold()
    {
        if(hold != null) hold.handle(1);
    }

    public void release()
    {
        if(release != null && lastStatus) release.handle(0);
        lastStatus = false;
    }
}
