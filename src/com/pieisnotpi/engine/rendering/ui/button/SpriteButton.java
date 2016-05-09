package com.pieisnotpi.engine.rendering.ui.button;

import com.pieisnotpi.engine.game_objects.UiObject;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shapes.types.textured_c.TexCQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.scene.Scene;

public class SpriteButton extends UiObject
{
    TexCQuad base;
    final static Color blank = new Color(0, 0, 0, 0), highlighted = new Color(0.8f, 0.8f, 0.8f);

    public SpriteButton(float x, float y, float z, float scale, Sprite sprite, int matrixID, Scene scene)
    {
        float r = (float) (sprite.x1 - sprite.x0)/(sprite.y1 - sprite.y0);

        System.out.println(r);

        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = scale /= 100;
        this.matrixID = matrixID;
        this.scene = scene;

        width = scale*r;
        height = scale/r;

        float tm = Float.max(width, height), tx = x - (tm - width), ty = y - (tm - height);

        System.out.printf("tm:%f,x:%f,y:%f,tx:%f,ty:%f%n", tm, x, y, tx, ty);

        base = new TexCQuad(tx, ty, z, tm + tx, tm + ty, 0, sprite, blank, matrixID, scene);
        scene.gameObjects.add(this);
    }

    public void onMouseEntered()
    {
        base.setColors(highlighted, highlighted, highlighted, highlighted);
    }

    public void onMouseExited()
    {
        base.setColors(blank, blank, blank, blank);
    }
}
