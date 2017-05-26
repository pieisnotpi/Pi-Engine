package com.pieisnotpi.engine.rendering.shaders.types.text_shader;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.window.Window;

public class TextShader extends ShaderProgram
{
    public static final int ID = 73412;

    public TextShader(Window window)
    {
        super(window, ShaderFile.getShaderFile("text.vert", ShaderFile.TYPE_VERT), ShaderFile.getShaderFile("text.frag", ShaderFile.TYPE_FRAG));
    }

    public void bindPMUniforms(Camera camera, Mesh mesh)
    {
        super.bindPMUniforms(camera, mesh);
        TextMaterial m = (TextMaterial) mesh.material;
        setUniformInt("size", m.outlineSize);
        for(int i = 0; i < m.textures.length; i++) m.textures[i].bind(i);
    }
}
