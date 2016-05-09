package com.pieisnotpi.engine.rendering.ui.text.effects;

import com.pieisnotpi.engine.rendering.shapes.types.text.TextQuad;
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
        float cx = text.getWidth()/2 + text.getX(), cy = (text.getHeight()/2) + text.getY(), cz = text.getZ();

        if(axis == 0) for(TextQuad c : text.getChars()) c.rotateX(speed += acceleration, cy, cz);
        else if(axis == 1) for(TextQuad c : text.getChars()) c.rotateY(speed += acceleration, cx, cz);
        else if(axis == 2) for(TextQuad c : text.getChars()) c.rotateZ(speed += acceleration, cx, cy);
    }
}
