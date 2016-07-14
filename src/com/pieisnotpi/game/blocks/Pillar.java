package com.pieisnotpi.game.blocks;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.model.Model;
import com.pieisnotpi.engine.scene.Scene;

public class Pillar extends Block
{
    static Model model = new Model("/assets/models/pillar.model");

    public Pillar(float x, float y, float z, Scene scene)
    {
        super(x, y, z, model, PiEngine.S_TEXTURE_ID, scene);
    }
}
