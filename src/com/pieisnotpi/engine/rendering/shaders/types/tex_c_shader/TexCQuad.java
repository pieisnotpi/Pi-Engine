package com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader;

import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;

public class TexCQuad extends TexQuad
{
    public TexCQuad(float x, float y, float z, float width, float height, float depth, Sprite sprite)
    {
        super(x, y, z, width, height, depth, sprite);
    }
    /*protected Sprite sprite;
    private static final Material m = new Material(PiEngine.S_TEXTURE_C_ID, true, false, true);

    public TexCQuad(float x, float y, float z, float width, float height, float depth, Sprite sprite, Color color, int matrixID, Scene scene)
    {
        super(x, y, z, width, height, depth, sprite, m, matrixID, scene);
        this.sprite = sprite;

        setQuadColors(color, color, color, color);
    }

    public TexCQuad(Vector3f c0, Vector3f c1, Vector3f c2, Vector3f c3, Sprite sprite, Color color, int matrixID, Scene scene)
    {
        super(c0, c1, c2, c3, sprite, m, matrixID, scene);
        this.sprite = sprite;

        setQuadColors(color, color, color, color);
    }

    public void preCompile(ShaderProgram shader)
    {
        if(sprite.isAnimated) sprite.updateAnimation();
    }*/
}