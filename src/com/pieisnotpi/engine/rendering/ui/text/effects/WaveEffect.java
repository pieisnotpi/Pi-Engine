package com.pieisnotpi.engine.rendering.ui.text.effects;

import com.pieisnotpi.engine.rendering.shapes.types.text.TextQuad;
import com.pieisnotpi.engine.rendering.ui.text.Text;

import java.util.List;

public class WaveEffect implements TextEffect
{
    double x;
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

        for(int i = 0; i < chars.size(); i++)
        {
            TextQuad c = chars.get(i);

            float offset = text.getFont().pixelScale*text.getScale()*c.cSprite.offsetY, sine = (float) (scale*Math.sin(Math.toRadians((x + heightDif*i)*speed)));

            c.setY(offset + text.getY() + sine);
        }

        x++;
    }
}
