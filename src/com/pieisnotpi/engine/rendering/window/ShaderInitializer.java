package com.pieisnotpi.engine.rendering.window;

import com.pieisnotpi.engine.rendering.shaders.types.ads.ADSShader;
import com.pieisnotpi.engine.rendering.shaders.types.color.ColorShader;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexShader;
import com.pieisnotpi.engine.rendering.shaders.types.tex_c.TexCShader;
import com.pieisnotpi.engine.rendering.shaders.types.text.TextShader;

public interface ShaderInitializer
{
    default void init(GLInstance inst)
    {
        inst.registerShaderProgram(ADSShader.ID, new ADSShader(inst.window).init());
        inst.registerShaderProgram(ColorShader.ID, new ColorShader(inst.window).init());
        inst.registerShaderProgram(TextShader.ID, new TextShader(inst.window).init());
        inst.registerShaderProgram(TexCShader.ID, new TexCShader(inst.window).init());
        inst.registerShaderProgram(TexShader.ID, new TexShader(inst.window).init());
    }
}
