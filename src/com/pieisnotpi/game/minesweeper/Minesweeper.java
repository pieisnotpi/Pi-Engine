package com.pieisnotpi.game.minesweeper;

import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;

public class Minesweeper extends Scene
{
    protected Tile[][] tiles = new Tile[16][16];

    public void init()
    {
        super.init();

        fps.disable();

        cameras.add(new Camera(0, 0, 1, 1, 90, this));
        clearColor.set(0.55f, 0.39f, 0);

        float xScale = 2f/tiles.length, yScale = 2f/tiles[0].length, xOffset = -tiles.length*xScale/2, yOffset = -tiles[0].length*yScale/2;

        Sprite sprite = new Sprite(Texture.getTexture("grass"), 0, 0, 16, 16);

        for(int x = 0; x < tiles.length; x++)
        {
            for(int y = 0; y < tiles[0].length; y++)
            {
                tiles[x][y] = new Tile(xOffset + x*xScale + 0.0001f, yOffset + y*yScale + 0.0001f, 0.5f, Float.min(xScale, yScale), sprite, this);
            }
        }
    }

    public void update()
    {
        super.update();
    }
}
