package com.pieisnotpi.engine.rendering.shaders.types.tex_shader;

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
}
