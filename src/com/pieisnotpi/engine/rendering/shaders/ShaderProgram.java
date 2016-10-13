package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
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

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

public abstract class ShaderProgram
{
    public static final int primitiveRestart = Integer.MAX_VALUE - 1;
    private static final FloatBuffer vec3b = BufferUtils.createFloatBuffer(3), vec4b = BufferUtils.createFloatBuffer(4), mat3b = BufferUtils.createFloatBuffer(9), mat4b = BufferUtils.createFloatBuffer(16);

    public List<Mesh> unsortedMeshes;
    protected ShaderFile[] shaderFiles;
    protected String perspName = "cameras", transformName = "transform", mIDName = "mID", samplerName = "sampler";
    private Map<String, Integer> uniformLocations = new HashMap<>();

    protected int handle;
    protected int index = 0, vertCount = 0, lastMatrix = -1, lastSampler = -1;

    public ShaderProgram(ShaderFile... shaderFiles)
    {
        this.shaderFiles = shaderFiles;

        handle = glCreateProgram();
        unsortedMeshes = new ArrayList<>(100);
    }

    public ShaderProgram init()
    {
        for(ShaderFile shader : shaderFiles) shader.attach(handle);

        glLinkProgram(handle);
        glUseProgram(handle);

        return this;
    }

    public void addUnsortedMesh(Mesh mesh)
    {
        if(!unsortedMeshes.contains(mesh)) unsortedMeshes.add(mesh);
    }

    public void removeUnsortedMesh(Mesh mesh)
    {
        if(unsortedMeshes.contains(mesh)) unsortedMeshes.remove(mesh);
    }

    /**
     * Binds uniforms that are independent of individual meshes
     * Called once per shader draw
     * @param camera The camera being drawn
     */

    public void bindUniforms(Camera camera)
    {
        for(int i = 0; i < camera.matrices.length; i++) setUniformMat4(String.format("%s[%d]", perspName, i), camera.matrices[i]);
    }

    /**
     * Binds uniforms that are dependent on the current mesh
     * Called once per mesh draw
     * @param camera The camera being drawn
     * @param mesh The current mesh being drawn
     */

    public void bindPMUniforms(Camera camera, Mesh mesh)
    {
        if(mesh.material.matrixID != lastMatrix) setUniformInt(mIDName, mesh.material.matrixID);
        setUniformMat4("transform", mesh.getTransform().getBuffer());
    }

    public void drawUnsorted(Camera camera)
    {
        if(unsortedMeshes.size() == 0) return;

        use();

        bindUniforms(camera);

        unsortedMeshes.forEach((m) ->
        {
            if(m.shouldBuild()) m.build();

            m.array.bind();
            m.indices.bind();

            bindPMUniforms(camera, m);
            glDrawElementsInstanced(m.getDrawMode(), m.indices.count, GL_UNSIGNED_INT, 0, 1);
        });

        clearUnsorted();
    }

    public void use()
    {
        if(PiEngine.glInstance.lastShaderID != handle) glUseProgram(PiEngine.glInstance.lastShaderID = handle);
    }

    public void setUniformMat3(String name, Matrix3f mat3)
    {
        int location = getUniformLocation(name);
        mat3.get(mat3b);

        if(location > -1) glUniformMatrix3fv(location, false, mat3b);
        else Logger.SHADER_PROGRAM.err("Program " + handle + " attempted to set non-existent uniform '" + name + '\'');
    }

    public void setUniformMat3(String name, FloatBuffer buffer)
    {
        int location = getUniformLocation(name);

        if(location > -1) glUniformMatrix3fv(location, false, buffer);
        else Logger.SHADER_PROGRAM.err("Program " + handle + " attempted to set non-existent uniform '" + name + '\'');
    }

    public void setUniformMat4(String name, Matrix4f mat4)
    {
        int location = getUniformLocation(name);
        mat4.get(mat4b);

        if(location > -1) glUniformMatrix4fv(location, false, mat4b);
        else Logger.SHADER_PROGRAM.err("Program " + handle + " attempted to set non-existent uniform '" + name + '\'');
    }

    public void setUniformMat4(String name, FloatBuffer buffer)
    {
        int location = getUniformLocation(name);
        if(location > -1) glUniformMatrix4fv(location, false, buffer);
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

    private int getUniformLocation(String name)
    {
        Integer t = uniformLocations.get(name);
        if(t == null)
        {
            t = glGetUniformLocation(handle, name);
            if(t != -1) uniformLocations.put(name, t);
            else Logger.SHADER_PROGRAM.err("Program " + handle + " attempted to find non-existent uniform '" + name + '\'');
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

    public void finalize() throws Throwable
    {
        super.finalize();

        glDeleteProgram(handle);
    }

    public void clearUnsorted()
    {
        lastMatrix = -1;
    }
}
