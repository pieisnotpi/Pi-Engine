package com.pieisnotpi.engine.rendering.ui.button;

import com.pieisnotpi.engine.game_objects.UiObject;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shapes.types.colored.ColorQuad;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.utility.MathUtility;
import org.joml.Vector2f;

public class Button extends UiObject
{
    private Text text;
    public ColorQuad base;
    private ButtonAction leftClick, leftRelease, rightClick, rightRelease;
    private static Color t0 = new Color(0.8f, 0.8f, 0.8f), t1 = new Color(1, 1, 1), b0 = new Color(0.7f, 0.7f, 0.7f), b1 = new Color(0.9f, 0.9f, 0.9f);

    public boolean leftStatus = false, rightStatus = false;
    private static final float SPACING_CONST = 0.00417f;

    public Button(String textValue, float scale, float x, float y, float z, int matrixID, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = scale;
        this.matrixID = matrixID;
        this.scene = scene;

        setText(textValue);

        scene.gameObjects.add(this);
    }

    public void setText(String textValue)
    {
        if(text == null) text = new Text(textValue, scale, x + scale*SPACING_CONST, y + scale*SPACING_CONST, z + 0.001f, new Color(0, 0, 0), new Color(1, 1, 1, 0), matrixID, scene);
        else text.setText(textValue);

        width = text.width + scale*SPACING_CONST*2;
        height = text.height + scale*SPACING_CONST*2;

        base = new ColorQuad(x, y, z, width, height, 0, b0, b0, t0, t0, matrixID, scene);

        addToZRot(45);
    }

    public void setX(float value)
    {
        if(value == x) return;

        x = value;

        base.setX(value);
        text.setX(scale* SPACING_CONST + value);
    }

    public void setY(float value)
    {
        if(value == y) return;

        y = value;

        base.setY(value);
        text.setY(scale* SPACING_CONST + value);
    }

    public void setZ(float value)
    {
        base.setZ(value);
        text.setZ(value);
    }

    public void setOnLeftClick(ButtonAction action)
    {
        leftClick = action;
    }

    public void setOnLeftRelease(ButtonAction action)
    {
        leftRelease = action;
    }

    public void setOnRightClick(ButtonAction action)
    {
        rightClick = action;
    }

    public void setOnRightRelease(ButtonAction action)
    {
        rightRelease = action;
    }

    public void onScroll(float xAmount, float yAmount)
    {
        setY(y + yAmount/(scale*2));
    }

    public void onLeftClick()
    {
        if(mouseHoverStatus && !leftStatus && leftClick != null)
        {
            base.setColors(t1, t1, b0, b0);
            leftClick.action();
            leftStatus = true;
        }
    }

    public void onLeftRelease()
    {
        if(mouseHoverStatus && leftStatus && leftRelease != null)
        {
            base.setColors(b1, b1, t1, t1);
            leftRelease.action();
            leftStatus = false;
        }
    }

    public void onRightClick()
    {
        if(mouseHoverStatus && !rightStatus && rightClick != null)
        {
            rightClick.action();
            rightStatus = true;
        }
    }

    public void onRightRelease()
    {
        if(mouseHoverStatus && rightStatus && rightRelease != null)
        {
            rightRelease.action();
            rightStatus = false;
        }
    }

    public void onMouseEntered()
    {
        base.setColors(b1, b1, t1, t1);
    }

    public void onMouseExited()
    {
        if(leftStatus) onLeftRelease();
        if(rightStatus) onRightRelease();

        base.setColors(b0, b0, t0, t0);
    }

    public boolean isPointInsideObject(Vector2f point)
    {
        return MathUtility.isPointInside(point, base.points[0], base.points[2], base.points[3], base.points[1]);
    }

    public void addToXRot(float rot)
    {
        addToXRot(rot, base.getCenterY(), base.getCenterZ());
    }

    public void addToXRot(float rot, float cy, float cz)
    {
        base.addToXRot(rot, cy, cz);
        text.addToXRot(rot, cy, cz);
    }

    public void addToYRot(float rot)
    {
        addToYRot(rot, base.getCenterX(), base.getCenterZ());
    }

    public void addToYRot(float rot, float cx, float cz)
    {
        base.addToYRot(rot, cx, cz);
        text.addToYRot(rot, cx, cz);
    }

    public void addToZRot(float rot)
    {
        addToZRot(rot, base.getCenterX(), base.getCenterY());
    }

    public void addToZRot(float rot, float cx, float cy)
    {
        base.addToZRot(rot, cx, cy);
        text.addToZRot(rot, cx, cy);
    }
}
