package com.pieisnotpi.engine.rendering.textures;

import com.pieisnotpi.engine.output.Logger;

import java.util.ArrayList;
import java.util.List;

public class TextureDatabase
{
    public static List<Texture> textures = new ArrayList<>();

    public static Texture getTexture(String name)
    {
        for(Texture texture : textures) if(texture.name.equals(name)) return texture;

        try
        {
            Texture temp = new Texture("/assets/textures/" + name + ".png");
            Logger.TEXTURES.debug("Found texture '" + name + ".png'");
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
