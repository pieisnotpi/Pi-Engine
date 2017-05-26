package com.pieisnotpi.engine.rendering.window;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.ui.text.font.Font;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.GL_MAX_TEXTURE_IMAGE_UNITS;

public class GLInstance
{
    private Map<Integer, ShaderProgram> shaderPrograms = new HashMap<>();
    private Map<String, Texture> textures = new HashMap<>();
    private Map<String, Font> fonts = new HashMap<>();
    private Map<String, ShaderFile> shaderFiles = new HashMap<>();
    public GLCapabilities capabilities;
    public Window window;
    public int lastShaderID = -1;
    private int[] boundTextures;

    public GLInstance(Window window)
    {
        this.window = window;
        glfwMakeContextCurrent(window.handle);
        capabilities = GL.createCapabilities();
        boundTextures = new int[glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS)];

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
    
    public void bindTexture(int sampler, int target, int textureID)
    {
        if(boundTextures[sampler] == textureID) return;
        
        boundTextures[sampler] = textureID;
        glActiveTexture(GL_TEXTURE0 + sampler);
        glBindTexture(target, textureID);
    }
    
    public Map<Integer, ShaderProgram> getShaderPrograms()
    {
        return shaderPrograms;
    }
    
    public ShaderProgram getShaderProgram(int ID)
    {
        return shaderPrograms.get(ID);
    }
    
    public void registerShaderProgram(int ID, ShaderProgram shader)
    {
        if(shader == null) { Logger.OPENGL.debugErr("Attempted to register null shader program"); Logger.OPENGL.debugStacktrace(); }
        else shaderPrograms.put(ID, shader);
    }
    
    public Map<String, Texture> getTextures()
    {
        return textures;
    }
    
    public Texture getTexture(String name)
    {
        return textures.get(name);
    }
    
    public void registerTexture(String name, Texture texture)
    {
        if(texture == null) { Logger.OPENGL.debugErr("Attempted to register null texture"); Logger.OPENGL.debugStacktrace(); }
        else textures.put(name, texture);
    }
    
    public Map<String, Font> getFonts()
    {
        return fonts;
    }
    
    public Font getFont(String name)
    {
        return fonts.get(name);
    }
    
    public void registerFont(String name, Font font)
    {
        if(font == null) { Logger.OPENGL.debugErr("Attempted to register null font"); Logger.OPENGL.debugStacktrace(); }
        else fonts.put(name, font);
    }

    public Map<String, ShaderFile> getShaderFiles()
    {
        return shaderFiles;
    }

    public ShaderFile getShaderFile(String name)
    {
        return shaderFiles.get(name);
    }

    public void registerShaderFile(String name, ShaderFile file)
    {
        if(file == null) { Logger.OPENGL.debugErr("Attempted to register null shader file"); Logger.OPENGL.debugStacktrace(); }
        else shaderFiles.put(name, file);
    }
}
