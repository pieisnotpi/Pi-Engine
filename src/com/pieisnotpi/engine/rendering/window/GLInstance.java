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
    public int[] boundTextures = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    public long windowHandle;
    public int lastShaderID = -1;

    public GLInstance(Window window)
    {
        windowHandle = window.handle;
        glfwMakeContextCurrent(windowHandle);
        capabilities = GL.createCapabilities();

        bind();
    }

    public GLInstance(long windowHandle)
    {
        this.windowHandle = windowHandle;
        glfwMakeContextCurrent(windowHandle);
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

        glfwMakeContextCurrent(windowHandle);
        GL.setCapabilities(capabilities);
    }
}
