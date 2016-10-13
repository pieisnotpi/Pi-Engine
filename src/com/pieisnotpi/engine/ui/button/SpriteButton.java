package com.pieisnotpi.engine.ui.button;

import com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader.TexCQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.engine.utility.MathUtility;
import org.joml.Vector2f;

public class SpriteButton extends UiObject
{
    private TexCQuad base;
    private ButtonAction leftClick, leftRelease, rightClick, rightRelease;
    private boolean leftStatus = false, rightStatus = false;

    final static Color blank = new Color(0, 0, 0, 0), highlighted = new Color(0.8f, 0.8f, 0.8f);

    public SpriteButton(float x, float y, float z, Sprite sprite, int matrixID, Scene scene)
    {
        pos.set(x, y, z);
        this.matrixID = matrixID;
        this.scene = scene;

        setSprite(sprite);

        scene.gameObjects.add(this);
    }

    public void setSprite(Sprite sprite)
    {
        float w = (sprite.uvx1 - sprite.uvx0)*sprite.texWidth, h = (sprite.uvy1 - sprite.uvy0)*sprite.texHeight;

        size.set(w, h, 0);
        //base = new TexCQuad(pos.x, pos.y, pos.z, size.x, size.y, 0, sprite, blank, matrixID, scene);
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
        setY(pos.y + yAmount/2);
    }

    public void onLeftClick()
    {
        if(mouseHoverStatus && !leftStatus && leftClick != null)
        {
            leftClick.action();
            leftStatus = true;
        }
    }

    public void onLeftRelease()
    {
        if(mouseHoverStatus && leftStatus && leftRelease != null)
        {
            leftRelease.action();
        }

        leftStatus = false;
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
        }

        rightStatus = false;
    }

    public boolean isPointInsideObject(Vector2f point)
    {
        return MathUtility.isPointInside(point, base.points[0], base.points[1], base.points[3], base.points[2]);
    }

    public void onMouseEntered()
    {
        base.setColors(highlighted);
        super.onMouseEntered();
    }

    public void onMouseExited()
    {
        base.setColors(blank);
        super.onMouseExited();
    }

    public void destroy()
    {
        super.destroy();
    }
}
