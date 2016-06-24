package com.pieisnotpi.game.blocks;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.GameObject;
import com.pieisnotpi.engine.rendering.model.Model;
import com.pieisnotpi.engine.rendering.shapes.Quad;
import com.pieisnotpi.engine.rendering.shapes.types.textured.TexCube;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Block extends GameObject
{
    public static final float SIZE = 0.5f;

    public Sprite top, side, bottom;
    public List<TexCube> cubes;

    public Block(float x, float y, float z, Model model, Scene scene)
    {
        pos.set(x, y, z);
        size.set(SIZE);
        this.scene = scene;

        defaultCenter();

        cubes = new ArrayList<>();
        cubes.addAll(model.cubes.stream().map(cube -> cube.toTexCube(x, y, z, SIZE, PiEngine.C_PERSPECTIVE, scene)).collect(Collectors.toList()));
    }

    public void setXRot(float r)
    {
        cubes.forEach(texCube ->
        {
            for(Quad quad : texCube.sides) quad.setXRot(r, getCy(), getCz());
        });
    }

    public void setYRot(float r)
    {
        cubes.forEach(texCube ->
        {
            for(Quad quad : texCube.sides) quad.setYRot(r, getCx(), getCz());
        });
    }

    public void setZRot(float r)
    {
        cubes.forEach(texCube ->
        {
            for(Quad quad : texCube.sides) quad.setZRot(r, getCx(), getCy());
        });
    }

    @Override
    public void enable()
    {
        cubes.forEach(TexCube::register);
    }

    @Override
    public void disable()
    {
        cubes.forEach(TexCube::unregister);
    }

    public void destroy()
    {
        super.destroy();

        disable();
    }
}
