package com.pieisnotpi.engine.input;

public class Joybind
{
    public int joystick, axis;
    public boolean isButton, lastStatus, enabled = true;
    private InputHandler press, hold, release;

    public Joybind(int joystick, int axis, boolean isButton, InputHandler onPress, InputHandler onHold, InputHandler onRelease)
    {
        this.joystick = joystick;
        this.axis = axis;
        this.isButton = isButton;

        press = onPress;
        hold = onHold;
        release = onRelease;
    }

    public void press()
    {
        if(press != null && !lastStatus) press.handle(1);
        lastStatus = true;
    }

    public void hold(float value)
    {
        if(hold != null) hold.handle(value);
    }

    public void release()
    {
        if(release != null && lastStatus) release.handle(0);
        lastStatus = false;
    }
}
