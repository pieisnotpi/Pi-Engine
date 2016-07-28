package com.pieisnotpi.engine.rendering.shaders.types.tex_shader;

import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shapes.Quad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TexQuad extends Quad
{
    protected Sprite sprite;

    public TexQuad(float x, float y, float z, float width, float height, float depth, Sprite sprite)
    {
        super(x, y, z, width, height, depth);

        this.sprite = sprite;

        setTexCoords(new Vector2f(sprite.uvx0, sprite.uvy0), new Vector2f(sprite.uvx1, sprite.uvy0), new Vector2f(sprite.uvx0, sprite.uvy1), new Vector2f(sprite.uvx1, sprite.uvy1));
    }

    public TexQuad(Vector3f c0, Vector3f c1, Vector3f c2, Vector3f c3, Sprite sprite)
    {
        super(c0, c1, c2, c3);

        this.sprite = sprite;

        setTexCoords(new Vector2f(sprite.uvx0, sprite.uvy0), new Vector2f(sprite.uvx1, sprite.uvy0), new Vector2f(sprite.uvx0, sprite.uvy1), new Vector2f(sprite.uvx1, sprite.uvy1));
    }

    public Sprite getQuadSprite()
    {
        return sprite;
    }

    public void setQuadSprite(Sprite sprite)
    {
        this.sprite = sprite;

        setQuadTexCoords(sprite.uvx0, sprite.uvy0, sprite.uvx1, sprite.uvy1);
    }

    public void preCompile(ShaderProgram shader)
    {
        if(sprite.isAnimated && sprite.updateAnimation()) setQuadSprite(sprite);
    }
}
