package com.pieisnotpi.game.test_editor;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.UiObject;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shapes.types.textured_c.TexCQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.scene.Scene;

public class EditorTile extends UiObject
{
    TexCQuad foreground, background;
    Sprite foreSprite, backSprite;
    boolean foreShowing = true, backShowing = true;
    private EditorScene actualScene;
    private Color highlightColor = new Color(0.8f, 0.8f, 0.8f), blankColor = new Color(0, 0, 0, 0), backgroundColor = new Color(0.3f, 0.3f, 0.3f);
    private boolean leftStatus = false, rightStatus = false, middleStatus = false, placed = false, destroyed = false;

    public EditorTile(float x, float y, float z, float scale, Sprite sprite, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = scale;
        this.height = scale;
        this.scale = scale;
        this.foreSprite = sprite;
        this.backSprite = sprite;
        this.scene = scene;
        this.actualScene = (EditorScene) scene;

        foreground = new TexCQuad(x, y, z, scale, scale, 0, sprite, blankColor, PiEngine.ORTHO_ID, scene);
        background = new TexCQuad(x, y, z - 0.01f, scale, scale, 0, sprite, backgroundColor, PiEngine.ORTHO_ID, scene);

        scene.gameObjects.add(this);
    }

    public void update()
    {
        if(leftStatus && !placed && mouseHoverStatus)
        {
            if(!foreground.registered) foreground.register();

            foreShowing = true;

            foreSprite = actualScene.getCurSprite();

            foreground.setQuadSprite(foreSprite);
            foreground.setQuadColors(blankColor);

            placed = true;
        }

        if(rightStatus && !placed && mouseHoverStatus)
        {
            if(!background.registered) background.register();

            backShowing = true;

            backSprite = actualScene.getCurSprite();

            background.setQuadSprite(backSprite);
            background.setQuadColors(backgroundColor);

            placed = true;
        }

        if(middleStatus && !destroyed)
        {
            if(mouseHoverStatus)
            {
                if(foreShowing)
                {
                    foreground.unregister();
                    foreShowing = false;
                    destroyed = true;
                }
                else if(backShowing)
                {
                    background.unregister();
                    backShowing = false;
                    destroyed = true;
                }

                onMouseEntered();
            }
            else destroyed = false;
        }
    }

    public void onLeftClick()
    {
        leftStatus = true;
    }

    public void onLeftRelease()
    {
        leftStatus = false;
        placed = false;
    }

    public void onRightClick()
    {
        rightStatus = true;
    }

    public void onRightRelease()
    {
        rightStatus = false;
        placed = false;
    }

    public void onMiddleClick()
    {
        middleStatus = true;
    }

    public void onMiddleRelease()
    {
        middleStatus = false;
        destroyed = false;
    }

    public void onMouseEntered()
    {
        if(!foreShowing) foreground.register();

        foreground.setQuadColors(highlightColor);
        foreground.setQuadSprite(actualScene.getCurSprite());
    }

    public void onMouseExited()
    {
        if(!foreShowing) foreground.unregister();

        foreground.setQuadSprite(foreSprite);
        foreground.setQuadColors(blankColor);
    }

    public void blank()
    {
        foreground.unregister();
        background.unregister();

        foreShowing = false;
    }

    public void destroy()
    {
        super.destroy();
        foreground.unregister();
        background.unregister();
    }
}
