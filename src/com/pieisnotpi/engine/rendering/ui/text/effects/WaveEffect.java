package com.pieisnotpi.engine.rendering.ui.text.effects;

import com.pieisnotpi.engine.rendering.shapes.types.text.TextQuad;
import com.pieisnotpi.engine.rendering.ui.text.Text;

import java.util.List;

public class WaveEffect implements TextEffect
{
    int x;
    float speed, heightDif, scale;

    public WaveEffect()
    {
        speed = 4;
        heightDif = 2;
        scale = 0.5f;
    }

    public WaveEffect(float speed, float heightDif, float scale)
    {
        this.speed = speed;
        this.heightDif = heightDif;
        this.scale = scale;
    }

    public void process(Text text)
    {
        List<TextQuad> chars = text.getChars();

        int prevLine = 0, t = 0;

        for(int i = 0; i < chars.size(); i++)
        {
            TextQuad c = chars.get(i);

            if(c.line != prevLine) t = 0;

            float offset = -text.newlineSpace*c.line + text.getFont().pixelScale*text.scale*c.cSprite.offsetY, sine = (float) (scale*Math.sin(Math.toRadians((x + heightDif*t)*speed)));

            c.setY(offset + text.getY() + sine);

            prevLine = c.line;
            t++;
        }

        x++;
    }
}
