package com.pieisnotpi.test.blocks;

import com.pieisnotpi.test.blocks.model.Model;

public class Grass extends Block
{
    private static final Model model = new Model("/assets/models/grass.model");

    public Grass(float x, float y, float z)
    {
        super(x, y, z, model);
    }
}
