package com.pieisnotpi.game.blocks;

import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.game.blocks.model.Model;

public class Stone extends Block
{
    public Stone(float x, float y, float z, Model model, Scene scene)
    {
        super(x, y, z, model, scene);
    }
    /*private static final Model model = new Model("/assets/models/stone.model");
    private static final Material m = new Material(PiEngine.S_TEXTURE_ID, false, false, true);

    public Stone(float x, float y, float z, Scene scene)
    {
        super(x, y, z, model, m, scene);
    }*/
}
