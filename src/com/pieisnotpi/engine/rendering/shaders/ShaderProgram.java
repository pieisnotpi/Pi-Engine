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
    public List<Renderable> buffer;
    protected Window window;
    protected String perspName;
    protected VertexArray array;
    protected ShaderFile[] shaders;
    protected Matrix4f perspective;

    public int program, shaderID;
    protected int bufferSize = 0;
    private int current = 0, vertCount = -100, lastMatrix = -1, lastSampler = -1;

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
        Texture tex = renderable.getTexture();
        int matrixID = renderable.getMatrixID();

        if(current == 0) vertCount = -renderable.getVertCount();

        renderable.preDraw(this);

        if(matrixID != lastMatrix)
        {
            setUniformMat4(perspName, BufferUtility.mat4ToFloatBuffer(camera.matrices[lastMatrix = matrixID]));
        }

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

        current++;
    }

    public void use()
    {
        if(Window.lastShaderID == program) return;

        glUseProgram(program);

        Window.lastShaderID = program;

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

    public void setUniformFloat(String name, float value)
    {
        use();
        int location = glGetUniformLocation(program, name);

        if(location > -1) glUniform1f(location, value);
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

        lastSampler = -1;
        lastMatrix = -1;
        current = 0;
        vertCount = -100;
        bufferSize = 0;
    }
}
