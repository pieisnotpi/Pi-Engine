package com.pieisnotpi.engine.rendering.window;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.shaders.types.ads_shader.ADSShader;
import com.pieisnotpi.engine.rendering.shaders.types.color_shader.ColorShader;
import com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader.TexturedCShader;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TextureShader;
import com.pieisnotpi.engine.rendering.shaders.types.text_shader.TextShader;

public interface ShaderInitializer
{
    default void init(GLInstance inst)
    {
        inst.shaders.put(PiEngine.S_ADS_ID, new ADSShader().init());
        inst.shaders.put(PiEngine.S_COLOR_ID, new ColorShader().init());
        inst.shaders.put(PiEngine.S_TEXT_ID, new TextShader().init());
        inst.shaders.put(PiEngine.S_TEXTURE_C_ID, new TexturedCShader().init());
        inst.shaders.put(PiEngine.S_TEXTURE_ID, new TextureShader().init());
    }
}
