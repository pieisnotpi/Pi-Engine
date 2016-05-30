package com.pieisnotpi.engine.rendering.shaders.types;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.renderable_types.TextRenderable;
import com.pieisnotpi.engine.rendering.shaders.Attribute;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.utility.BufferUtility;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TextShader extends ShaderProgram
{
    private Attribute vertex, coords, textColor, outlineColor;

    public TextShader()
    {
        super(new ShaderFile("/assets/shaders/text.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/text.frag", GL_FRAGMENT_SHADER));

        shaderID = PiEngine.TEXT_ID;

        vertex = new Attribute("VertexPosition", BufferUtility.vec3ToFloatBuffer(), 0, 3);
        coords = new Attribute("VertexTexCoords", BufferUtility.vec2ToFloatBuffer(), 1, 2);
        textColor = new Attribute("VertexTextColor", BufferUtility.colorToFloatBuffer(), 2, 4);
        outlineColor = new Attribute("VertexOutlineColor", BufferUtility.colorToFloatBuffer(), 3, 4);
        array = new VertexArray(vertex, coords, textColor, outlineColor);
        perspName = "camera";
    }

    public void compileVertices()
    {
        FloatBuffer vertBuffer, coordsBuffer, textColorBuffer, outlineColorBuffer;

        if(bufferSize == 0) return;
        int capacity = vertex.buffer.capacity()/3;

        if(capacity == bufferSize)
        {
            vertBuffer = vertex.buffer;
            coordsBuffer = coords.buffer;
            textColorBuffer = textColor.buffer;
            outlineColorBuffer = outlineColor.buffer;

            vertBuffer.position(0);
            coordsBuffer.position(0);
            textColorBuffer.position(0);
            outlineColorBuffer.position(0);
        }
        else
        {
            vertBuffer = BufferUtils.createFloatBuffer(bufferSize*3);
            coordsBuffer = BufferUtils.createFloatBuffer(bufferSize*2);
            textColorBuffer = BufferUtils.createFloatBuffer(bufferSize*4);
            outlineColorBuffer = BufferUtils.createFloatBuffer(bufferSize*4);
        }

        buffer.forEach(renderable ->
        {
            TextRenderable temp = (TextRenderable) renderable;

            renderable.preDraw();
            BufferUtility.putVec3s(vertBuffer, temp.points);
            BufferUtility.putVec2s(coordsBuffer, temp.texCoords);
            BufferUtility.putColors(textColorBuffer, temp.textColors);
            BufferUtility.putColors(outlineColorBuffer, temp.outlineColors);
        });

        vertBuffer.flip();
        coordsBuffer.flip();
        textColorBuffer.flip();
        outlineColorBuffer.flip();

        vertex.bindData(vertBuffer);
        coords.bindData(coordsBuffer);
        textColor.bindData(textColorBuffer);
        outlineColor.bindData(outlineColorBuffer);
    }
}
