package com.pieisnotpi.test.blocks;

import com.pieisnotpi.engine.rendering.primitives.Primitive;
import com.pieisnotpi.engine.rendering.primitives.Quad;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexCube;
import com.pieisnotpi.test.blocks.model.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Block
{
    public static final float SIZE = 0.5f;

    public List<TexCube> cubes;

    public Block(float x, float y, float z, Model model)
    {
        cubes = new ArrayList<>();
        cubes.addAll(model.cubes.stream().map(cube -> cube.toTexCube(x, y, z, SIZE)).collect(Collectors.toList()));
    }

    public List<Primitive> getRenderables()
    {
        List<Primitive> list = new ArrayList<>();
        for(TexCube cube : cubes) for(Quad quad : cube.sides) if(quad.enabled) list.add(quad);
        return list;
    }
}
