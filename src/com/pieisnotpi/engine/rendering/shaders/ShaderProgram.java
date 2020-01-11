package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.Transform;
import com.pieisnotpi.engine.rendering.window.Window;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram
{
    public static final int primitiveRestart = Integer.MAX_VALUE - 1;
    private static final FloatBuffer vec2b = BufferUtils.createFloatBuffer(2), vec3b = BufferUtils.createFloatBuffer(3), vec4b = BufferUtils.createFloatBuffer(4), mat3b = BufferUtils.createFloatBuffer(9), mat4b = BufferUtils.createFloatBuffer(16);

    private ShaderFile[] shaderFiles;
    private Map<String, Integer> uniformLocations = new HashMap<>(), attribLocations = new HashMap<>();
    private Camera lastCamera;
    protected Window window;

    private int handle, lastMatrix = -1;

    public ShaderProgram(Window window, ShaderFile... shaderFiles)
    {
        this.window = window;
        this.shaderFiles = shaderFiles;

        handle = glCreateProgram();
    }

    public ShaderProgram init()
    {
        for(ShaderFile shader : shaderFiles) shader.attach(this);

        glLinkProgram(handle);
        glUseProgram(handle);

        return this;
    }

    public ShaderProgram reload()
    {
        glDeleteProgram(handle);
        handle = glCreateProgram();
        init();

        Logger.OPENGL.debug("Shader program '" + handle + "' reloaded");

        return this;
    }

    /**
     * Binds uniforms that are independent of individual meshes
     * Called once per shader draw
     * @param camera The camera being drawn
     */

    public void bindUniforms(Camera camera) {}
    
    /**
     * Binds uniforms that are dependent on the current mesh
     * Called once per mesh draw
     * @param camera The camera being drawn
     * @param mesh The current mesh being drawn
     */

    public void bindPMUniforms(Transform transform, Camera camera, Mesh mesh)
    {
        setUniformMat4("transform", transform.getBuffer());
        if(lastMatrix != mesh.getMaterial().matrixID) setUniformMat4("camera", camera.getMatrix(mesh.getMaterial().matrixID).getBuffer());
    }

    public void draw(Transform transform, Mesh mesh, Camera camera)
    {
        use();

        if(!camera.equals(lastCamera))
        {
            lastCamera = camera;
            bindUniforms(camera);
        }

        if(mesh.shouldBuild()) mesh.build();
        mesh.bind();

        bindPMUniforms(transform, camera, mesh);
        glDrawElements(mesh.getDrawMode(), mesh.getPrimCount(), GL_UNSIGNED_INT, 0);
    }

    public void use()
    {
        if(PiEngine.glInstance.lastShaderID != handle)
        {
            glUseProgram(PiEngine.glInstance.lastShaderID = handle);
            lastMatrix = -1;
            lastCamera = null;
        }
    }

    public void setUniformMat3(String name, Matrix3f mat3)
    {
        int location = getUniformLocation(name);
        mat3.get(mat3b);

        if(location > -1) glUniformMatrix3fv(location, false, mat3b);
        else Logger.OPENGL.err("Program " + handle + " attempted to set non-existent uniform '" + name + '\'');
    }

    public void setUniformMat3(String name, FloatBuffer buffer)
    {
        int location = getUniformLocation(name);

        if(location > -1) glUniformMatrix3fv(location, false, buffer);
        else Logger.OPENGL.err("Program " + handle + " attempted to set non-existent uniform '" + name + '\'');
    }

    public void setUniformMat4(String name, Matrix4f mat4)
    {
        int location = getUniformLocation(name);
        mat4.get(mat4b);

        if(location > -1) glUniformMatrix4fv(location, false, mat4b);
        else Logger.OPENGL.err("Program " + handle + " attempted to set non-existent uniform '" + name + '\'');
    }

    public void setUniformMat4(String name, FloatBuffer buffer)
    {
        int location = getUniformLocation(name);
        if(location > -1) glUniformMatrix4fv(location, false, buffer);
    }

    public void setUniformVec2(String name, Vector2f vec2)
    {
        int location = getUniformLocation(name);
        vec2.get(vec2b);
    
        if(location > -1) glUniform2fv(location, vec2b);
        vec2b.clear();
    }
    
    public void setUniformVec3(String name, Vector3f vec3)
    {
        int location = getUniformLocation(name);
        vec3.get(vec3b);

        if(location > -1) glUniform3fv(location, vec3b);
        vec3b.clear();
    }

    public void setUniformVec4(String name, Vector4f vec4)
    {
        int location = getUniformLocation(name);
        vec4.get(vec4b);

        if(location > -1) glUniform4fv(location, vec4b);
        vec4b.clear();
    }

    public void setUniformInt(String name, int value)
    {
        int location = getUniformLocation(name);

        if(location > -1) glUniform1i(location, value);
    }

    public void setUniformFloat(String name, float value)
    {
        int location = getUniformLocation(name);

        if(location > -1) glUniform1f(location, value);
    }

    public void setUniformBoolean(String name, boolean value)
    {
        int location = getUniformLocation(name);
        if(location > -1) glUniform1i(location, value ? 1 : 0);
    }

    public int getHandle()
    {
        return handle;
    }

    public int getUniformLocation(String name)
    {
        Integer t = uniformLocations.get(name);
        if(t == null)
        {
            t = glGetUniformLocation(handle, name);
            if(t != -1) uniformLocations.put(name, t);
            else Logger.OPENGL.err("Program " + handle + " attempted to find non-existent uniform '" + name + '\'');
        }
        return t;
    }

    public int getAttribLocation(String name)
    {
        Integer t = attribLocations.get(name);
        if(t == null)
        {
            t = glGetAttribLocation(handle, name);
            if(t != -1) attribLocations.put(name, t);
            else Logger.OPENGL.err("Program " + handle + " attempted to find non-existent attribute '" + name + '\'');
        }
        return t;
    }

    public String toString()
    {
        StringBuilder temp = new StringBuilder();

        for(int i = 0; i < shaderFiles.length; i++)
        {
            ShaderFile s = shaderFiles[i];

            if(i != shaderFiles.length - 1) temp.append(String.format("s%d: %s,", i, s));
            else temp.append(String.format("s%d: %s", i, s));
        }

        return temp.toString();
    }
}
