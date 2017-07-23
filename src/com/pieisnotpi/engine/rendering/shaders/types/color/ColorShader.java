package com.pieisnotpi.engine.rendering.shaders.types.color;

import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.window.Window;

public class ColorShader extends ShaderProgram
{
    public static final int ID = 42145;

    public ColorShader(Window window)
    {
        super(window, ShaderFile.getShaderFile("color.vert", ShaderFile.TYPE_VERT), ShaderFile.getShaderFile("color.frag", ShaderFile.TYPE_FRAG));
    }
}
