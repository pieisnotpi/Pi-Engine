package com.pieisnotpi.engine.rendering.shapes.types.textured;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.shapes.Triangle;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TexTriangle extends Triangle
{
    public TexTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Vector2f c0tx, Vector2f c1tx, Vector2f c2tx, Texture texture, int matrixID, Scene scene)
    {
        this(c0, c1, c2, c0tx, c1tx, c2tx, texture, PiEngine.S_TEXTURE_ID, matrixID, scene);
    }

    public TexTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Vector2f c0tx, Vector2f c1tx, Vector2f c2tx, Texture texture, int shaderID, int matrixID, Scene scene)
    {
        super(c0, c1, c2, c0tx, c1tx, c2tx, texture, shaderID, matrixID, scene);

        this.texture = texture;
        if(this.texture.hasTransparency) transparent = true;

        register();
    }

    public String toString()
    {
        return super.toString() + ", c0tx: " + texCoords[0] + "c1tx: " + texCoords[1] + ", c2tx: " + texCoords[2];
    }
}
