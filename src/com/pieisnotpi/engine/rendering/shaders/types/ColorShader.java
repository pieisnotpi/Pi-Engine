package com.pieisnotpi.engine.rendering.shaders.types;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.renderable_types.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Attribute;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.utility.BufferUtility;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class ColorShader extends ShaderProgram
{
    private Attribute vertex, colors;

    public ColorShader()
    {
        super(new ShaderFile("/assets/shaders/color.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/color.frag", GL_FRAGMENT_SHADER));

        shaderID = PiEngine.COLOR_ID;

        vertex = new Attribute("VertexPosition", BufferUtility.vec3ToFloatBuffer(), 0, 3);
        colors = new Attribute("VertexColor", BufferUtility.colorToFloatBuffer(), 1, 4);
        array = new VertexArray(vertex, colors);
        perspName = "camera";
    }

    public void compileVertices()
    {
        FloatBuffer vertBuffer, colorBuffer;

        if(bufferSize == 0) return;
        int capacity = vertex.value.capacity()/(bufferSize*3);

        if(this.vertex.value.capacity()/(bufferSize*3) == buffer.size())
        {
            vertBuffer = vertex.value;
            colorBuffer = colors.value;

            if(capacity > buffer.size())
            {
                vertBuffer.limit(bufferSize*3*buffer.size());
                colorBuffer.limit(bufferSize*4*buffer.size());
            }

            vertBuffer.position(0);
            colorBuffer.position(0);
        }
        else
        {
            vertBuffer = BufferUtils.createFloatBuffer(bufferSize*3*buffer.size());
            colorBuffer = BufferUtils.createFloatBuffer(bufferSize*4*buffer.size());
        }

        for(Renderable renderable : buffer)
        {
            BufferUtility.putVec3s(vertBuffer, renderable.points);
            BufferUtility.putColors(colorBuffer, renderable.colors);
        }

        vertBuffer.flip();
        colorBuffer.flip();

        vertex.bindData(vertBuffer);
        colors.bindData(colorBuffer);
    }
}
