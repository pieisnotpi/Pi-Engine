package com.pieisnotpi.engine.input;

public class Joybind
{
    public int joystick, axis;
    public boolean isButton, allowHolding, enabled = true;
    public float lastValue = -100;
    public Object parent;
    private InputHandler press, release;

    public Joybind(int joystick, int axis, boolean isButton, boolean allowHolding, InputHandler onPress, InputHandler onRelease)
    {
        this.joystick = joystick;
        this.axis = axis;
        this.isButton = isButton;
        this.allowHolding = allowHolding;

        parent = null;
        press = onPress;
        release = onRelease;
    }

    public void setPress(InputHandler handler)
    {
        press = handler;
    }

    public void setRelease(InputHandler handler)
    {
        release = handler;
    }

    public void press(float value)
    {
        if(press != null)
        press.handle(value);
    }

    public void release()
    {
        if(release != null)
        release.handle(0);
    }
}
