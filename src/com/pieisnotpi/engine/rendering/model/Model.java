package com.pieisnotpi.engine.rendering.model;

import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Model
{
    public List<CubeModel> cubes;
    public List<RectModel> rects;

    public Model(String path)
    {
        List<String> strings = new ArrayList<>();

        Scanner scanner = new Scanner(Model.class.getResourceAsStream(path));

        while(scanner.hasNextLine()) strings.add(scanner.nextLine());
        scanner.close();

        cubes = new ArrayList<>();
        rects = new ArrayList<>();

        for(String string : strings)
        {
            string = string.replaceAll(" ", "");

            if(string.startsWith("cube"))
            {
                int index = string.indexOf('(');

                String texName;
                float x0, y0, z0, x1, y1, z1;
                int uv0x0, uv0y0, uv0x1, uv0y1, uv1x0, uv1y0, uv1x1, uv1y1, uv2x0, uv2y0, uv2x1, uv2y1;

                texName = string.substring(index += 1, index = string.indexOf(',', index));
                x0 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                y0 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                z0 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                x1 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                y1 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                z1 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                uv0x0 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uv0y0 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uv0x1 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uv0y1 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uv1x0 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uv1y0 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uv1x1 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uv1y1 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uv2x0 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uv2y0 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uv2x1 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uv2y1 = Integer.parseInt(string.substring(index += 1, string.indexOf(')', index)));

                Texture tex = Texture.getTexture(texName);

                cubes.add(new CubeModel(x0, y0, z0, x1, y1, z1, new Sprite(tex, uv0x0, uv0y0, uv0x1, uv0y1), new Sprite(tex, uv1x0, uv1y0, uv1x1, uv1y1), new Sprite(tex, uv2x0, uv2y0, uv2x1, uv2y1)));
            }
            else if(string.startsWith("rect"))
            {
                int index = string.indexOf('(');

                String texName;
                float x0, y0, z0, x1, y1, z1;
                int uvx0, uvy0, uvx1, uvy1;

                texName = string.substring(index += 1, index = string.indexOf(',', index));
                x0 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                y0 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                z0 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                x1 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                y1 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                z1 = Float.parseFloat(string.substring(index += 1, index = string.indexOf(',', index)));
                uvx0 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uvy0 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uvx1 = Integer.parseInt(string.substring(index += 1, index = string.indexOf(',', index)));
                uvy1 = Integer.parseInt(string.substring(index += 1, string.indexOf(',', index)));

                Texture tex = Texture.getTexture(texName);

                rects.add(new RectModel(x0, y0, z0, x1, y1, z1, new Sprite(tex, uvx0, uvy0, uvx1, uvy1)));
            }
            else System.out.println("this is bad " + string);
        }
    }
}
