package com.pieisnotpi.game.test_editor;

import com.pieisnotpi.engine.input.devices.Keyboard;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class EditorScene extends Scene
{
    public SpriteSet curSpriteSet;
    public int curType = 0;
    private List<SpriteSet> types = new ArrayList<>();

    EditorTile[][] tiles = new EditorTile[24][24];

    public void init()
    {
        super.init();

        name = "Editor";

        cameras.add(new Camera(90, this).setViewport(new Vector2f(0, 0), new Vector2f(1, 1)));
        clearColor.set(0.6f, 0.6f, 0.6f);

        fps.disable();
        pps.disable();

        int max = Integer.max(tiles.length, tiles[0].length);

        float xScale = 2f/max, yScale = 2f/max, xOffset = -tiles.length*xScale/2, yOffset = -tiles[0].length*yScale/2;

        Texture tex = Texture.getTexture("tiles");

        Sprite[] grass = new Sprite[8];
        grass[0] = new Sprite(tex, 0, 16, 16, 0);
        grass[1] = new Sprite(tex, 16, 16, 32, 0);
        grass[2] = new Sprite(tex, 32, 16, 48, 0);
        grass[3] = new Sprite(tex, 32, 32, 48, 16);
        grass[4] = new Sprite(tex, 32, 48, 48, 32);
        grass[5] = new Sprite(tex, 16, 48, 32, 32);
        grass[6] = new Sprite(tex, 0, 48, 16, 32);
        grass[7] = new Sprite(tex, 0, 32, 16, 16);

        Sprite[] grassStair = new Sprite[4];
        grassStair[0] = new Sprite(tex, 128, 16, 144, 0);
        grassStair[1] = new Sprite(tex, 144, 16, 160, 0);
        grassStair[2] = new Sprite(tex, 144, 32, 160, 16);
        grassStair[3] = new Sprite(tex, 128, 32, 144, 16);

        Sprite[] stone = new Sprite[8];
        stone[0] = new Sprite(tex, 48, 16, 64, 0);
        stone[1] = new Sprite(tex, 64, 16, 80, 0);
        stone[2] = new Sprite(tex, 80, 16, 96, 0);
        stone[3] = new Sprite(tex, 80, 32, 96, 16);
        stone[4] = new Sprite(tex, 80, 48, 96, 32);
        stone[5] = new Sprite(tex, 64, 48, 80, 32);
        stone[6] = new Sprite(tex, 48, 48, 64, 32);
        stone[7] = new Sprite(tex, 48, 32, 64, 16);

        Sprite[] stoneStair = new Sprite[4];
        stoneStair[0] = new Sprite(tex, 96, 16, 112, 0);
        stoneStair[1] = new Sprite(tex, 112, 16, 128, 0);
        stoneStair[2] = new Sprite(tex, 112, 32, 128, 16);
        stoneStair[3] = new Sprite(tex, 96, 32, 112, 16);

        Sprite[] dirt = new Sprite[1];
        dirt[0] = new Sprite(tex, 16, 32, 32, 16);

        types.add(new SpriteSet(grass));
        types.add(new SpriteSet(grassStair));
        types.add(new SpriteSet(stone));
        types.add(new SpriteSet(stoneStair));
        types.add(new SpriteSet(dirt));

        curSpriteSet = types.get(curType);

        for(int x = 0; x < tiles.length; x++)
        {
            for(int y = 0; y < tiles[0].length; y++)
            {
                tiles[x][y] = new EditorTile(xOffset + x*xScale + 0.0001f, yOffset + y*yScale + 0.0001f, 0.5f, Float.min(xScale, yScale), curSpriteSet.getCurrent(), this);
                tiles[x][y].blank();
            }
        }
    }

    public Sprite getCurSprite()
    {
        return curSpriteSet.getCurrent();
    }

    public void onKeyPressed(int key, int mods)
    {
        if(key == Keyboard.KEY_LEFT_BRACKET) curType--;
        else if(key == Keyboard.KEY_RIGHT_BRACKET) curType++;

        if(curType >= types.size()) curType = 0;
        else if(curType < 0) curType = types.size() - 1;

        curSpriteSet = types.get(curType);

        onScroll(0, 0);

        super.onKeyPressed(key, mods);
    }

    public void onScroll(float x, float y)
    {
        super.onScroll(x, y);

        if(y < 0) curSpriteSet.addToCurrent(-1);
        else if(y > 0) curSpriteSet.addToCurrent(1);

        gameObjects.forEach(gameObject ->
        {
            if(!gameObject.getClass().equals(EditorTile.class)) return;

            EditorTile tile = (EditorTile) gameObject;
            if(tile.mouseHoverStatus) tile.onMouseEntered();
        });
    }
}
