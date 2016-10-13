package com.pieisnotpi.engine.rendering.shaders.types.ads_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Renderable;
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
        super(PiEngine.S_ADS_ID, matrixID);
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

        return new Attribute[]{new Attribute("VertexPosition", 3, 0, mode, isStatic), new Attribute("VertexNormal", 3, 1, mode, isStatic), new Attribute("VertexTexCoord", 2, 2, mode, isStatic)};
    }

    public void putElements(List<? extends Renderable> renderables, VertexArray a)
    {
        renderables.forEach(r ->
        {
            BufferUtility.putVec3s(a.attributes[0].buffer, r.points);
            BufferUtility.putVec3s(a.attributes[1].buffer, r.normals);
            BufferUtility.putVec2s(a.attributes[2].buffer, r.texCoords);
        });
    }
}
