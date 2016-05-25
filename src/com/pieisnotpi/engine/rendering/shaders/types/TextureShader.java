package com.pieisnotpi.engine.rendering.shaders.types;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.shaders.Attribute;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.utility.BufferUtility;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TextureShader extends ShaderProgram
{
    private Attribute vertex, coords;

    public TextureShader()
    {
        super(new ShaderFile("/assets/shaders/textured.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/textured.frag", GL_FRAGMENT_SHADER));

        shaderID = PiEngine.TEXTURE_ID;

        vertex = new Attribute("VertexPosition", 0, 3);
        coords = new Attribute("VertexTexCoords", 1, 2);
        array = new VertexArray(vertex, coords);
        perspName = "camera";
    }

    public void compileVertices()
    {
        FloatBuffer vertBuffer, coordsBuffer;

        if(bufferSize == 0) return;
        int capacity = vertex.buffer.capacity()/3;

        if(capacity == bufferSize)
        {
            vertBuffer = vertex.buffer;
            coordsBuffer = coords.buffer;

            vertBuffer.position(0);
            coordsBuffer.position(0);
        }
        else
        {
            vertBuffer = BufferUtils.createFloatBuffer(bufferSize*3);
            coordsBuffer = BufferUtils.createFloatBuffer(bufferSize*2);
        }

        buffer.forEach(renderable ->
        {
            BufferUtility.putVec3s(vertBuffer, renderable.points);
            BufferUtility.putVec2s(coordsBuffer, renderable.texCoords);
        });

        vertBuffer.flip();
        coordsBuffer.flip();

        vertex.bindData(vertBuffer);
        coords.bindData(coordsBuffer);
    }
}
