package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;

import java.util.List;

public abstract class Material
{
    public int shaderID, matrixID;
    public ShaderProgram shader;

    public Material(int shaderID, int matrixID)
    {
        this.shaderID = shaderID;
        this.matrixID = matrixID;
        this.shader = PiEngine.glInstance.getShaderProgram(shaderID);
    }

    public abstract Attribute[] genAttributes(boolean isStatic);
    public abstract void putElements(List<? extends Renderable> renderables, VertexArray array);
    public abstract void bind();
}
