package com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader;

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

public class TexturedCShader extends ShaderProgram
{
    public TexturedCShader()
    {
        super(new ShaderFile("/assets/shaders/textured_c.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/textured_c.frag", GL_FRAGMENT_SHADER));

        shaderID = PiEngine.S_TEXTURE_ID;

        unsortedArray = new VertexArray(new Attribute("VertexPosition", 3), new Attribute("VertexColor", 4), new Attribute("VertexTexCoords", 2));
        sortedArray = new VertexArray(new Attribute("VertexPosition", 3), new Attribute("VertexColor", 4), new Attribute("VertexTexCoords", 2));
        perspName = "camera";
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

    @Override
    public void bindPRUniforms(Camera camera, Renderable r)
    {
        super.bindPRUniforms(camera, r);
        if(Window.lastTextureID != r.texture.getTexID()) r.texture.bind(0);
    }

    /*public void compileSorted()
    {
        FloatBuffer vertBuffer, colorBuffer, coordsBuffer;

        if(sortedBufferSize == 0) return;
        int capacity = vertex.buffer.capacity()/3;

        if(capacity == sortedBufferSize)
        {
            vertBuffer = vertex.buffer;
            colorBuffer = color.buffer;
            coordsBuffer = coords.buffer;

            vertBuffer.position(0);
            colorBuffer.position(0);
            coordsBuffer.position(0);
        }
        else
        {
            vertBuffer = BufferUtils.createFloatBuffer(sortedBufferSize *3);
            colorBuffer = BufferUtils.createFloatBuffer(sortedBufferSize *4);
            coordsBuffer = BufferUtils.createFloatBuffer(sortedBufferSize *2);
        }

        sortedBuffer.forEach(renderable ->
        {
            renderable.preCompile(this);
            BufferUtility.putVec3s(vertBuffer, renderable.points);
            BufferUtility.putColors(colorBuffer, renderable.colors);
            BufferUtility.putVec2s(coordsBuffer, renderable.texCoords);
        });

        vertBuffer.flip();
        colorBuffer.flip();
        coordsBuffer.flip();

        vertex.bindData(vertBuffer);
        color.bindData(colorBuffer);
        coords.bindData(coordsBuffer);
    }*/
}
