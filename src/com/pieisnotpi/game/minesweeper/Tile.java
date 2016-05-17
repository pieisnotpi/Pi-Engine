package com.pieisnotpi.game.minesweeper;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.UiObject;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shapes.types.textured_c.TexCQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.scene.Scene;

public class Tile extends UiObject
{
    private TexCQuad quad;
    private Sprite sprite;
    private boolean hoverStatus = false, activated = false;

    private Color highlightColor = new Color(0.8f, 0.8f, 0.8f), activateColor = new Color(0.2f, 0.2f, 0.2f), blankColor = new Color(0, 0, 0, 0);

    public Tile(float x, float y, float z, float scale, Sprite sprite, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = scale;
        this.height = scale;
        this.scale = scale;
        this.sprite = sprite;
        this.scene = scene;

        quad = new TexCQuad(x, y, z, scale, scale, 0, sprite, blankColor, PiEngine.ORTHO_ID, scene);

        scene.gameObjects.add(this);
    }

    public void onLeftClick()
    {
        if(hoverStatus)
        {
            activated = true;
            quad.setQuadColors(activateColor);
        }
    }

    public void onMouseEntered()
    {
        if(!activated) quad.setQuadColors(highlightColor);
    }

    public void onMouseExited()
    {
        if(!activated) quad.setQuadColors(blankColor);
    }

    public void destroy()
    {
        super.destroy();
        quad.unregister();
    }
}
