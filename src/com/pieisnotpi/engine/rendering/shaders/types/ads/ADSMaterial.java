package com.pieisnotpi.engine.rendering.shaders.types.ads;

import com.pieisnotpi.engine.rendering.primitives.Primitive;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.utility.BufferUtility;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class ADSMaterial extends Material
{
    public Texture[] textures;
    public Vector3f ka, kd, ks;
    public float s;

    public ADSMaterial(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess, int matrixID, Texture... textures)
    {
        super(ADSShader.ID, matrixID);
        this.textures = textures;

        ka = ambient;
        kd = diffuse;
        ks = specular;
        s = shininess;
    }

    @Override
    public Attribute[] genAttributes(boolean isStatic)
    {
        int mode = GL_DYNAMIC_DRAW;
        if(isStatic) mode = GL_STATIC_DRAW;

        return new Attribute[]{new Attribute("VertexPosition", shader, 3, mode, isStatic), new Attribute("VertexNormal", shader, 3, mode, isStatic), new Attribute("VertexTexCoord", shader, 2, mode, isStatic)};
    }

    public void putElements(List<? extends Primitive> renderables, VertexArray a)
    {
        renderables.forEach(r ->
        {
            if(!r.enabled) return;
            BufferUtility.putVec3s(a.attributes[0].buffer, r.points);
            BufferUtility.putVec3s(a.attributes[1].buffer, r.normals);
            BufferUtility.putVec2s(a.attributes[2].buffer, r.texCoords);
        });
    }

    @Override
    public void bind()
    {
        shader.setUniformVec3("m.Ka", ka);
        shader.setUniformVec3("m.Kd", kd);
        shader.setUniformVec3("m.Ks", ks);
        shader.setUniformFloat("m.Shininess", s);

        for(int i = 0; i < textures.length; i++) textures[i].bind(i);
    }
}
