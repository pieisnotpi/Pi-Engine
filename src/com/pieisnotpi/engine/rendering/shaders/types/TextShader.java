package com.pieisnotpi.engine.rendering.shaders.types;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Attribute;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.ui.text.TextRenderable;
import com.pieisnotpi.engine.utility.BufferUtility;

import java.util.List;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TextShader extends ShaderProgram
{
    private Attribute vertex, coords, textColor, outlineColor;

    public TextShader()
    {
        super(new ShaderFile("/assets/shaders/text.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/text.frag", GL_FRAGMENT_SHADER));

        shaderID = PiEngine.S_TEXT_ID;

        vertex = new Attribute("VertexPosition", BufferUtility.vec3ToFloatBuffer(), 0, 3);
        coords = new Attribute("VertexTexCoords", BufferUtility.vec2ToFloatBuffer(), 1, 2);
        textColor = new Attribute("VertexTextColor", BufferUtility.colorToFloatBuffer(), 2, 4);
        outlineColor = new Attribute("VertexOutlineColor", BufferUtility.colorToFloatBuffer(), 3, 4);
        array = new VertexArray(vertex, coords, textColor, outlineColor);
        perspName = "camera";
    }

    protected void putElements(List<Renderable> buffer)
    {
        buffer.forEach(r ->
        {
            TextRenderable t = (TextRenderable) r;

            BufferUtility.putVec3s(vertex.buffer, t.points);
            BufferUtility.putVec2s(coords.buffer, t.texCoords);
            BufferUtility.putColors(textColor.buffer, t.textColors);
            BufferUtility.putColors(outlineColor.buffer, t.outlineColors);
        });
    }

    /*public void compileSorted()
    {
        FloatBuffer vertBuffer, coordsBuffer, textColorBuffer, outlineColorBuffer;

        if(sortedBufferSize == 0) return;
        int capacity = vertex.buffer.capacity()/3;

        if(capacity == sortedBufferSize)
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
            vertBuffer = BufferUtils.createFloatBuffer(sortedBufferSize*3);
            coordsBuffer = BufferUtils.createFloatBuffer(sortedBufferSize*2);
            textColorBuffer = BufferUtils.createFloatBuffer(sortedBufferSize*4);
            outlineColorBuffer = BufferUtils.createFloatBuffer(sortedBufferSize*4);
        }

        sortedBuffer.forEach(renderable ->
        {
            TextRenderable temp = (TextRenderable) renderable;

            renderable.preCompile(this);
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
    }*/
}
