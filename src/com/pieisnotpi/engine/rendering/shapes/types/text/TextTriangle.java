package com.pieisnotpi.engine.rendering.shapes.types.text;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.renderable_types.TextRenderable;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class TextTriangle extends TextRenderable
{
    public TextTriangle(Vector3f c0, Vector3f c1, Vector3f c2, Vector2f c0tx, Vector2f c1tx, Vector2f c2tx, Color c0tc, Color c1tc, Color c2tc, Color c0to, Color c1to, Color c2to, Texture texture, int matrixID, Scene scene)
    {
        setDefaults(3);
        this.drawMode = GL11.GL_TRIANGLES;

        setPoints(c0, c1, c2);
        setTexCoords(c0tx, c1tx, c2tx);
        setTextColors(c0tc, c1tc, c2tc);
        setOutlineColors(c0to, c1to, c2to);

        this.texture = texture;
        this.matrixID = matrixID;
        this.shaderID = PiEngine.TEXT_ID;
        this.scene = scene;

        this.texture = texture;
        transparent = true;
    }
}
