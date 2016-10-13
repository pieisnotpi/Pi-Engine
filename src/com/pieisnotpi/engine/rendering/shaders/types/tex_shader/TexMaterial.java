package com.pieisnotpi.engine.rendering.shaders.types.tex_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.utility.BufferUtility;

import java.util.List;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class TexMaterial extends Material
{
    public Texture[] textures;

    public TexMaterial(int matrixID, Texture... textures)
    {
        super(PiEngine.S_TEXTURE_ID, matrixID);
        this.textures = textures;
    }

    @Override
    public Attribute[] genAttributes(boolean isStatic)
    {
        int mode = isStatic ? GL_STATIC_DRAW : GL_DYNAMIC_DRAW;
        return new Attribute[]{new Attribute("VertexPosition", 3, 0, mode, isStatic), new Attribute("VertexTexCoords", 2, 1, mode, isStatic)};
    }

    @Override
    public void putElements(List<? extends Renderable> renderables, VertexArray a)
    {
        renderables.forEach(r ->
        {
            BufferUtility.putVec3s(a.attributes[0].buffer, r.points);
            BufferUtility.putVec2s(a.attributes[1].buffer, r.texCoords);
        });
    }
}
