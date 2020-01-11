package com.pieisnotpi.engine.rendering.shaders.types.ads;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.primitives.Primitive;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.utility.BufferUtility;
import org.joml.Vector4f;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.Assimp;

import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class ADSMaterial extends Material
{
    public Texture[] textures;
    public Vector4f ka, kd, ks;
    public float s;

    public ADSMaterial(AIMaterial material)
    {
        super(ADSShader.ID, Camera.PERSP);
        AIColor4D color = AIColor4D.create();

        AIString path = AIString.calloc();
        Assimp.aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
        String textPath = path.dataString();
        textures = new Texture[1];
        // TODO remove
        textures[0] = Texture.getTextureFile("grass.png");
        /*if (textPath != null && textPath.length() > 0)
        {
            System.out.println(textPath);
            textures[0] = Texture.getTextureFile(textPath);
        }*/

        //ka = new Vector4f(0.25f);
        ka = new Vector4f(1f);
        int result = aiGetMaterialColor(material, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, color);
        if (result == 0)
        {
            ka = new Vector4f(color.r(), color.g(), color.b(), color.a());
        }

        //kd = new Vector4f(0.2f);
        kd = new Vector4f(1f);
        result = aiGetMaterialColor(material, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color);
        if (result == 0)
        {
            kd = new Vector4f(color.r(), color.g(), color.b(), color.a());
        }

        //ks = new Vector4f(0.5f);
        ks = new Vector4f(1f);
        result = aiGetMaterialColor(material, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, color);
        if (result == 0)
        {
            ks = new Vector4f(color.r(), color.g(), color.b(), color.a());
        }
    }

    public ADSMaterial(Vector4f ambient, Vector4f diffuse, Vector4f specular, float shininess, int matrixID, Texture... textures)
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
        shader.setUniformVec4("m.Ka", ka);
        shader.setUniformVec4("m.Kd", kd);
        shader.setUniformVec4("m.Ks", ks);
        shader.setUniformFloat("m.Shininess", s);

        for(int i = 0; i < textures.length; i++) textures[i].bind(i);
    }
}
