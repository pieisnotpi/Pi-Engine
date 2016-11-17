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
        inst.shaders.put(ADSShader.ID, new ADSShader().init());
        inst.shaders.put(ColorShader.ID, new ColorShader().init());
        inst.shaders.put(TextShader.ID, new TextShader().init());
        inst.shaders.put(TexCShader.ID, new TexCShader().init());
        inst.shaders.put(TexShader.ID, new TexShader().init());
    }
}
