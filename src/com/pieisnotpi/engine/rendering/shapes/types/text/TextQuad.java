package com.pieisnotpi.engine.rendering.shapes.types.text;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.renderable_types.TextRenderable;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.ui.text.font.CharSprite;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import static com.pieisnotpi.engine.utility.MathUtility.*;

public class TextQuad extends TextRenderable
{
    public int line = 0;
    public float xRot = 0, yRot = 0, zRot = 0;
    protected Sprite sprite;
    public CharSprite cSprite;

    public TextQuad(float x, float y, float z, float width, float height, CharSprite cSprite, Color textColor, Color outlineColor, int line, int matrixID, Scene scene)
    {
        setDefaults(4);
        drawMode = GL11.GL_TRIANGLE_STRIP;

        this.line = line;
        this.scene = scene;
        this.shaderID = PiEngine.S_TEXT_ID;
        this.matrixID = matrixID;
        this.transparent = true;
        this.cSprite = cSprite;
        this.sprite = cSprite.sprite;
        this.texture = sprite.texture;

        setPoints(new Vector3f(x, y, z), new Vector3f(x + width, y, z), new Vector3f(x, y + height, z), new Vector3f(x + width, y + height, z));
        setTexCoords(new Vector2f(sprite.uvx0, sprite.uvy0), new Vector2f(sprite.uvx1, sprite.uvy0), new Vector2f(sprite.uvx0, sprite.uvy1), new Vector2f(sprite.uvx1, sprite.uvy1));
        setTextColors(textColor, textColor, textColor, textColor);
        setOutlineColors(outlineColor, outlineColor, outlineColor, outlineColor);

        register();
    }

    public void setQuadTextColor(Color color)
    {
        setQuadTextColor(color, color, color, color);
    }

    public void setQuadTextColor(Color c0, Color c1, Color c2, Color c3)
    {
        setTextColors(c0, c1, c2, c3);
    }

    public void setQuadOutlineColor(Color color)
    {
        setQuadOutlineColor(color, color, color, color);
    }

    public void setQuadOutlineColor(Color c0, Color c1, Color c2, Color c3)
    {
        setOutlineColors(c0, c1, c2, c3);
    }

    public float getX()
    {
        return points[0].x;
    }

    public void setX(float amount)
    {
        float dif = amount - getX();

        points[0].x += dif;
        points[1].x += dif;
        points[2].x += dif;
        points[3].x += dif;
    }

    public float getY()
    {
        return points[0].y;
    }

    public void setY(float amount)
    {
        float dif = amount - getY();

        points[0].y += dif;
        points[1].y += dif;
        points[2].y += dif;
        points[3].y += dif;
    }

    public float getZ()
    {
        return points[0].z;
    }

    public void setZ(float amount)
    {
        float dif = amount - getZ();

        points[0].z += dif;
        points[1].z += dif;
        points[2].z += dif;
        points[3].z += dif;
    }

    public void rotateX(float amount)
    {
        rotateX(amount, points[0].y, points[0].z);
    }

    public void rotateX(float amount, float pointY, float pointZ)
    {
        xRot += amount;

        rotateAxisX(amount, pointY, pointZ, points);
    }

    public void rotateY(float amount)
    {
        rotateY(amount, points[0].x, points[0].z);
    }

    public void rotateY(float amount, float pointX, float pointZ)
    {
        yRot += amount;

        rotateAxisY(amount, pointX, pointZ, points);
    }

    public void rotateZ(float amount)
    {
        rotateZ(amount, points[0].x, points[0].y);
    }

    public void rotateZ(float amount, float pointX, float pointY)
    {
        zRot += amount;

        rotateAxisZ(amount, pointX, pointY, points);
    }

    public void preCompile(ShaderProgram shader)
    {
        if(sprite.isAnimated) sprite.updateAnimation();
    }
}
