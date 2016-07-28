package com.pieisnotpi.engine.rendering.shaders.types.text_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Attribute;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.rendering.ui.text.TextRenderable;
import com.pieisnotpi.engine.utility.BufferUtility;

import java.util.List;

public class TextMaterial extends Material
{
    public Texture[] textures;

    public TextMaterial(int matrixID, Texture... textures)
    {
        super(PiEngine.S_TEXT_ID, matrixID);
        this.textures = textures;
    }

    @Override
    public Attribute[] genAttributes()
    {
        return new Attribute[]{new Attribute("VertexPosition", 3, 0), new Attribute("VertexTexCoords", 2, 1), new Attribute("VertexTextColor", 4, 2), new Attribute("VertexOutlineColor", 4, 3)};
    }

    @Override
    public void putElements(List<Renderable> buffer, VertexArray a)
    {
        buffer.forEach(renderable ->
        {
            TextRenderable r = (TextRenderable) renderable;

            BufferUtility.putVec3s(a.attributes[0].buffer, r.points);
            BufferUtility.putVec2s(a.attributes[1].buffer, r.texCoords);
            BufferUtility.putColors(a.attributes[2].buffer, r.textColors);
            BufferUtility.putColors(a.attributes[3].buffer, r.outlineColors);
        });
    }
}
