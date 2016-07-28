package com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader;

import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Attribute;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.utility.BufferUtility;

import java.util.List;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TexturedCShader extends ShaderProgram
{
    public TexturedCShader()
    {
        super(new ShaderFile("/assets/shaders/textured_c.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/textured_c.frag", GL_FRAGMENT_SHADER));

        /*unsortedArray = new VertexArray(new Attribute("VertexPosition", 3), new Attribute("VertexColor", 4), new Attribute("VertexTexCoords", 2));
        sortedArray = new VertexArray(new Attribute("VertexPosition", 3), new Attribute("VertexColor", 4), new Attribute("VertexTexCoords", 2));*/
    }

    protected void putElements(List<Renderable> buffer, Attribute[] a)
    {
        buffer.forEach(r ->
        {
            BufferUtility.putVec3s(a[0].buffer, r.points);
            BufferUtility.putColors(a[1].buffer, r.colors);
            BufferUtility.putVec2s(a[2].buffer, r.texCoords);
        });
    }
}
