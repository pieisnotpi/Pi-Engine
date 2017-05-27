package com.pieisnotpi.engine.rendering.mesh;

import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;
import com.pieisnotpi.engine.rendering.shaders.buffers.IndexBuffer;

import java.util.ArrayList;
import java.util.List;

public class Mesh<R extends Renderable>
{
    public Mesh original;
    public VertexArray array;
    public IndexBuffer indices;
    public Material material;
    public List<R> renderables;
    
    protected Transform transform;
    protected List<Mesh> copies;
    protected MeshConfig config;
    protected int vertCount, primCount;
    protected boolean shouldSort = false, shouldBuild = true;
    
    private boolean destroyed = false;

    public Mesh(Material material, Transform transform, MeshConfig config)
    {
        this.material = material;
        this.transform = transform;
        this.config = config;

        renderables = new ArrayList<>();
        array = new VertexArray(material.genAttributes(config.isStatic)).init();
        indices = new IndexBuffer(config.isStatic);
    }

    public Mesh(Mesh original, Transform transform)
    {
        this.original = original;
        this.transform = transform;

        original.registerCopy(this);

        material = original.material;
        array = original.array;
        indices = original.indices;
        vertCount = original.vertCount;
        config = original.config;
        primCount = original.primCount;
        shouldSort = original.shouldSort;
        shouldBuild = original.shouldBuild;
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
        material.bind();

        return this;
    }

    public Mesh<R> build()
    {
        if(original != null)
        {
            original.build();

            vertCount = original.vertCount;
            primCount = original.primCount;
            shouldSort = original.shouldSort;

            return this;
        }

        if(renderables == null || renderables.size() == 0 || !array.alive || !indices.alive) return this;

        primCount = renderables.size();
        vertCount = config.vpr*primCount;

        for(Attribute attribute : array.attributes) attribute.setBufferSize(attribute.size*vertCount);

        material.putElements(renderables, array);
        if(config.isStatic)
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
            if(i != 0 && i != vertCount - 1 && (i + 1) % config.vpr == 0) indices.buffer.put(ShaderProgram.primitiveRestart);
        }

        indices.buffer.flip();
        indices.bindData(indices.buffer);

        shouldBuild = false;

        return this;
    }
    
    public void draw(Camera camera)
    {
        material.shader.draw(this, camera);
    }

    protected void registerCopy(Mesh copy)
    {
        if(copies == null) copies = new ArrayList<>();
        copies.add(copy);
    }

    protected void unregisterCopy(Mesh copy)
    {
        if(copies != null) copies.remove(copy);
    }

    public Transform getTransform() { return transform; }

    public int getVertCount() { return vertCount; }

    public int getVpr() { return config.vpr; }

    public int getDrawMode() { return config.drawMode; }

    public int getPrimCount() { return primCount; }

    public boolean shouldBuild() { return shouldBuild; }

    public boolean shouldSort() { return shouldSort; }
    
    public boolean isDestroyed() { return destroyed; }

    public void destroy()
    {
        if(original == null)
        {
            array.destroy();
            indices.destroy();
        }
        else original.unregisterCopy(this);

        if(copies != null) while(copies.size() > 0) copies.get(0).destroy();

        transform.removeFromParent();

        renderables = null;
        array = null;
        indices = null;
        transform = null;
    }
}
