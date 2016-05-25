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

public class TexturedCShader extends ShaderProgram
{
    //private static Shader vertShader = new Shader("/assets/shaders/textured_c.vert", GL_VERTEX_SHADER), fragShader = new Shader("/assets/shaders/textured_c.frag", GL_FRAGMENT_SHADER);
    private Attribute vertex, color, coords;

    public TexturedCShader()
    {
        super(new ShaderFile("/assets/shaders/textured_c.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/textured_c.frag", GL_FRAGMENT_SHADER));

        shaderID = PiEngine.TEXTURE_ID;

        vertex = new Attribute("VertexPosition", BufferUtility.vec3ToFloatBuffer(), 0, 3);
        color = new Attribute("VertexColor", BufferUtility.colorToFloatBuffer(), 1, 4);
        coords = new Attribute("VertexTexCoords", BufferUtility.vec2ToFloatBuffer(), 2, 2);
        array = new VertexArray(vertex, color, coords);
        perspName = "camera";
    }

    public void compileVertices()
    {
        FloatBuffer vertBuffer, colorBuffer, coordsBuffer;

        if(bufferSize == 0) return;
        int capacity = vertex.value.capacity()/3;

        if(capacity >= bufferSize)
        {
            vertBuffer = vertex.value;
            colorBuffer = color.value;
            coordsBuffer = coords.value;

            if(capacity > bufferSize)
            {
                vertBuffer.limit(bufferSize*3);
                colorBuffer.limit(bufferSize*4);
                coordsBuffer.limit(bufferSize*2);
            }

            vertBuffer.position(0);
            colorBuffer.position(0);
            coordsBuffer.position(0);
        }
        else
        {
            vertBuffer = BufferUtils.createFloatBuffer(bufferSize*3);
            colorBuffer = BufferUtils.createFloatBuffer(bufferSize*4);
            coordsBuffer = BufferUtils.createFloatBuffer(bufferSize*2);
        }

        buffer.forEach(renderable ->
        {
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
    }
}
