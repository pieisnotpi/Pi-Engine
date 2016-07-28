package com.pieisnotpi.game.blocks;

import com.pieisnotpi.engine.rendering.model.Model;
import com.pieisnotpi.engine.scene.Scene;

public class Metal extends Block
{
    private static final Model model = new Model("/assets/models/metal.model");

    public Metal(float x, float y, float z, Scene scene)
    {
        super(x, y, z, model, scene);
    }
}