package com.pieisnotpi.engine.rendering.textures;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Window;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture
{
    public static List<Texture> textures = new ArrayList<>();
    public static String defaultPath = "/assets/textures/";

    private int texID = -1, samplerID = 0;
    private String path;

    public boolean hasTransparency = false;
    public int width, height;
    public String name;

    public Texture(Image image)
    {
        this(image, 0);
    }

    public Texture(String path)
    {
        this(path, 0);
    }

    public Texture(Image image, int samplerID)
    {
        this.samplerID = samplerID;
        texID = glGenTextures();
        compileTexture(image);
    }

    public Texture(BufferedImage image)
    {
        this(image, 0);
    }

    public Texture(BufferedImage image, int samplerID)
    {
        this.samplerID = samplerID;
        texID = glGenTextures();
        compileTexture(image);
    }

    public Texture(String path, int samplerID)
    {
        this.samplerID = samplerID;
        this.path = path;
        name = path;

        int begin = Integer.max(name.lastIndexOf('\\') + 1, Integer.max(name.lastIndexOf('/') + 1, 0)), end;

        if((end = name.lastIndexOf('.')) == -1) end = name.length();

        name = name.substring(begin, end);

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

    public void bind()
    {
        glActiveTexture(GL_TEXTURE0 + samplerID);
        glBindTexture(GL_TEXTURE_2D, texID);

        Window.lastTextureID = texID;
    }

    public void compileTexture(BufferedImage image)
    {
        width = image.getWidth();
        height = image.getHeight();

        int[] pixels = new int[width*height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer bytes = ByteBuffer.allocateDirect(width*height*4);

        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                int pixel = pixels[y*width + x];

                bytes.put((byte) ((pixel >> 16) & 0xFF));
                bytes.put((byte) ((pixel >> 8) & 0xFF));
                bytes.put((byte) (pixel & 0xFF));
                bytes.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        bytes.flip();

        bind();
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, bytes);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    }

    public void compileTexture(Image image)
    {
        width = (int) image.getWidth();
        height = (int) image.getHeight();

        ByteBuffer bytes = ByteBuffer.allocateDirect(4 * width * height);

        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                Color pixel = image.getPixelReader().getColor(x, y);
                if(pixel.getOpacity() != 1) hasTransparency = true;

                bytes.put((byte) ((int) (pixel.getRed()*255) & 0xff));
                bytes.put((byte) ((int) (pixel.getGreen()*255) & 0xff));
                bytes.put((byte) ((int) (pixel.getBlue()*255) & 0xff));
                bytes.put((byte) ((int) (pixel.getOpacity()*255) & 0xff));
            }
        }

        bytes.flip();

        bind();
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, bytes);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    }

    public int getTexID()
    {
        return texID;
    }

    public int getSamplerID()
    {
        return samplerID;
    }

    public String toString()
    {
        return path;
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        glDeleteTextures(texID);
    }

    public static Texture getTexture(String name)
    {
        for(Texture texture : textures) if(texture.name.equals(name)) return texture;

        try
        {
            String path = name;

            if(!path.contains("\\") && !path.contains("/")) path = defaultPath + path;
            if(!path.contains(".")) path += ".png";

            Texture temp = new Texture(path);
            Logger.TEXTURES.debug("Found texture '" + path + "'");
            textures.add(temp);
            return temp;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
