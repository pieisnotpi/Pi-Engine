package com.pieisnotpi.engine.rendering.textures.image;

import com.pieisnotpi.engine.utility.FileLoader;

import javafx.scene.paint.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static com.pieisnotpi.engine.utility.Color.valueToByte;

public class Image
{
    public int width, height;
    public ByteBuffer bytes;
    
    protected Image(){}
    
    public Image(String path)
    {
        InputStream s = FileLoader.findStream(path, getClass());
        if(s == null) return;
        
        javafx.scene.image.Image image = new javafx.scene.image.Image(s);
        
        width = (int) image.getWidth();
        height = (int) image.getHeight();
        
        bytes = getBytes(image);
    }
    
    public Image(javafx.scene.image.Image image)
    {
        width = (int) image.getWidth();
        height = (int) image.getHeight();
        
        bytes = getBytes(image);
    }
    
    public Image(BufferedImage image)
    {
        width = image.getWidth();
        height = image.getHeight();
        
        bytes = getBytes(image);
    }
    
    public void freeBuffer()
    {
        bytes.limit(0);
    }
    
    public static ByteBuffer getBytes(javafx.scene.image.Image image)
    {
        int w = (int) image.getWidth(), h = (int) image.getHeight();
        
        ByteBuffer bytes = ByteBuffer.allocateDirect((int) (4*image.getWidth()*image.getHeight()));
        
        for(int y = 0; y < h; y++)
        {
            for(int x = 0; x < w; x++)
            {
                Color pixel = image.getPixelReader().getColor(x, y);
                
                bytes.put(valueToByte((float) pixel.getRed()));
                bytes.put(valueToByte((float) pixel.getGreen()));
                bytes.put(valueToByte((float) pixel.getBlue()));
                bytes.put(valueToByte((float) pixel.getOpacity()));
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
