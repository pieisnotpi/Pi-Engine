package com.pieisnotpi.engine.rendering.shaders.types.color_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;
import com.pieisnotpi.engine.utility.BufferUtility;

import java.util.List;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class ColorMaterial extends Material
{
    public ColorMaterial(int matrixID)
    {
        super(PiEngine.S_COLOR_ID, matrixID);
    }

    public Attribute[] genAttributes(boolean isStatic)
    {
        int mode = GL_DYNAMIC_DRAW;
        if(isStatic) mode = GL_STATIC_DRAW;

        return new Attribute[]{new Attribute("VertexPosition", 3, 0, mode, isStatic), new Attribute("VertexColor", 4, 1, mode, isStatic)};
    }

    @Override
    public void putElements(List<? extends Renderable> renderables, VertexArray a)
    {
        renderables.forEach(r ->
        {
            BufferUtility.putVec3s(a.attributes[0].buffer, r.points);
            BufferUtility.putColors(a.attributes[1].buffer, r.colors);
        });
    }
}
