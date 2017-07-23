package com.pieisnotpi.engine.rendering.shaders.types.tex_c;

import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.window.Window;

public class TexCShader extends ShaderProgram
{
    public static final int ID = 52643;

    public TexCShader(Window window)
    {
        super(window, ShaderFile.getShaderFile("textured_c.vert", ShaderFile.TYPE_VERT), ShaderFile.getShaderFile("textured_c.frag", ShaderFile.TYPE_FRAG));
    }
}
