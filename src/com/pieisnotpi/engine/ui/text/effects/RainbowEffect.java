package com.pieisnotpi.engine.ui.text.effects;

import com.pieisnotpi.engine.ui.text.Text;
import com.pieisnotpi.engine.utility.Color;

public class RainbowEffect implements TextEffect
{
    private float speed, x;
    private Color c = new Color(0, 0, 0);
    private Text text;

    public RainbowEffect(float speed)
    {
        this.speed = speed;
    }

    public void setText(Text text)
    {
        this.text = text;
    }

    public void process(float timeStep)
    {
        float difference = 3f/text.chars.size();

        x -= speed*timeStep;
        if(x < 0) x = 3;

        for(int i = 0; i < text.chars.size(); i++)
        {
            float r = 0, g = 0, b = 0;
            float color = x + i*difference;
            if(color > 3) color -= 3;

            if(color > 2)
            {
                b = color - 2;
                g = 1 - b;
            }
            else if(color > 1)
            {
                g = color - 1;
                r = 1 - g;
            }
            else
            {
                r = color;
                b = 1 - r;
            }

            text.chars.get(i).setQuadTextColor(this.c.set(r, g, b));
        }
    }

    @Override
    public void onTextUpdated()
    {

    }
}
