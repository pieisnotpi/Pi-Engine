package com.pieisnotpi.engine.rendering.shaders.types.tex_shader;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.window.Window;

public class TexShader extends ShaderProgram
{
    public static final int ID = 11246;

    public TexShader(Window window)
    {
        super(window, ShaderFile.getShaderFile("textured.vert", ShaderFile.TYPE_VERT), ShaderFile.getShaderFile("textured.frag", ShaderFile.TYPE_FRAG));
    }

    @Override
    public void bindPMUniforms(Camera camera, Mesh mesh)
    {
        super.bindPMUniforms(camera, mesh);
        TexMaterial m = (TexMaterial) mesh.material;
        for(int i = 0; i < m.textures.length; i++) m.textures[i].bind(i);
    }
}
