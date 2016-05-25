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
    //private static ShaderFile vertShader = new ShaderFile("/assets/shaders/textured.vert", GL_VERTEX_SHADER), fragShader = new ShaderFile("/assets/shaders/textured.frag", GL_FRAGMENT_SHADER);
    private Attribute vertex, coords;

    public TextureShader()
    {
        super(new ShaderFile("/assets/shaders/textured.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/textured.frag", GL_FRAGMENT_SHADER));
        //super(vertShader, fragShader);

        shaderID = PiEngine.TEXTURE_ID;

        vertex = new Attribute("VertexPosition", BufferUtility.vec3ToFloatBuffer(), 0, 3);
        coords = new Attribute("VertexTexCoords", BufferUtility.vec2ToFloatBuffer(), 1, 2);
        array = new VertexArray(vertex, coords);
        perspName = "camera";
    }

    public void compileVertices()
    {
        FloatBuffer vertBuffer, coordsBuffer;

        if(bufferSize == 0) return;
        int capacity = vertex.value.capacity()/3;

        if(capacity >= bufferSize)
        {
            vertBuffer = vertex.value;
            coordsBuffer = coords.value;

            if(capacity > bufferSize)
            {
                vertBuffer.limit(bufferSize*3);
                coordsBuffer.limit(bufferSize*2);
            }

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
