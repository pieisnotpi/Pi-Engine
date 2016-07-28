package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Mesh;
import com.pieisnotpi.engine.rendering.Window;
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

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

public abstract class ShaderProgram
{
    public static final int primitiveRestart = -1;

    public List<Mesh> unsortedMeshes/*, sortedMeshes*/;
    protected Window window;
    protected String perspName = "cameras", transformName = "transform", mIDName = "mID", samplerName = "sampler";
    protected ShaderFile[] shaders;
    private Map<String, Integer> uniformLocations = new HashMap<>();

    protected int programID;
    protected int index = 0, vertCount = 0, lastMatrix = -1, lastSampler = -1;

    private static FloatBuffer vec3b = BufferUtils.createFloatBuffer(3), vec4b = BufferUtils.createFloatBuffer(4), mat3b = BufferUtils.createFloatBuffer(9), mat4b = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(ShaderFile... shaders)
    {
        this.shaders = shaders;

        programID = glCreateProgram();
        unsortedMeshes = new ArrayList<>(100);
        //sortedMeshes = new ArrayList<>(100);
    }

    public ShaderProgram init()
    {
        for(ShaderFile shader : shaders) shader.attach(programID);

        glLinkProgram(programID);
        glUseProgram(programID);

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

    /*public void addSortedVertex(Renderable renderable)
    {
        if(!sortedBuffer.contains(renderable))
        {
            sortedBuffer.add(renderable);
            sortedBufferSize += renderable.getVertCount();
        }
    }*/

    /**
     * Binds uniforms that are independent of individual renderables
     * Called once per buffer draw
     */

    public void bindUniforms(Camera camera)
    {
        for(int i = 0; i < camera.matrices.length; i++) setUniformMat4(String.format("%s[%d]", perspName, i), camera.matrices[i]);
    }

    public void bindPMUniforms(Camera camera, Mesh mesh)
    {
        if(mesh.material.matrixID != lastMatrix) setUniformInt(mIDName, mesh.material.matrixID);
        setUniformMat4("transform", mesh.transform);
    }

    public void drawUnsorted(Camera camera)
    {
        if(unsortedMeshes.size() == 0) return;

        use();

        bindUniforms(camera);

        unsortedMeshes.forEach((m) ->
        {
            m.array.bind();
            m.indices.bind();
            bindPMUniforms(camera, m);

            //glDrawElementsInstanced(m.getDrawMode(), m.indices, 1);
            glDrawElementsInstanced(m.getDrawMode(), m.getVertCount() + m.getPrimCount() - 1, GL_UNSIGNED_INT, 0, 1);
            //glDrawElements(m.getDrawMode(), m.getVpr(), GL_UNSIGNED_INT, 0);
            //glDrawArraysInstanced(m.getDrawMode(), 0, m.getVertCount(), 1);
            //glDrawArrays(m.getDrawMode(), 0, m.getVertCount());
            //for(int i = 0, dm = m.getDrawMode(), vpr = m.getVpr(), limit = m.getVertCount()/vpr; i < limit; i++) glDrawArrays(dm, i*vpr, vpr);
        });

        clearUnsorted();
    }

    /*public void drawNextSorted(Camera camera)
    {
        if(sortedBuffer.size() == 0) return;

        Renderable r = sortedBuffer.get(index++);
        int matrixID = r.getMatrixID();

        if(index == 0)
        {
            bindUniforms(camera);
            vertCount = -r.getVertCount();
        }

        if(Window.lastShaderID != programID)
        {
            use();
            sortedArray.bind();
        }

        r.preDraw(this);
        bindPRUniforms(camera, r);
        glDrawArrays(r.getDrawMode(), vertCount += r.getVertCount(), r.getVertCount());

        if(index == sortedBuffer.size() - 1) clearSorted();
    }*/

    public void use()
    {
        if(Window.lastShaderID != programID) glUseProgram(Window.lastShaderID = programID);
    }

    public void setUniformMat3(String name, Matrix3f mat3)
    {
        int location = getUniformLocation(name);
        mat3.get(mat3b);

        if(location > -1) glUniformMatrix3fv(location, false, mat3b);
        else Logger.SHADER_PROGRAM.err("Program " + programID + " attempted to set non-existent uniform '" + name + '\'');
    }

    public void setUniformMat4(String name, Matrix4f mat4)
    {
        int location = getUniformLocation(name);
        mat4.get(mat4b);

        if(location > -1) glUniformMatrix4fv(location, false, mat4b);
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
            t = glGetUniformLocation(programID, name);
            if(t != -1) uniformLocations.put(name, t);
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

            if(i != shaders.length - 1) temp.append(String.format("s%d: %s,", i, s));
            else temp.append(String.format("s%d: %s", i, s));
        }

        return temp.toString();
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        glDeleteProgram(programID);
    }

    /*public void clearSorted()
    {
        sortedBuffer.clear();

        lastMatrix = -1;
        index = 0;
        sortedBufferSize = 0;
    }*/

    public void clearUnsorted()
    {
        lastMatrix = -1;
    }

    private void checkError()
    {
        int e = glGetError();

        while(e != GL_NO_ERROR)
        {
            Logger.SHADER_PROGRAM.err("Vertex Array Error Code: " + e);
            e = glGetError();
        }
    }
}
