package com.pieisnotpi.engine.rendering.mesh;

import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;
import com.pieisnotpi.engine.rendering.shaders.buffers.IndexBuffer;
import com.pieisnotpi.engine.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class Mesh<R extends Renderable>
{
    public Scene scene;
    public VertexArray array;
    public IndexBuffer indices;
    public Material material;
    public List<R> renderables;
    protected Transform transform;

    public int lastProgram = -1;
    protected int vertCount, drawMode, vpr, primCount;
    protected boolean shouldSort = false, isStatic, shouldBuild = true;

    public Mesh(Material material, Transform transform, int drawMode, int vertsPerRenderable, boolean isStatic, Scene scene)
    {
        this.material = material;
        this.transform = transform;
        this.scene = scene;
        this.drawMode = drawMode;
        this.vpr = vertsPerRenderable;
        this.isStatic = isStatic;

        renderables = new ArrayList<>();
        array = new VertexArray(material.genAttributes(isStatic)).init();
        indices = new IndexBuffer(isStatic);
    }

    public Mesh<R> setTransform(Transform transform)
    {
        this.transform = transform;
        return this;
    }

    public Mesh<R> setSorting(boolean sorting)
    {
        shouldSort = sorting;
        return this;
    }

    public Mesh<R> setRenderables(List<R> renderables)
    {
        this.renderables = renderables;
        return this;
    }

    public Mesh<R> addRenderable(R renderable)
    {
        if(renderables == null) renderables = new ArrayList<>();
        renderables.add(renderable);
        shouldBuild = true;
        return this;
    }

    public Mesh<R> removeRenderable(R renderable)
    {
        if(renderables != null)
        {
            renderables.remove(renderable);
            shouldBuild = true;
        }
        return this;
    }

    public Mesh<R> flagForBuild()
    {
        shouldBuild = true;
        return this;
    }

    public Mesh<R> bind()
    {
        array.bind();
        indices.bind();

        return this;
    }

    public Mesh<R> build()
    {
        if(renderables.size() == 0 && array.alive && indices.alive) return this;

        primCount = renderables.size();
        vertCount = vpr*primCount;

        for(Attribute attribute : array.attributes) attribute.setBufferSize(attribute.size*vertCount);

        material.putElements(renderables, array);
        if(isStatic)
        {
            renderables.forEach(Renderable::nullify);
            renderables = null;
        }

        for(Attribute a : array.attributes)
        {
            a.buffer.flip();
            a.bindData();
        }

        indices.setBufferSize(vertCount + primCount);

        for(int i = 0; i < vertCount; i++)
        {
            indices.buffer.put(i);
            if(i != 0 && i != vertCount - 1 && (i + 1) % vpr == 0) indices.buffer.put(ShaderProgram.primitiveRestart);
        }

        indices.buffer.flip();
        indices.bindData(indices.buffer);

        shouldBuild = false;

        return this;
    }

    public Mesh<R> register()
    {
        scene.addMesh(this);
        return this;
    }

    public Mesh<R> unregister()
    {
        scene.removeMesh(this);
        return this;
    }

    public Transform getTransform() { return transform; }

    public int getVertCount() { return vertCount; }

    public int getVpr() { return vpr; }

    public int getDrawMode() { return drawMode; }

    public int getPrimCount() { return primCount; }

    public boolean shouldBuild() { return shouldBuild; }

    public boolean shouldSort() { return shouldSort; }

    public void destroy()
    {
        array.destroy();
        indices.destroy();
        transform.removeFromParent();

        renderables = null;
        array = null;
        indices = null;
        transform = null;

        unregister();
    }
}
