package com.pieisnotpi.engine.rendering.shaders.types.text_shader;

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
}
