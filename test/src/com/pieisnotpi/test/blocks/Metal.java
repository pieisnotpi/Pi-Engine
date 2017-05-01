package com.pieisnotpi.test.blocks;

import com.pieisnotpi.test.blocks.model.Model;

public class Metal extends Block
{
    private static final Model model = new Model("/assets/models/metal.model");

    public Metal(float x, float y, float z)
    {
        super(x, y, z, model);
    }
}
