package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.rendering.shaders.*;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Mesh
{
    public Scene scene;
    public VertexArray array;
    public IndexBuffer indices;
    public Material material;
    public List<Renderable> renderables;
    public Matrix4f transform = new Matrix4f();
    public Vector3f pos = new Vector3f(0), scale = new Vector3f(1), rot = new Vector3f(0);
    public boolean shouldSort = false, isStatic;
    public int lastProgram = -1;
    protected int vertCount, drawMode, vpr, primCount;

    public Mesh(Material material, int drawMode, int vertsPerRenderable, boolean isStatic, Scene scene)
    {
        this.material = material;
        this.scene = scene;
        this.drawMode = drawMode;
        this.vpr = vertsPerRenderable;
        this.isStatic = isStatic;
        array = new VertexArray(material.genAttributes());
    }

    public Mesh scale(float x, float y, float z)
    {
        transform.scale(x, y, z);
        scale.add(x, y, z);
        return this;
    }

    public Mesh setScale(float x, float y, float z)
    {
        return scale(x, y, z);
    }

    public Mesh translate(float x, float y, float z)
    {
        transform.translate(x, y, z);
        pos.add(x, y, z);
        return this;
    }

    public Mesh setTranslate(float x, float y, float z)
    {
        return translate(x - pos.x, y - pos.y, z - pos.z);
    }

    public Mesh rotateDegrees(float x, float y, float z)
    {
        transform.rotateXYZ((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
        rot.add(x, y, z);
        return this;
    }

    public Mesh setRotateDegrees(float x, float y, float z)
    {
        return rotateDegrees(x - rot.x, y - rot.y, z - rot.z);
    }

    public Mesh rotateRadians(float x, float y, float z)
    {
        transform.rotateXYZ(x, y, z);
        rot.add((float) Math.toDegrees(x), (float) Math.toDegrees(y), (float) Math.toDegrees(z));
        return this;
    }

    public Mesh setRotateRadians(float x, float y, float z)
    {
        float xr = (float) Math.toRadians(rot.x), yr = (float) Math.toRadians(rot.y), zr = (float) Math.toRadians(rot.z);
        return rotateRadians(x - xr, y - yr, z - zr);
    }

    public Mesh setTransform(Matrix4f mat4)
    {
        transform = mat4;
        return this;
    }

    public Mesh setSorting(boolean sorting)
    {
        shouldSort = sorting;
        return this;
    }

    public Mesh setRenderables(List<Renderable> renderables)
    {
        this.renderables = renderables;
        return this;
    }

    public Mesh addRenderable(Renderable renderable)
    {
        if(renderables == null) renderables = new ArrayList<>();
        renderables.add(renderable);
        return this;
    }

    public Mesh build()
    {
        if(renderables.size() == 0) return this;

        primCount = renderables.size();
        vertCount = vpr*primCount;

        if(!array.initialized) array.init();

        for(Attribute attribute : array.attributes)
        {
            int size = attribute.size*vertCount;

            if(attribute.buffer.limit() == size) attribute.buffer.position(0);
            else if(attribute.buffer.limit() > size) attribute.buffer.position(0).limit(size);
            else attribute.buffer = BufferUtils.createFloatBuffer(size);
        }

        material.putElements(renderables, array);
        if(isStatic) renderables = null;
        else renderables.clear();

        for(Attribute a : array.attributes)
        {
            a.buffer.flip();
            a.bindData();
            if(isStatic) a.buffer = null;
            else a.buffer.clear();
        }

        IntBuffer ind = BufferUtils.createIntBuffer(vertCount + primCount);
        for(int i = 0; i < vertCount; i++)
        {
            ind.put(i);
            if(i != 0 && i != vertCount - 1 && (i + 1) % vpr == 0) ind.put(ShaderProgram.primitiveRestart);
        }
        ind.flip();

        indices = new IndexBuffer();
        indices.init().bindData(ind);

        return this;
    }

    public Mesh register()
    {
        scene.addMesh(this);
        return this;
    }

    public Mesh unregister()
    {
        scene.removeMesh(this);
        return this;
    }

    public int getVertCount()
    {
        return vertCount;
    }

    public int getVpr()
    {
        return vpr;
    }

    public int getDrawMode()
    {
        return drawMode;
    }

    public int getPrimCount()
    {
        return primCount;
    }

    public void destroy()
    {
        if(array != null) array.destroy();
    }
}
