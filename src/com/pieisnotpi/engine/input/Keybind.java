package com.pieisnotpi.engine.input;

public class Keybind
{
    public int key;
    public boolean allowHolding, prevStatus = false, active = true;
    public Object parent;
    private InputHandler press, release;

    public Keybind(int key, boolean allowHolding, InputHandler onPress, InputHandler onRelease)
    {
        this.key = key;
        this.allowHolding = allowHolding;

        parent = null;
        press = onPress;
        release = onRelease;
    }

    public void press()
    {
        if(press != null)
        press.handle(1);
    }

    public void release()
    {
        if(release != null)
        release.handle(0);
    }
}
