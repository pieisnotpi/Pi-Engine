package com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TexturedCShader extends ShaderProgram
{
    public TexturedCShader()
    {
        super(new ShaderFile("/assets/shaders/textured_c.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/textured_c.frag", GL_FRAGMENT_SHADER));
    }

    @Override
    public void bindPMUniforms(Camera camera, Mesh mesh)
    {
        super.bindPMUniforms(camera, mesh);
        TexCMaterial m = (TexCMaterial) mesh.material;

        for(int i = 0; i < m.textures.length; i++) if(PiEngine.glInstance.boundTextures[i] != m.textures[i].getTexID()) m.textures[i].bind(i);
    }
}
