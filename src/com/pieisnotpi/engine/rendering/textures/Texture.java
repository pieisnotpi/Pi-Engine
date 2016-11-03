package com.pieisnotpi.engine.rendering.textures;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.window.GLInstance;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture
{
    public static String defaultPath = "/assets/textures/", defaultExtension = ".png";
    public static final int FILTER_NEAREST = GL_NEAREST, FILTER_LINEAR = GL_LINEAR;

    private int texID = -1;
    public int width, height;
    public int texFilter;

    public Texture(Image image)
    {
        this(image, FILTER_NEAREST);
    }

    public Texture(Image image, int texFilter)
    {
        texID = glGenTextures();
        compileTexture(image);
        this.texFilter = texFilter;
    }

    public Texture(BufferedImage image)
    {
        this(image, FILTER_NEAREST);
    }

    public Texture(BufferedImage image, int texFilter)
    {
        texID = glGenTextures();
        compileTexture(image);
        this.texFilter = texFilter;
    }

    public Texture(int texID, int width, int height)
    {
        this(texID, width, height, FILTER_NEAREST);
    }

    public Texture(int texID, int width, int height, int texFilter)
    {
        this.texID = texID;
        this.width = width;
        this.height = height;
        this.texFilter = texFilter;
    }

    public Texture(String path)
    {
        this(path, FILTER_NEAREST);
    }

    public Texture(String path, int texFilter)
    {
        this.texFilter = texFilter;

        try
        {
            InputStream file = Texture.class.getResourceAsStream(path);

            if(file == null)
            {
                Logger.TEXTURES.err("Texture at path " + path + " failed to load");
                return;
            }

            Image image = new Image(file);
            file.close();

            texID = glGenTextures();
            compileTexture(image);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void bind(int sampler)
    {
        if(PiEngine.glInstance.boundTextures[sampler] == texID) return;

        glActiveTexture(GL_TEXTURE0 + sampler);
        glBindTexture(GL_TEXTURE_2D, texID);

        PiEngine.glInstance.boundTextures[sampler] = texID;
    }

    public void compileTexture(BufferedImage image)
    {
        width = image.getWidth();
        height = image.getHeight();

        bind(0);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, getBytes(image));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, texFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, texFilter);
    }

    public void compileTexture(Image image)
    {
        width = (int) image.getWidth();
        height = (int) image.getHeight();

        bind(0);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, getBytes(image));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, texFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, texFilter);
    }

    public int getTexID()
    {
        return texID;
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        glDeleteTextures(texID);
    }

    public static Texture getTextureFile(String name)
    {
        return getTextureFile(name, FILTER_NEAREST);
    }

    public static Texture getTextureFile(String name, int texFilter)
    {
        GLInstance inst = PiEngine.glInstance;

        Texture t = inst.textures.get(name);
        if(t != null) return t;

        try
        {
            String path = name.replaceAll("\\\\", "/");

            if(!path.contains("/")) path = defaultPath + path;
            if(!path.contains(".")) path = path.concat(defaultExtension);

            Texture temp = new Texture(path, texFilter);

            Logger.TEXTURES.debug("Found texture '" + path + "' in instance " + inst.windowHandle);
            inst.textures.put(name, temp);
            return temp;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Texture getTextureInternal(String name)
    {
        return PiEngine.glInstance.textures.get(name);
    }

    public static ByteBuffer getBytes(Image image)
    {
        int w = (int) image.getWidth(), h = (int) image.getHeight();

        ByteBuffer bytes = ByteBuffer.allocateDirect((int) (4*image.getWidth()*image.getHeight()));

        for(int y = 0; y < h; y++)
        {
            for(int x = 0; x < w; x++)
            {
                Color pixel = image.getPixelReader().getColor(x, y);

                bytes.put((byte) ((int) (pixel.getRed()*255) & 0xff));
                bytes.put((byte) ((int) (pixel.getGreen()*255) & 0xff));
                bytes.put((byte) ((int) (pixel.getBlue()*255) & 0xff));
                bytes.put((byte) ((int) (pixel.getOpacity()*255) & 0xff));
            }
        }

        bytes.flip();

        return bytes;
    }

    public static ByteBuffer getBytes(BufferedImage image)
    {
        int w = image.getWidth(), h = image.getHeight();

        int[] pixels = new int[w*h];
        image.getRGB(0, 0, w, h, pixels, 0, w);

        ByteBuffer bytes = ByteBuffer.allocateDirect(w*h*4);

        for(int y = 0; y < h; y++)
        {
            for(int x = 0; x < w; x++)
            {
                int pixel = pixels[y*w + x];

                bytes.put((byte) ((pixel >> 16) & 0xFF));
                bytes.put((byte) ((pixel >> 8) & 0xFF));
                bytes.put((byte) (pixel & 0xFF));
                bytes.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        bytes.flip();

        return bytes;
    }
}
