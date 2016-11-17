package com.pieisnotpi.engine.rendering.shaders.types.color_shader;

import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class ColorShader extends ShaderProgram
{
    public static final int ID = 42145;

    public ColorShader()
    {
        super(new ShaderFile("/assets/shaders/color.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/color.frag", GL_FRAGMENT_SHADER));
    }
}
