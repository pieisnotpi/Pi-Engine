package com.pieisnotpi.engine.rendering.shaders.types.tex_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.rendering.shaders.Attribute;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.utility.BufferUtility;

import java.util.List;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TextureShader extends ShaderProgram
{
    public TextureShader()
    {
        super(new ShaderFile("/assets/shaders/textured.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/textured.frag", GL_FRAGMENT_SHADER));

        shaderID = PiEngine.S_TEXTURE_ID;

        unsortedArray = new VertexArray(new Attribute("VertexPosition", 3), new Attribute("VertexTexCoords", 2));
        sortedArray = new VertexArray(new Attribute("VertexPosition", 3), new Attribute("VertexTexCoords", 2));
        perspName = "camera";
    }

    protected void putElements(List<Renderable> buffer, Attribute[] a)
    {
        if(!buffer.get(0).shouldBeSorted) buffer.sort((o1, o2) ->
        {
            int s = Integer.compare(o1.getTexture().getTexID(), o2.getTexture().getTexID());
            if(s != 0) return s;
            else return Integer.compare(o1.getMatrixID(), o2.getMatrixID());
        });

        buffer.forEach(r ->
        {
            BufferUtility.putVec3s(a[0].buffer, r.points);
            BufferUtility.putVec2s(a[1].buffer, r.texCoords);
        });
    }

    @Override
    public void bindPRUniforms(Camera camera, Renderable r)
    {
        super.bindPRUniforms(camera, r);
        if(Window.lastTextureID != r.texture.getTexID()) r.texture.bind(0);
    }
}
