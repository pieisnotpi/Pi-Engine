package com.pieisnotpi.engine.rendering.ui.text.effects;

import com.pieisnotpi.engine.rendering.ui.text.Text;

public class SpinEffect implements TextEffect
{
    float speed, acceleration;
    int axis = 0;

    public SpinEffect(float speed, float acceleration, int axis)
    {
        this.speed = speed;
        this.acceleration = acceleration;
        this.axis = axis;
    }

    public void process(Text text)
    {
        if(axis == 0) text.addToXRot(speed += acceleration);
        else if(axis == 1) text.addToYRot(speed += acceleration);
        else if(axis == 2) text.addToZRot(speed += acceleration);
    }
}
