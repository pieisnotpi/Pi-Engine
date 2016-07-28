package com.pieisnotpi.engine.rendering.shaders.types.color_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Attribute;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.utility.BufferUtility;

import java.util.List;

public class ColorMaterial extends Material
{
    public ColorMaterial(int matrixID)
    {
        super(PiEngine.S_COLOR_ID, matrixID);
    }

    public Attribute[] genAttributes()
    {
        return new Attribute[]{new Attribute("VertexPosition", 3, 0), new Attribute("VertexColor", 4, 1)};
    }

    @Override
    public void putElements(List<Renderable> renderables, VertexArray a)
    {
        renderables.forEach(r ->
        {
            BufferUtility.putVec3s(a.attributes[0].buffer, r.points);
            BufferUtility.putColors(a.attributes[1].buffer, r.colors);
        });
    }
}
