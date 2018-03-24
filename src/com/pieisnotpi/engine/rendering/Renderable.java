package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.Transform;

import java.util.Comparator;

public class Renderable
{
    public static final Comparator<Renderable> comparator = Comparator.comparingInt(r -> r.layer);

    private Mesh[] meshes;
    private Transform transform;
    private int pass, layer;

    public Renderable(int pass, Transform transform, Mesh... meshes)
    {
        this(pass, 0, transform, meshes);
    }
    
    public Renderable(int pass, int layer, Transform transform, Mesh... meshes)
    {
        this.pass = pass;
        this.layer = layer;
        this.transform = transform;
        this.meshes = meshes;
    }

    //TODO: ASSIMP Importing
    public Renderable(int pass, int layer, Transform transform, String path)
    {
    
    }

    public int getPass()
    {
        return pass;
    }

    public int getLayer()
    {
        return layer;
    }

    public Mesh[] getMeshes()
    {
        return meshes;
    }

    public Transform getTransform()
    {
        return transform;
    }
}
