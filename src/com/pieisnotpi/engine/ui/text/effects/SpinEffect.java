package com.pieisnotpi.engine.ui.text.effects;

import com.pieisnotpi.engine.ui.text.Text;
import org.joml.Vector3f;

public class SpinEffect implements TextEffect
{
    private Vector3f speed, acceleration;
    private Text text;

    public SpinEffect(Vector3f speed, Vector3f acceleration)
    {
        this.speed = speed;
        this.acceleration = acceleration;
    }

    @Override
    public void setText(Text text)
    {
        this.text = text;
    }

    @Override
    public void process()
    {
        text.getTransform().rotateDegrees(speed.x += acceleration.x, speed.y += acceleration.y, speed.z += acceleration.z);
    }
}
