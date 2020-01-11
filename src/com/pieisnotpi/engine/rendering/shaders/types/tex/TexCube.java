package com.pieisnotpi.engine.rendering.shaders.types.tex;

import com.pieisnotpi.engine.rendering.primitives.Cube;
import com.pieisnotpi.engine.rendering.textures.Sprite;

public class TexCube extends Cube
{
    public TexCube(float x, float y, float z, float width, float height, float depth, Sprite topSprite, Sprite sideSprite, Sprite bottomSprite)
    {
        super();

        sides[0] = new TexQuad(x, y, z, width, height, 0, sideSprite);
        sides[1] = new TexQuad(x, y, z + depth, 0, height, -depth, sideSprite);
        sides[2] = new TexQuad(x + width, y, z, 0, height, depth, sideSprite);
        sides[3] = new TexQuad(x + width, y, z + depth, -width, height, 0, sideSprite);
        sides[4] = new TexQuad(x, y + height, z, width, 0, depth, topSprite);
        sides[5] = new TexQuad(x, y, z, width, 0, depth, bottomSprite);
    }
}
