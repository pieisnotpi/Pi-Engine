package com.pieisnotpi.engine.ui.text.effects;

import com.pieisnotpi.engine.rendering.shaders.types.text_shader.TextQuad;
import com.pieisnotpi.engine.ui.text.Text;

import java.util.List;

public class WaveEffect implements TextEffect
{
    private float x, speed, heightDif, scale;
    private Text text;

    public WaveEffect(float speed, float heightDif, float scale)
    {
        this.speed = speed;
        this.heightDif = heightDif;
        this.scale = scale;
    }

    public void setText(Text text)
    {
        this.text = text;
    }

    public void process(float timeStep)
    {
        List<TextQuad> chars = text.chars;

        for(TextQuad c : chars)
        {
            float offset = -text.newlineSpace*c.line + c.cSprite.offsetY, sine = (float) Math.sin(x + c.getX()*heightDif)*scale;
            c.setY(offset + text.getY() + sine);
        }

        x += speed*timeStep;
    }
}
