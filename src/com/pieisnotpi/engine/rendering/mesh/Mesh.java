package com.pieisnotpi.engine.rendering.mesh;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.primitives.Primitive;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;
import com.pieisnotpi.engine.rendering.shaders.buffers.IndexBuffer;
import com.pieisnotpi.engine.rendering.shaders.types.ads.ADSMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Mesh<R extends Primitive>
{
    private VertexArray array;
    private IndexBuffer indices;
    private Material material;
    private List<R> primitives;

    private MeshConfig config;
    private int vertCount, primCount, layer;
    private boolean shouldBuild = true;
    private boolean destroyed = false;

    public Mesh(AIMesh mesh, ADSMaterial[] materials)
    {
        int matId = mesh.mMaterialIndex();
        if (matId >= 0 && matId < materials.length) {
            material = materials[matId];
        } else {
            throw new IllegalArgumentException("No material provided. That's bad.");
        }

        config = MeshConfig.TRIANGLE_STATIC;

        array = new VertexArray(material.genAttributes(config.isStatic)).init();
        indices = new IndexBuffer(config.isStatic);

        Attribute vertices = array.attributes[0];
        Attribute normals = array.attributes[1];
        Attribute texCoords = array.attributes[2];

        AIVector3D.Buffer aiVertices = mesh.mVertices();
        int index = 0;
        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            vertices.buffer.put(aiVertex.x());
            vertices.buffer.put(aiVertex.y());
            vertices.buffer.put(aiVertex.z());
            indices.buffer.put(index);
            indices.buffer.put(index + 1);
            indices.buffer.put(index + 2);
            index += 3;
        }

        AIVector3D.Buffer aiNormals = mesh.mNormals();
        while (aiVertices.remaining() > 0) {
            AIVector3D aiNormal = aiNormals.get();
            normals.buffer.put(aiNormal.x());
            normals.buffer.put(aiNormal.y());
            normals.buffer.put(aiNormal.z());
        }

        AIVector3D.Buffer aiTexCoords = mesh.mTextureCoords(0);
        while (aiVertices.remaining() > 0) {
            AIVector3D aiTexCoord = aiTexCoords.get();
            texCoords.buffer.put(aiTexCoord.x());
            texCoords.buffer.put(aiTexCoord.y());
            //texCoords.buffer.put(aiVertex.z());
        }
    }

    public Mesh(Material material, MeshConfig config)
    {
        this.material = material;
        this.config = config;

        primitives = new LinkedList<>();
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
        if(primitives == null || !array.isAlive() || !indices.alive) return this;

        primCount = primitives.size();
        vertCount = config.vpr*primCount;

        for(Attribute attribute : array.attributes) attribute.setBufferSize(attribute.size*vertCount);

        material.putElements(primitives, array);
        if(config.isStatic)
        {
            primitives.forEach(Primitive::nullify);
            primitives = null;
        }

        indices.setBufferSize(vertCount + primCount);

        for(int i = 0; i < vertCount; i++)
        {
            indices.buffer.put(i);
            if(i != 0 && i != vertCount - 1 && (i + 1) % config.vpr == 0) indices.buffer.put(ShaderProgram.primitiveRestart);
        }

        bindData();

        return this;
    }

    public void bindData()
    {
        for(Attribute a : array.attributes)
        {
            a.bindData();
        }
        indices.bindData();
        shouldBuild = false;
    }
    
    public void draw(Transform transform, Camera camera)
    {
        material.shader.draw(transform, this, camera);
    }

    public int getVertCount() { return vertCount; }

    public int getVpr() { return config.vpr; }

    public int getDrawMode() { return config.drawMode; }

    public int getPrimCount() { return indices.size(); }

    public boolean shouldBuild() { return shouldBuild; }

    public boolean isDestroyed() { return destroyed; }

    public List<R> getPrimitives() { return primitives; }

    public Material getMaterial() { return material; }

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
