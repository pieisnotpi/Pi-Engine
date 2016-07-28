package com.pieisnotpi.game.blocks;

import com.pieisnotpi.engine.rendering.Mesh;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.model.Model;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexCube;
import com.pieisnotpi.engine.rendering.shapes.Quad;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Block extends GameObject
{
    public static final float SIZE = 0.5f;

    public Material material;
    public List<TexCube> cubes;
    public Mesh mesh;

    public Block(float x, float y, float z, Model model, Scene scene)
    {
        pos.set(x, y, z);
        size.set(SIZE);
        this.scene = scene;

        cubes = new ArrayList<>();
        cubes.addAll(model.cubes.stream().map(cube -> cube.toTexCube(x, y, z, SIZE)).collect(Collectors.toList()));
    }

    public List<Renderable> getRenderables()
    {
        List<Renderable> list = new ArrayList<>();
        for(TexCube cube : cubes) for(Quad quad : cube.sides) if(quad.enabled) list.add(quad);
        return list;
    }

    public void addToRot(float xr, float yr, float zr)
    {
        float cx = getCx(), cy = getCy(), cz = getCz();

        cubes.forEach(texCube ->
        {
            for(Quad quad : texCube.sides)
            {
                quad.addToXRot(xr, cy, cz);
                quad.addToYRot(yr, cx, cz);
                quad.addToZRot(zr, cx, cy);
            }
        });
        super.addToRot(xr, yr, zr);
    }

    public void destroy()
    {
        super.destroy();

        disable();
    }
}
