package com.pieisnotpi.engine.rendering.ui.text.effects;

import com.pieisnotpi.engine.rendering.shapes.types.text.TextQuad;
import com.pieisnotpi.engine.rendering.ui.text.Text;

import java.util.List;

public class HoverEffect implements TextEffect
{
    double x = 0, modifier = 1;
    float speed, scale;

    public HoverEffect(float speed, float scale)
    {
        this.speed = speed;
        this.scale = scale;
    }

    public void process(Text text)
    {
        List<TextQuad> chars = text.getChars();

        double sine = scale*Math.sin(Math.toRadians(x*speed));

        for(TextQuad c : chars)
        {
            float offset = text.getFont().pixelScale*text.scale*c.cSprite.offsetY;
            c.setY(offset + text.getY() + ((float) sine));
        }

        x+= speed *modifier;
    }
}
