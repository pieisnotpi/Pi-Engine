package com.pieisnotpi.test.test_editor;

import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.mesh.Transform;
import com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader.TexCMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader.TexCQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class EditorScene extends Scene
{
    private SpriteSet curSpriteSet;
    private List<SpriteSet> types = new ArrayList<>();
    private EditorTile[][] tiles = new EditorTile[24][24];

    private int curType = 0;
    
    public GameObject<TexCQuad> tilesObject;

    public EditorScene init() throws Exception
    {
        super.init();

        name = "Editor";

        Texture tex = Texture.getTextureFile("tiles");
        assert tex != null;
        int tw = tex.image.width, th = tex.image.height;

        Mesh<TexCQuad> mesh = new Mesh<>(new TexCMaterial(Camera.ORTHO2D_S, tex), new Transform(), MeshConfig.QUAD);
        addGameObject(tilesObject = new GameObject<>(mesh));

        cameras.add(new Camera(90, new Vector2f(0, 0), new Vector2f(1, 1)));
        clearColor.set(0.6f, 0.6f, 0.6f);

        Sprite[] grass = new Sprite[8];
        grass[0] = new Sprite(tw, th, 0, 0, 16, 16);
        grass[1] = new Sprite(tw, th, 16, 0, 32, 16);
        grass[2] = new Sprite(tw, th, 32, 0, 48, 16);
        grass[3] = new Sprite(tw, th, 32, 16, 48, 32);
        grass[4] = new Sprite(tw, th, 32, 32, 48, 48);
        grass[5] = new Sprite(tw, th, 16, 32, 32, 48);
        grass[6] = new Sprite(tw, th, 0, 32, 16, 48);
        grass[7] = new Sprite(tw, th, 0, 16, 16, 32);

        Sprite[] grassStair = new Sprite[4];
        grassStair[0] = new Sprite(tw, th, 128, 0, 144, 16);
        grassStair[1] = new Sprite(tw, th, 144, 0, 160, 16);
        grassStair[2] = new Sprite(tw, th, 144, 16, 160, 32);
        grassStair[3] = new Sprite(tw, th, 128, 16, 144, 32);

        Sprite[] stone = new Sprite[8];
        stone[0] = new Sprite(tw, th, 48, 0, 64, 16);
        stone[1] = new Sprite(tw, th, 64, 0, 80, 16);
        stone[2] = new Sprite(tw, th, 80, 0, 96, 16);
        stone[3] = new Sprite(tw, th, 80, 16, 96, 32);
        stone[4] = new Sprite(tw, th, 80, 32, 96, 48);
        stone[5] = new Sprite(tw, th, 64, 32, 80, 48);
        stone[6] = new Sprite(tw, th, 48, 32, 64, 48);
        stone[7] = new Sprite(tw, th, 48, 16, 64, 32);

        Sprite[] stoneStair = new Sprite[4];
        stoneStair[0] = new Sprite(tw, th, 96, 0, 112, 16);
        stoneStair[1] = new Sprite(tw, th, 112, 0, 128, 16);
        stoneStair[2] = new Sprite(tw, th, 112, 16, 128, 32);
        stoneStair[3] = new Sprite(tw, th, 96, 16, 112, 32);

        Sprite[] dirt = new Sprite[1];
        dirt[0] = new Sprite(tw, th, 16, 16, 32, 32);

        types.add(new SpriteSet(grass));
        types.add(new SpriteSet(grassStair));
        types.add(new SpriteSet(stone));
        types.add(new SpriteSet(stoneStair));
        types.add(new SpriteSet(dirt));

        curSpriteSet = types.get(curType);

        int max = Integer.max(tiles.length, tiles[0].length);

        float xScale = 2f/max, yScale = 2f/max, min = Float.min(xScale, yScale), xOffset = -tiles.length*xScale/2, yOffset = -tiles[0].length*yScale/2;
        Sprite current = curSpriteSet.getCurrent();

        for(int x = 0; x < tiles.length; x++) for(int y = 0; y < tiles[0].length; y++) tiles[x][y] = new EditorTile(xOffset + x*xScale, yOffset + y*yScale, -0.5f, min, current, this);

        return this;
    }

    @Override
    public void drawUpdate(float timeStep) throws Exception
    {
        super.drawUpdate(timeStep);
        tilesObject.getMesh().flagForBuild();
    }

    public Sprite getCurSprite()
    {
        return curSpriteSet.getCurrent();
    }

    public void onKeyPressed(int key, int mods)
    {
        super.onKeyPressed(key, mods);

        if(key == Keyboard.KEY_LEFT_BRACKET) onScroll(-1, 0);
        else if(key == Keyboard.KEY_RIGHT_BRACKET) onScroll(1, 0);
    }

    public void onScroll(float x, float y)
    {
        super.onScroll(x, y);

        if(y < 0) curSpriteSet.addToCurrent(-1);
        else if(y > 0) curSpriteSet.addToCurrent(1);

        if(x < 0) addToSet(-1);
        else if(x > 0) addToSet(1);

        gameObjects.forEach(gameObject ->
        {
            if(!gameObject.getClass().equals(EditorTile.class)) return;

            EditorTile tile = (EditorTile) gameObject;
            if(tile.isMouseOver()) tile.onMouseEntered();
        });
    }
    
    private void addToSet(int amount)
    {
        curType += amount;
        
        if(curType >= types.size()) curType = 0;
        else if(curType < 0) curType = types.size() - 1;
    
        curSpriteSet = types.get(curType);
    }
}
