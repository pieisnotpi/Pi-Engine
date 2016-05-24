package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.ui.button.SpriteButton;
import com.pieisnotpi.engine.scene.Scene;

public class OptionsScene extends Scene
{
    protected SpriteButton button;

    public void init()
    {
        super.init();
        fps.disable();

        name = "Options";

        cameras.add(new Camera(0, 0, 1, 1, 90, this));

        clearColor.set(0.5f, 0.5f, 0.5f);
        //button = new SpriteButton(0, 0, 0, 20, new Sprite(TextureDatabase.getTexture("icons"), 3, 1, 11, 14), Global.ORTHO_ID, this);
    }
}
