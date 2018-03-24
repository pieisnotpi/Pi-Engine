package com.pieisnotpi.engine.rendering.window;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.ui.text.font.Font;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class GLInstance
{
    public Map<Integer, ShaderProgram> shaders = new HashMap<>();
    public Map<String, Texture> textures = new HashMap<>();
    public Map<String, Font> fonts = new HashMap<>();
    public GLCapabilities capabilities;
    public Window window;
    public int[] boundTextures = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    public int lastShaderID = -1;

    public GLInstance(Window window)
    {
        this.window = window;
        glfwMakeContextCurrent(window.handle);
        capabilities = GL.createCapabilities();

        bind();
    }

    public GLInstance initShaders(ShaderInitializer initializer)
    {
        initializer.init(this);
        return this;
    }

    public void bind()
    {
        if(equals(PiEngine.glInstance)) return;

        PiEngine.glInstance = this;

        glfwMakeContextCurrent(window.handle);
        GL.setCapabilities(capabilities);
    }
}
