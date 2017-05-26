package com.pieisnotpi.engine.rendering.textures;

import com.pieisnotpi.engine.image.Image;
import com.pieisnotpi.engine.output.Logger;
import org.joml.Vector2i;

import static com.pieisnotpi.engine.PiEngine.glInstance;
import static org.lwjgl.opengl.GL11.*;

public class Texture
{
    public static String defaultPath = "/assets/textures/", defaultExtension = ".png";
    public static final int FILTER_NEAREST = GL_NEAREST, FILTER_LINEAR = GL_LINEAR;

    public Vector2i res;
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
        glInstance.bindTexture(sampler, GL_TEXTURE_2D, handle);
    }

    public void setImage(Image image)
    {
        this.image = image;

        bind(0);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.bytes);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, texFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, texFilter);
        res = new Vector2i(image.width, image.height);
        
        image.freeBuffer();
    }
    
    public float[] getGLImage()
    {
        float[] temp = new float[res.x*res.y*4];
        bind(3);
        glReadPixels(0, 0, res.x, res.y, GL_RGBA, GL_UNSIGNED_BYTE, temp);
        return temp;
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
        Texture t = glInstance.getTexture(name + texFilter);
        if(t != null) return t;

        String path = name.replaceAll("\\\\", "/");

        if(path.charAt(0) != '\n') path = defaultPath + path;
        else path = path.substring(1);
        if(!path.contains(".")) path = path.concat(defaultExtension);

        Image image = new Image(path);
        if(image.bytes == null) return null;
        else
        {
            t = new Texture(image, texFilter);
            Logger.OPENGL.debug("Found texture '" + path + '\'');
            glInstance.registerTexture(name + texFilter, t);
            return t;
        }
    }

    public static Texture getTextureInternal(String name)
    {
        return glInstance.getTexture(name);
    }
}
