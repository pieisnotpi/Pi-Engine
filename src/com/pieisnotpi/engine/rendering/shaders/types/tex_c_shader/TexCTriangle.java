package com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shapes.Triangle;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TexCTriangle extends Triangle
{
    private static final Material m = new Material(PiEngine.S_TEXTURE_C_ID, true, false, true);

    public TexCTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Color c0c, Color c1c, Color c2c, Vector2f c0tx, Vector2f c1tx, Vector2f c2tx, Texture texture, int matrixID, Scene scene)
    {
        this(c0, c1, c2, c0c, c1c, c2c, c0tx, c1tx, c2tx, texture, m, matrixID, scene);
    }

    public TexCTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Color c0c, Color c1c, Color c2c, Vector2f c0tx, Vector2f c1tx, Vector2f c2tx, Texture texture, Material material, int matrixID, Scene scene)
    {
        super(c0, c1, c2, material, matrixID, scene);

        colors[0] = c0c;
        colors[1] = c1c;
        colors[2] = c2c;

        texCoords[0] = c0tx;
        texCoords[1] = c1tx;
        texCoords[2] = c2tx;

        this.texture = texture;

        register();
    }

    public void setTexCoords(Vector2f c0, Vector2f c1, Vector2f c2)
    {
        texCoords[0].set(c0);
        texCoords[1].set(c1);
        texCoords[2].set(c2);
    }

    public void setColors(Color c0, Color c1, Color c2)
    {
        colors[0] = c0;
        colors[1] = c1;
        colors[2] = c2;
    }
}
