package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.rendering.Renderable;

import java.util.List;

public abstract class Material
{
    public int shaderID, matrixID;
    public ShaderProgram shader = null;

    public Material(int shaderID, int matrixID)
    {
        this.shaderID = shaderID;
        this.matrixID = matrixID;
    }

    public abstract Attribute[] genAttributes();
    public abstract void putElements(List<Renderable> renderables, VertexArray array);
}
