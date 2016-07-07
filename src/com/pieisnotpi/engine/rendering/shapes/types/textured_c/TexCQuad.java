package com.pieisnotpi.engine.rendering.shapes.types.textured_c;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shapes.types.textured.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector3f;

public class TexCQuad extends TexQuad
{
    protected Sprite sprite;

    public TexCQuad(float x, float y, float z, float width, float height, float depth, Sprite sprite, Color color, int matrixID, Scene scene)
    {
        super(x, y, z, width, height, depth, sprite, PiEngine.S_TEXTURE_C_ID, matrixID, scene);
        this.sprite = sprite;

        setQuadColors(color, color, color, color);
    }

    public TexCQuad(Vector3f c0, Vector3f c1, Vector3f c2, Vector3f c3, Sprite sprite, Color color, int matrixID, Scene scene)
    {
        super(c0, c1, c2, c3, sprite, PiEngine.S_TEXTURE_C_ID, matrixID, scene);
        this.sprite = sprite;

        setQuadColors(color, color, color, color);
    }

    public void preCompile(ShaderProgram shader)
    {
        if(sprite.isAnimated) sprite.updateAnimation();
    }
}
