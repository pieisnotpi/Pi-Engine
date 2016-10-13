package com.pieisnotpi.game.test_editor;

import com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader.TexCQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.utility.Color;

public class EditorTile extends UiObject
{
    TexCQuad foreground, background;
    Sprite foreSprite, backSprite;
    boolean foreShowing = false, backShowing = false;
    private EditorScene scene;
    private Color highlightColor = new Color(0.8f, 0.8f, 0.8f), blankColor = new Color(0, 0, 0, 0), backgroundColor = new Color(0.3f, 0.3f, 0.3f);
    private boolean placed = false, destroyed = false;

    public EditorTile(float x, float y, float z, float scale, Sprite sprite, Scene scene)
    {
        pos.set(x, y, z);
        size.set(scale, scale, 0);
        this.foreSprite = sprite;
        this.backSprite = sprite;
        this.scene = (EditorScene) scene;

        foreground = new TexCQuad(x, y, z, scale, scale, 0, sprite, blankColor);
        foreground.enabled = false;
        background = new TexCQuad(x, y, z - 0.01f, scale, scale, 0, sprite, backgroundColor);
        background.enabled = false;

        scene.gameObjects.add(this);
    }

    public void onLeftHold()
    {
        if(mouseHoverStatus && !placed)
        {
            if(!foreground.enabled) registerFore();

            foreShowing = true;

            foreSprite = scene.getCurSprite();

            foreground.setQuadSprite(foreSprite);
            foreground.setQuadColors(blankColor);

            placed = true;
        }
    }

    public void onLeftRelease()
    {
        placed = false;
    }

    public void onRightHold()
    {
        if(mouseHoverStatus && !placed)
        {
            if(!background.enabled) registerBack();

            backShowing = true;

            backSprite = scene.getCurSprite();

            background.setQuadSprite(backSprite);
            background.setQuadColors(backgroundColor);

            placed = true;
        }
    }

    public void onRightRelease()
    {
        placed = false;
    }

    public void onMiddleHold()
    {
        if(!mouseHoverStatus) destroyed = false;
        else if(!destroyed)
        {
            if(foreShowing)
            {
                unregisterFore();
                foreShowing = false;
                destroyed = true;
            }
            else if(backShowing)
            {
                unregisterBack();
                backShowing = false;
                destroyed = true;
            }

            onMouseEntered();
        }
    }

    public void onMiddleRelease()
    {
        destroyed = false;
    }

    public void onMouseEntered()
    {
        if(!foreShowing && !foreground.enabled) registerFore();

        foreground.setQuadColors(highlightColor);
        foreground.setQuadSprite(scene.getCurSprite());
    }

    public void onMouseExited()
    {
        if(!foreShowing) unregisterFore();

        foreground.setQuadSprite(foreSprite);
        foreground.setQuadColors(blankColor);
    }

    public void blank()
    {
        unregisterFore();
        unregisterBack();

        foreShowing = false;
        backShowing = false;
    }

    private void registerFore()
    {
        foreground.enabled = true;
        scene.mesh.addRenderable(foreground);
    }

    private void unregisterFore()
    {
        foreground.enabled = false;
        scene.mesh.removeRenderable(foreground);
    }

    private void registerBack()
    {
        background.enabled = true;
        scene.mesh.addRenderable(background);
    }

    private void unregisterBack()
    {
        background.enabled = false;
        scene.mesh.removeRenderable(background);
    }
}
