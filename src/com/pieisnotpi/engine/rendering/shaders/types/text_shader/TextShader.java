package com.pieisnotpi.engine.rendering.shaders.types.text_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.Window;
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
    public TextShader()
    {
        super(new ShaderFile("/assets/shaders/text.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/text.frag", GL_FRAGMENT_SHADER));

        shaderID = PiEngine.S_TEXT_ID;

        unsortedArray = new VertexArray(new Attribute("VertexPosition", 3), new Attribute("VertexTexCoords", 2), new Attribute("VertexTextColor", 4), new Attribute("VertexOutlineColor", 4));
        sortedArray = new VertexArray(new Attribute("VertexPosition", 3), new Attribute("VertexTexCoords", 2), new Attribute("VertexTextColor", 4), new Attribute("VertexOutlineColor", 4));
        perspName = "camera";
    }

    @Override
    public void bindPRUniforms(Camera camera, Renderable r)
    {
        super.bindPRUniforms(camera, r);
        if(Window.lastTextureID != r.texture.getTexID()) r.texture.bind(0);
    }

    protected void putElements(List<Renderable> buffer, Attribute[] a)
    {
        buffer.forEach(r ->
        {
            TextRenderable t = (TextRenderable) r;

            BufferUtility.putVec3s(a[0].buffer, t.points);
            BufferUtility.putVec2s(a[1].buffer, t.texCoords);
            BufferUtility.putColors(a[2].buffer, t.textColors);
            BufferUtility.putColors(a[3].buffer, t.outlineColors);
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
