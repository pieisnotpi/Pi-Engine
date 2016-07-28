package com.pieisnotpi.engine.rendering.textures;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Window;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture
{
    public static Map<String, Texture> textures = new HashMap<>();
    public static String defaultPath = "/assets/textures/";

    private int texID = -1;
    public int width, height;

    public Texture(Image image)
    {
        texID = glGenTextures();
        compileTexture(image);
    }

    public Texture(BufferedImage image)
    {
        texID = glGenTextures();
        compileTexture(image);
    }

    public Texture(int texID, int width, int height)
    {
        this.texID = texID;
        this.width = width;
        this.height = height;
    }

    public Texture(String path)
    {
        try
        {
            InputStream file = Texture.class.getResourceAsStream(path);

            if(file == null)
            {
                Logger.TEXTURES.err("Texture at path " + path + " failed to load");
                return;
            }

            Image image = new Image(file);

            texID = glGenTextures();
            compileTexture(image);

            file.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void bind(int sampler)
    {
        glActiveTexture(GL_TEXTURE0 + sampler);
        glBindTexture(GL_TEXTURE_2D, texID);

        Window.boundTextures[sampler] = texID;
    }

    public void compileTexture(BufferedImage image)
    {
        width = image.getWidth();
        height = image.getHeight();

        bind(0);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, getBytes(image));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    }

    public void compileTexture(Image image)
    {
        width = (int) image.getWidth();
        height = (int) image.getHeight();

        bind(0);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, getBytes(image));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
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

    public static Texture getTexture(String name)
    {
        Texture t = textures.get(name);
        if(t != null) return t;

        try
        {
            String path = name;

            if(!path.contains("\\") && !path.contains("/")) path = defaultPath + path;
            if(!path.contains(".")) path += ".png";

            Texture temp = new Texture(path);
            Logger.TEXTURES.debug("Found texture '" + path + "'");
            textures.put(name, temp);
            return temp;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
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
