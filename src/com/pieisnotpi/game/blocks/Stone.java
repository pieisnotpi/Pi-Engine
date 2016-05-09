package com.pieisnotpi.game.blocks;

import com.pieisnotpi.engine.rendering.model.Model;
import com.pieisnotpi.engine.scene.Scene;

public class Stone extends Block
{
    static Model model = new Model("/assets/models/stone.model");

    public Stone(float x, float y, float z, Scene scene)
    {
        super(x, y, z, model, scene);
    }
}
