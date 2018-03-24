package com.pieisnotpi.engine.rendering.textures.image;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.engine.utility.FileLoader;

import java.io.InputStream;
import java.nio.ByteBuffer;

import static com.pieisnotpi.engine.utility.Color.valueToByte;

public class WritableImage extends Image
{
    public byte[][][] pixels;
    
    public WritableImage(String path)
    {
        InputStream s = FileLoader.findStream(path, getClass());
    
        if(s == null) return;
    
        javafx.scene.image.Image image = new javafx.scene.image.Image(s);
    
        width = (int) image.getWidth();
        height = (int) image.getHeight();
        
        pixels = new byte[width][height][4];
    
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                javafx.scene.paint.Color pixel = image.getPixelReader().getColor(x, y);
            
                pixels[x][y][0] = valueToByte((float) pixel.getRed());
                pixels[x][y][1] = valueToByte((float) pixel.getGreen());
                pixels[x][y][2] = valueToByte((float) pixel.getBlue());
                pixels[x][y][3] = valueToByte((float) pixel.getOpacity());
            }
        }
        
        bytes = getBytes(pixels, width, height);
    }
    
    public void setPixel(int x, int y, Color color)
    {
        setPixel(x, y, color.red, color.green, color.blue, color.alpha);
    }
    
    public void setPixel(int x, int y, float r, float g, float b, float a)
    {
        pixels[x][y][0] = valueToByte(r);
        pixels[x][y][1] = valueToByte(g);
        pixels[x][y][2] = valueToByte(b);
        pixels[x][y][3] = valueToByte(a);
    }
    
    public void write()
    {
        if(pixels == null)
        {
            Logger.OPENGL.err("Attempted to write freed image");
            new Throwable().printStackTrace();
        }
        else bytes = getBytes(pixels, width, height);
    }
    
    public void freePixels()
    {
        pixels = null;
    }
    
    public static ByteBuffer getBytes(byte[][][] pixels, int w, int h)
    {
        ByteBuffer bytes = ByteBuffer.allocateDirect(w*h*4);
        
        for(int y = 0; y < h; y++)
        {
            for(int x = 0; x < w; x++)
            {
                bytes.put(pixels[x][y][0]);
                bytes.put(pixels[x][y][1]);
                bytes.put(pixels[x][y][2]);
                bytes.put(pixels[x][y][3]);
            }
        }
        
        bytes.flip();
        
        return bytes;
    }
}
