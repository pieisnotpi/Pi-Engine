package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.rendering.textures.Texture;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram
{
    public List<Renderable> unsortedBuffer;
    public List<Renderable> sortedBuffer;
    protected Window window;
    protected String perspName = "camera", samplerName = "sampler";
    protected VertexArray array;
    protected ShaderFile[] shaders;
    protected Matrix4f perspective;
    private Map<String, Integer> uniformLocations = new HashMap<>();

    protected int programID, shaderID, sortedBufferSize = 0, unsortedBufferSize = 0;
    private int index = 0, vertCount = -100, lastMatrix = -1, lastSampler = -1;

    private static FloatBuffer vec3b = BufferUtils.createFloatBuffer(3), vec4b = BufferUtils.createFloatBuffer(4), mat3b = BufferUtils.createFloatBuffer(9), mat4b = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(ShaderFile... shaders)
    {
        this.shaders = shaders;

        programID = glCreateProgram();
        unsortedBuffer = new ArrayList<>(100);
        sortedBuffer = new ArrayList<>(20);
    }

    public ShaderProgram init()
    {
        for(ShaderFile shader : shaders) shader.attach(programID);

        glLinkProgram(programID);
        glUseProgram(programID);

        array.init(programID);

        return this;
    }

    public void compileUnsorted()
    {
        if(unsortedBufferSize == 0) return;

        for(Attribute attribute : array.attributes)
        {
            int size = attribute.size*unsortedBufferSize;

            if(attribute.buffer.limit() >= size)
            {
                attribute.buffer.position(0);
                if(attribute.buffer.limit() > size) attribute.buffer.limit(size);
            }
            else attribute.buffer = BufferUtils.createFloatBuffer(size);
        }

        putElements(unsortedBuffer);

        for(Attribute attribute : array.attributes)
        {
            attribute.buffer.flip();
            attribute.bindData();
        }
    }

    public void compileSorted()
    {
        if(sortedBufferSize == 0) return;

        for(Attribute attribute : array.attributes)
        {
            int size = attribute.size*sortedBufferSize;

            if(attribute.buffer.limit() >= size)
            {
                attribute.buffer.position(0);
                if(attribute.buffer.limit() > size) attribute.buffer.limit(size);
            }
            else attribute.buffer = BufferUtils.createFloatBuffer(size);
        }

        putElements(sortedBuffer);

        for(Attribute attribute : array.attributes)
        {
            attribute.buffer.flip();
            attribute.bindData();
        }
    }

    protected abstract void putElements(List<Renderable> buffer);

    public void addUnsortedVertex(Renderable renderable)
    {
        if(!unsortedBuffer.contains(renderable))
        {
            unsortedBufferSize += renderable.getVertCount();
            unsortedBuffer.add(renderable);
        }
    }

    public void removeUnsortedVertex(Renderable renderable)
    {
        if(unsortedBuffer.contains(renderable))
        {
            unsortedBufferSize -= renderable.getVertCount();
            unsortedBuffer.remove(renderable);
        }
    }

    public void addSortedVertex(Renderable renderable)
    {
        if(!sortedBuffer.contains(renderable))
        {
            sortedBuffer.add(renderable);
            sortedBufferSize += renderable.getVertCount();
        }
    }

    /**
     * Binds uniforms that are independent of individual renderables
     * Called once per buffer draw
     */

    public void bindUniforms(Camera camera) {}

    /**
     * Binds uniforms that are dependent on individual renderables
     * Called once per renderable draw
     * @param renderable The current renderable
     */

    public void bindPRUniforms(Camera camera, Renderable renderable)
    {
        if(renderable.getMatrixID() != lastMatrix) setUniformMat4(perspName, camera.matrices[lastMatrix = renderable.getMatrixID()]);
    }

    public void drawUnsorted(Camera camera)
    {
        if(unsortedBuffer.size() == 0) return;

        compileUnsorted();
        bindUniforms(camera);

        int curVert = -unsortedBuffer.get(0).getVertCount();

        for(Renderable r : unsortedBuffer)
        {
            Texture tex = r.getTexture();
            int matrixID = r.getMatrixID();

            r.preDraw(this);

            bindPRUniforms(camera, r);

            if(tex != null)
            {
                if(tex.getTexID() != Window.lastTextureID)
                {
                    tex.bind();
                    setUniformInt(samplerName, lastSampler = tex.getSamplerID());
                }
                else if(lastSampler == -1) setUniformInt(samplerName, lastSampler = tex.getSamplerID());

                if(tex.getSamplerID() != lastSampler) setUniformInt(samplerName, lastSampler = tex.getSamplerID());
            }

            glDrawArrays(r.getDrawMode(), curVert += r.getVertCount(), r.getVertCount());
        }

        clearUnsorted();
    }

    public void drawNextSorted(Camera camera)
    {
        if(sortedBuffer.size() == 0) return;

        Renderable renderable = sortedBuffer.get(index);
        Texture tex = renderable.getTexture();
        int matrixID = renderable.getMatrixID();

        if(index == 0)
        {
            compileSorted();
            bindUniforms(camera);
            vertCount = -renderable.getVertCount();
        }

        renderable.preDraw(this);

        bindPRUniforms(camera, renderable);

        if(tex != null)
        {
            if(tex.getTexID() != Window.lastTextureID)
            {
                tex.bind();
                setUniformInt("sampler", lastSampler = tex.getSamplerID());
            }
            else if(lastSampler == -1) setUniformInt("sampler", lastSampler = tex.getSamplerID());

            if(tex.getSamplerID() != lastSampler) setUniformInt("sampler", lastSampler = tex.getSamplerID());
        }

        glDrawArrays(renderable.getDrawMode(), vertCount += renderable.getVertCount(), renderable.getVertCount());

        index++;
    }

    public void use()
    {
        if(Window.lastShaderID == programID) return;

        glUseProgram(programID);

        Window.lastShaderID = programID;

        if(array != null) array.bind();
        else Logger.SHADER_PROGRAM.err("Attempted to bind a null array, shader program must be corrupt");
    }

    public void setUniformMat3(String name, Matrix3f mat3)
    {
        use();
        int location = getUniformLocation(name);
        mat3.get(mat3b);

        if(location > -1) glUniformMatrix3fv(location, false, mat3b);
        else Logger.SHADER_PROGRAM.err("Program " + programID + " attempted to set non-existent uniform '" + name + '\'');
    }

    public void setUniformMat4(String name, Matrix4f mat4)
    {
        use();
        int location = getUniformLocation(name);
        mat4.get(mat4b);

        if(location > -1) glUniformMatrix4fv(location, false, mat4b);
    }

    public void setUniformVec3(String name, Vector3f vec3)
    {
        use();
        int location = getUniformLocation(name);
        vec3.get(vec3b);

        if(location > -1) glUniform3fv(location, vec3b);
    }

    public void setUniformVec4(String name, Vector4f vec4)
    {
        use();
        int location = getUniformLocation(name);
        vec4.get(vec4b);

        if(location > -1) glUniform4fv(location, vec4b);
    }

    public void setUniformInt(String name, int value)
    {
        use();
        int location = getUniformLocation(name);

        if(location > -1) glUniform1i(location, value);
    }

    public void setUniformFloat(String name, float value)
    {
        use();
        int location = getUniformLocation(name);

        if(location > -1) glUniform1f(location, value);
    }

    private int getUniformLocation(String name)
    {
        Integer t = uniformLocations.get(name);
        if(t == null)
        {
            t = glGetUniformLocation(programID, name);
            if(t  > -1) uniformLocations.put(name, t);
            else Logger.SHADER_PROGRAM.err("Program " + programID + " attempted to find non-existent uniform '" + name + '\'');
        }
        return t;
    }

    public String toString()
    {
        StringBuilder temp = new StringBuilder();

        for(int i = 0; i < shaders.length; i++)
        {
            ShaderFile s = shaders[i];

            if(i != shaders.length - 1) temp.append("s").append(i).append(": ").append(s).append(", ");
            else temp.append("s").append(i).append(": ").append(s);
        }

        return temp.toString();
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        glDeleteProgram(programID);
    }

    public void clearSorted()
    {
        sortedBuffer.clear();

        lastSampler = -1;
        lastMatrix = -1;
        index = 0;
        vertCount = -100;
        sortedBufferSize = 0;
    }

    public void clearUnsorted()
    {
        lastSampler = -1;
        lastMatrix = -1;
    }
}
