package com.pieisnotpi.engine.rendering.shapes.types.textured;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.shapes.Quad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TexQuad extends Quad
{
    protected Sprite sprite;

    public TexQuad(float x, float y, float z, float width, float height, float depth, Sprite sprite, int matrixID, Scene scene)
    {
        this(x, y, z, width, height ,depth, sprite, PiEngine.TEXTURE_ID, matrixID, scene);
    }

    public TexQuad(float x, float y, float z, float width, float height, float depth, Sprite sprite, int shaderID, int matrixID, Scene scene)
    {
        super(x, y, z, width, height, depth, shaderID, matrixID, scene);

        this.sprite = sprite;
        this.texture = sprite.texture;
        transparent = texture.hasTransparency;

        setTexCoords(new Vector2f(sprite.uvx0, sprite.uvy0), new Vector2f(sprite.uvx1, sprite.uvy0), new Vector2f(sprite.uvx0, sprite.uvy1), new Vector2f(sprite.uvx1, sprite.uvy1));
    }

    public TexQuad(Vector3f c0, Vector3f c1, Vector3f c2, Vector3f c3, Sprite sprite, int matrixID, Scene scene)
    {
        this(c0, c1, c2, c3, sprite, PiEngine.TEXTURE_ID, matrixID, scene);
    }

    public TexQuad(Vector3f c0, Vector3f c1, Vector3f c2, Vector3f c3, Sprite sprite, int shaderID, int matrixID, Scene scene)
    {
        super(c0, c1, c2, c3, shaderID, matrixID, scene);

        this.sprite = sprite;
        this.texture = sprite.texture;
        transparent = texture.hasTransparency;

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
}
