package com.pieisnotpi.engine.rendering.mesh;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.primitives.Primitive;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;
import com.pieisnotpi.engine.rendering.shaders.buffers.IndexBuffer;

import java.util.ArrayList;
import java.util.List;

public class Mesh<R extends Primitive>
{
    public VertexArray array;
    public IndexBuffer indices;
    public Material material;
    public List<R> primitives;
    
    protected MeshConfig config;
    protected int vertCount, primCount, layer;
    protected boolean shouldBuild = true;
    
    private boolean destroyed = false;

    public Mesh(Material material, MeshConfig config)
    {
        this.material = material;
        this.config = config;

        primitives = new ArrayList<>();
        array = new VertexArray(material.genAttributes(config.isStatic)).init();
        indices = new IndexBuffer(config.isStatic);
    }

    public Mesh<R> setPrimitives(List<R> primitives)
    {
        this.primitives = primitives;
        return this;
    }

    public Mesh<R> addPrimitive(R primitive)
    {
        if(primitives == null) primitives = new ArrayList<>();
        primitives.add(primitive);
        shouldBuild = true;
        return this;
    }

    public Mesh<R> removePrimitive(R primitive)
    {
        if(primitives != null)
        {
            primitives.remove(primitive);
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
        if(primitives == null || primitives.size() == 0 || !array.alive || !indices.alive) return this;

        primCount = primitives.size();
        vertCount = config.vpr*primCount;

        for(Attribute attribute : array.attributes) attribute.setBufferSize(attribute.size*vertCount);

        material.putElements(primitives, array);
        if(config.isStatic)
        {
            primitives.forEach(Primitive::nullify);
            primitives = null;
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
    
    public void draw(Transform transform, Camera camera)
    {
        material.shader.draw(transform, this, camera);
    }

    public int getVertCount() { return vertCount; }

    public int getVpr() { return config.vpr; }

    public int getDrawMode() { return config.drawMode; }

    public int getPrimCount() { return primCount; }

    public boolean shouldBuild() { return shouldBuild; }

    public boolean isDestroyed() { return destroyed; }

    public void destroy()
    {
        array.destroy();
        indices.destroy();

        primitives = null;
        array = null;
        indices = null;
        
        destroyed = true;
    }
}
