package com.pieisnotpi.engine.rendering.textures;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.textures.image.Image;
import com.pieisnotpi.engine.rendering.window.GLInstance;
import org.lwjgl.glfw.GLFWImage;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture
{
    public static String defaultPath = "/assets/textures/", defaultExtension = ".png";
    public static final int FILTER_NEAREST = GL_NEAREST, FILTER_LINEAR = GL_LINEAR;

    public int texFilter, handle = -1;
    public Image image;
    
    public Texture(String path)
    {
        this(path, FILTER_NEAREST);
    }
    
    public Texture(String path, int texFilter)
    {
        this(new Image(path), texFilter);
    }

    public Texture(Image image)
    {
        this(image, FILTER_NEAREST);
    }

    public Texture(Image image, int texFilter)
    {
        this.texFilter = texFilter;
        
        handle = glGenTextures();
        setImage(image);
    }

    public Texture(int texID)
    {
        this(texID, FILTER_NEAREST);
    }

    public Texture(int texID, int texFilter)
    {
        this.handle = texID;
        this.texFilter = texFilter;
    }

    public void bind(int sampler)
    {
        if(PiEngine.glInstance.boundTextures[sampler] == handle) return;

        glActiveTexture(GL_TEXTURE0 + sampler);
        glBindTexture(GL_TEXTURE_2D, handle);

        PiEngine.glInstance.boundTextures[sampler] = handle;
    }

    public void setImage(Image image)
    {
        this.image = image;

        bind(0);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.bytes);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, texFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, texFilter);
        
        image.freeBuffer();
    }

    public int getHandle()
    {
        return handle;
    }

    public static Texture getTextureFile(String name)
    {
        return getTextureFile(name, FILTER_NEAREST);
    }

    public static Texture getTextureFile(String name, int texFilter)
    {
        GLInstance inst = PiEngine.glInstance;

        Texture t = inst.textures.get(name + texFilter);
        if(t != null) return t;

        String path = name.replaceAll("\\\\", "/");

        if(!path.contains("/")) path = defaultPath + path;
        if(!path.contains(".")) path = path.concat(defaultExtension);

        Image image = new Image(path);
        if(image.bytes == null) return null;
        else
        {
            t = new Texture(image, texFilter);
            Logger.OPENGL.debug("Found texture '" + path + '\'');
            inst.textures.put(name + texFilter, t);
            return t;
        }
    }

    public static Texture getTextureInternal(String name)
    {
        return PiEngine.glInstance.textures.get(name);
    }

    public static GLFWImage.Buffer getGLFWImage(Image... images)
    {
        GLFWImage.Buffer buffer = GLFWImage.malloc(images.length);

        for(int i = 0; i < images.length; i++)
        {
            Image image = images[i];
            buffer.position(i).width(image.width).height(image.height).pixels(image.bytes);
        }

        return buffer;
    }
}
