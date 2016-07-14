package com.pieisnotpi.engine.rendering.shaders.types;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Renderable;
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
    private Attribute vertex, color, coords;

    public TexturedCShader()
    {
        super(new ShaderFile("/assets/shaders/textured_c.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/textured_c.frag", GL_FRAGMENT_SHADER));

        shaderID = PiEngine.S_TEXTURE_ID;

        vertex = new Attribute("VertexPosition", BufferUtility.vec3ToFloatBuffer(), 0, 3);
        color = new Attribute("VertexColor", BufferUtility.colorToFloatBuffer(), 1, 4);
        coords = new Attribute("VertexTexCoords", BufferUtility.vec2ToFloatBuffer(), 2, 2);
        array = new VertexArray(vertex, color, coords);
        perspName = "camera";
    }

    protected void putElements(List<Renderable> buffer)
    {
        buffer.forEach(r ->
        {
            BufferUtility.putVec3s(vertex.buffer, r.points);
            BufferUtility.putColors(color.buffer, r.colors);
            BufferUtility.putVec2s(coords.buffer, r.texCoords);
        });
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
