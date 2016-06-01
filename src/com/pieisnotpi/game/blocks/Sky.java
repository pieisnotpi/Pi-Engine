package com.pieisnotpi.game.blocks;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.shapes.types.textured.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;

public class Sky
{
    public TexQuad rect;

    public Sky(Scene scene)
    {
        Texture texture = Texture.getTexture("sky");
        Sprite sprite = new Sprite(texture, 0, 0, 128, 72);

        rect = new TexQuad(-0.8f, -0.45f, -1.0f, 1.6f, 0.9f, 0, sprite, PiEngine.ORTHO_ID, scene);
    }
}
