package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.rendering.renderable_types.Renderable;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.utility.BufferUtility;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram
{
    protected ShaderFile[] shaders;
    public List<Renderable> buffer;
    protected VertexArray array;
    protected Matrix4f perspective;

    public int program, shaderID;
    protected String perspName;
    protected Window window;
    private int current = 0, vertCount = -100, lastMatrix = -1, lastSampler = -1;
    protected int bufferSize = 0;

    Texture currentTex = null;

    public ShaderProgram(ShaderFile... shaders)
    {
        this.shaders = shaders;

        program = glCreateProgram();
        buffer = new ArrayList<>(100);
    }

    public void init()
    {
        for(ShaderFile shader : shaders) shader.attach(program);

        glLinkProgram(program);
        glUseProgram(program);

        array.init(program);
    }

    public void compileVertices() {}

    public void addVertex(Renderable... renderables)
    {
        for(Renderable renderable : renderables) if(!buffer.contains(renderable))
        {
            buffer.add(renderable);
            bufferSize += renderable.getVertCount();
        }
    }

    public void drawNext(Camera camera)
    {
        Renderable renderable = buffer.get(current);

        if(current == 0) vertCount = -renderable.getVertCount();

        int matrixID = renderable.getMatrixID();

        if(matrixID != lastMatrix) setUniformMat4(perspName, BufferUtility.mat4ToFloatBuffer(camera.matrices[matrixID]));
        if(renderable.getTexture() != null && lastSampler != renderable.getTexture().getSamplerID()) setUniformInt("sampler", renderable.getTexture().getSamplerID());

        if(renderable.getTexture() != null && !renderable.getTexture().equals(currentTex))
        {
            currentTex = renderable.getTexture();
            currentTex.bind();
        }

        lastMatrix = matrixID;

        glDrawArrays(renderable.getDrawMode(), vertCount += renderable.getVertCount(), renderable.getVertCount());

        current++;
    }

    public void draw(Camera camera)
    {
        compileVertices();
        use();

        Texture currentTex = null;
        int lastMatrix = -1, lastSampler = -1;

        int current = -100;

        for(int i = 0; i < buffer.size(); i++)
        {
            Renderable renderable = buffer.get(i);

            if(i == 0) current = -renderable.getVertCount();

            int matrixID = renderable.getMatrixID();

            if(matrixID != lastMatrix) setUniformMat4(perspName, BufferUtility.mat4ToFloatBuffer(camera.matrices[matrixID]));
            if(renderable.getTexture() != null && lastSampler != renderable.getTexture().getSamplerID()) setUniformInt("sampler", renderable.getTexture().getSamplerID());

            if(renderable.getTexture() != null && !renderable.getTexture().equals(currentTex))
            {
                currentTex = renderable.getTexture();
                currentTex.bind();
            }

            lastMatrix = matrixID;

            glDrawArrays(renderable.getDrawMode(), current += renderable.getVertCount(), renderable.getVertCount());
        }

        buffer.clear();
    }

    public void use()
    {
        glUseProgram(program);

        if(array != null) array.bind();
        else Logger.SHADER_PROGRAM.err("Attempted to bind a null array, shader program must be corrupt");
    }

    public void setUniformMat4(String name, FloatBuffer value)
    {
        use();
        int location = glGetUniformLocation(program, name);

        if(location > -1) glUniformMatrix4fv(location, false, value);
        else Logger.SHADER_PROGRAM.err("Program " + program + " attempted to set non-existent uniform '" + name + '\'');
    }

    public void setUniformVec3(String name, FloatBuffer value)
    {
        use();
        int location = glGetUniformLocation(program, name);

        if(location > -1) glUniform3fv(location, value);
        else Logger.SHADER_PROGRAM.err("Program " + program + " attempted to set non-existent uniform '" + name + '\'');
    }
    public void setUniformVec4(String name, FloatBuffer value)
    {
        use();
        int location = glGetUniformLocation(program, name);

        if(location > -1) glUniform4fv(location, value);
        else Logger.SHADER_PROGRAM.err("Program " + program + " attempted to set non-existent uniform '" + name + '\'');
    }

    public void setUniformInt(String name, int value)
    {
        use();
        int location = glGetUniformLocation(program, name);

        if(location > -1) glUniform1i(location, value);
        else Logger.SHADER_PROGRAM.err("Program " + program + " attempted to set non-existent uniform '" + name + '\'');
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

        glDeleteProgram(program);
    }

    public void clear()
    {
        buffer.clear();

        currentTex = null;
        current = 0;
        vertCount = -100;
        lastMatrix = -1;
        lastSampler = -1;
        bufferSize = 0;
    }
}
