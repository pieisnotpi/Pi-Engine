package com.pieisnotpi.engine.rendering.ui.text.effects;

import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.ui.text.Text;

public class RainbowEffect implements TextEffect
{
    float speed, difference, x;

    public RainbowEffect(float speed, float difference)
    {
        this.speed = speed;
        this.difference = difference;
    }

    public void process(Text text)
    {
        for(int i = 0; i < text.chars.size(); i++)
        {
            float r = 0, g = 0, b = 0;
            float sine = (float) (1.5 + 1.5*Math.sin(Math.toRadians((x + difference*i)*speed)));

            if(sine > 2)
            {
                b = sine - 2;
                g = 1 - b;
            }
            else if(sine > 1)
            {
                g = sine - 1;
                r = 1 - g;
            }
            else
            {
                r = sine;
                b = 1 - r;
            }

            text.chars.get(i).setQuadTextColor(new Color(r, g, b));
        }

        x++;
    }
}
