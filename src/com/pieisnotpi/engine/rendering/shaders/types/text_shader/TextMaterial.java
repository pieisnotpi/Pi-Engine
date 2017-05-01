package com.pieisnotpi.engine.rendering.shaders.types.text_shader;

import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.ui.text.TextRenderable;
import com.pieisnotpi.engine.utility.BufferUtility;

import java.util.List;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class TextMaterial extends Material
{
    public Texture[] textures;
    public int outlineSize = 0;

    public TextMaterial(int outlineSize, int matrixID, Texture... textures)
    {
        super(TextShader.ID, matrixID);
        this.outlineSize = outlineSize;
        this.textures = textures;
    }

    @Override
    public Attribute[] genAttributes(boolean isStatic)
    {
        int mode = GL_DYNAMIC_DRAW;
        if(isStatic) mode = GL_STATIC_DRAW;

        return new Attribute[]{new Attribute("VertexPosition", shader, 3, mode, isStatic), new Attribute("VertexTexCoord", shader, 2, mode, isStatic), new Attribute("VertexTextColor", shader, 4, mode, isStatic), new Attribute("VertexOutlineColor", shader, 4, mode, isStatic)};
    }

    @Override
    public void putElements(List<? extends Renderable> buffer, VertexArray a)
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
