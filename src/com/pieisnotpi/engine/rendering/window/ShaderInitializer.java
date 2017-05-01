package com.pieisnotpi.engine.rendering.window;

import com.pieisnotpi.engine.rendering.shaders.types.ads_shader.ADSShader;
import com.pieisnotpi.engine.rendering.shaders.types.color_shader.ColorShader;
import com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader.TexCShader;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexShader;
import com.pieisnotpi.engine.rendering.shaders.types.text_shader.TextShader;

public interface ShaderInitializer
{
    default void init(GLInstance inst)
    {
        inst.registerShader(ADSShader.ID, new ADSShader(inst.window).init());
        inst.registerShader(ColorShader.ID, new ColorShader(inst.window).init());
        inst.registerShader(TextShader.ID, new TextShader(inst.window).init());
        inst.registerShader(TexCShader.ID, new TexCShader(inst.window).init());
        inst.registerShader(TexShader.ID, new TexShader(inst.window).init());
    }
}
