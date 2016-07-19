package com.pieisnotpi.engine.rendering.shaders.types.tex_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shapes.Triangle;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TexTriangle extends Triangle
{
    public static final Material m = new Material(PiEngine.S_TEXTURE_ID, false, false, true);

    public TexTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Vector2f c0tx, Vector2f c1tx, Vector2f c2tx, Texture texture, int matrixID, Scene scene)
    {
        this(c0, c1, c2, c0tx, c1tx, c2tx, texture, m, matrixID, scene);
    }

    public TexTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Vector2f c0tx, Vector2f c1tx, Vector2f c2tx, Texture texture, Material material, int matrixID, Scene scene)
    {
        super(c0, c1, c2, c0tx, c1tx, c2tx, texture, material, matrixID, scene);

        this.texture = texture;

        register();
    }

    public String toString()
    {
        return super.toString() + ", c0tx: " + texCoords[0] + "c1tx: " + texCoords[1] + ", c2tx: " + texCoords[2];
    }
}
