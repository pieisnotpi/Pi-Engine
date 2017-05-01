package com.pieisnotpi.engine.image;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.engine.utility.FileUtility;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static com.pieisnotpi.engine.utility.Color.byteToValue;
import static com.pieisnotpi.engine.utility.Color.valueToByte;

public class WritableImage extends Image
{
    public byte[][][] pixels;
    
    public WritableImage(Texture texture)
    {
        float[] temp = texture.getGLImage();
        
        System.out.println(temp.length);
        System.out.printf("(%f,%f,%f,%f)%n", temp[0], temp[1], temp[2], temp[3]);
        
        pixels = new byte[width][height][4];
        width = texture.res.x;
        height = texture.res.y;
        
        for(int i = 0, x = 0, y = 0; i < pixels.length; i += 4, x++)
        {
            if(x >= width)
            {
                x = 0;
                y++;
            }
            
            pixels[x][y][0] = valueToByte(temp[i]);
            pixels[x][y][1] = valueToByte(temp[i+1]);
            pixels[x][y][2] = valueToByte(temp[i+2]);
            pixels[x][y][3] = valueToByte(temp[i+3]);
        }
    }
    
    public WritableImage(String path)
    {
        InputStream s = FileUtility.findStream(path, getClass());
    
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
    
    public static boolean writeImage(WritableImage image, File file) throws IOException
    {
        if(image.pixels == null) return false;
    
        javafx.scene.image.WritableImage temp = new javafx.scene.image.WritableImage(image.width, image.height);
    
        PixelWriter writer = temp.getPixelWriter();
        
        for(int x = 0; x < image.width; x++)
        {
            for(int y = 0; y < image.height; y++)
            {
                javafx.scene.paint.Color color = new javafx.scene.paint.Color(byteToValue(image.pixels[x][y][0]), byteToValue(image.pixels[x][y][1]), byteToValue(image.pixels[x][y][2]), byteToValue(image.pixels[x][y][3]));
                writer.setColor(x, y, color);
            }
        }
        
        return ImageIO.write(SwingFXUtils.fromFXImage(temp, null), "PNG", file);
    }
}
