package com.pieisnotpi.engine.rendering.shaders.types.text_shader;

import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.ui.text.TextRenderable;
import com.pieisnotpi.engine.ui.text.font.CharSprite;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static com.pieisnotpi.engine.utility.MathUtility.*;

public class TextQuad extends TextRenderable
{
    public int line = 0;
    public float xRot = 0, yRot = 0, zRot = 0;
    protected Sprite sprite;
    public CharSprite cSprite;

    public TextQuad(float x, float y, float z, float width, float height, CharSprite cSprite, Color textColor, Color outlineColor, int line)
    {
        super();

        this.line = line;
        this.cSprite = cSprite;
        this.sprite = cSprite.sprite;

        setPoints(new Vector3f(x, y, z), new Vector3f(x + width, y, z), new Vector3f(x, y + height, z), new Vector3f(x + width, y + height, z));
        setTexCoords(new Vector2f(sprite.uvx0, sprite.uvy0), new Vector2f(sprite.uvx1, sprite.uvy0), new Vector2f(sprite.uvx0, sprite.uvy1), new Vector2f(sprite.uvx1, sprite.uvy1));
        setQuadTextColor(textColor, textColor, textColor, textColor);
        setQuadOutlineColor(outlineColor, outlineColor, outlineColor, outlineColor);
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

    public void setPos(float x, float y, float z)
    {
        float xDif = x - getX(), yDif = y - getY(), zDif = z - getZ();

        points[0].add(xDif, yDif, zDif);
        points[1].add(xDif, yDif, zDif);
        points[2].add(xDif, yDif, zDif);
        points[3].add(xDif, yDif, zDif);
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

    public void addToXRot(float amount, float pointY, float pointZ)
    {
        xRot += amount;

        rotateAxisX(amount, pointY, pointZ, points);
    }

    public void addToYRot(float amount, float pointX, float pointZ)
    {
        yRot += amount;

        rotateAxisY(amount, pointX, pointZ, points);
    }

    public void addToZRot(float amount, float pointX, float pointY)
    {
        zRot += amount;

        rotateAxisZ(amount, pointX, pointY, points);
    }
}
