package com.pieisnotpi.game.blocks;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.model.Model;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.scene.Scene;

public class Pillar extends Block
{
    private static final Model model = new Model("/assets/models/pillar.model");
    private static final Material m = new Material(PiEngine.S_TEXTURE_ID, false, false, true);

    public Pillar(float x, float y, float z, Scene scene)
    {
        super(x, y, z, model, m, scene);
    }
}
