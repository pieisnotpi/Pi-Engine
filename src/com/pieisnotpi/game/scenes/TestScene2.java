package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.rendering.ui.text.font.SystemFont;
import com.pieisnotpi.game.blocks.Block;
import com.pieisnotpi.game.blocks.Stone;
import com.pieisnotpi.game.cameras.FirstPersonCamera;
import org.joml.Vector2f;

public class TestScene2 extends PauseScene
{
    private final static int w = 50, h = 50;
    protected Block[][] blocks = new Block[w][h];

    public void init()
    {
        super.init();

        name = "Test Scene 2";

        cameras.add(new FirstPersonCamera(new Vector2f(0, 0), new Vector2f(1, 1), 90, 0, this));
        /*cameras.add(new OrbitCamera(new Vector3f(4, 2, 4), new Vector3f(0, 1, 0), new Vector2f(0, 0), new Vector2f(0.5f, 0.5f), 90, this));
        cameras.add(new OrbitCamera(new Vector3f(-4, 2, 4), new Vector3f(0, 1, 0), new Vector2f(0.5f, 0), new Vector2f(0.5f, 0.5f), 90, this));
        cameras.add(new OrbitCamera(new Vector3f(-4, 2, -4), new Vector3f(0, 1, 0), new Vector2f(0, 0.5f), new Vector2f(0.5f, 0.5f), 90, this));
        cameras.add(new OrbitCamera(new Vector3f(4, 2, -4), new Vector3f(0, 1, 0), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f), 90, this));*/
        clearColor.set(0.918f, 0.729f, 0.125f);

        float xOffset = -(w/2f)*Block.SIZE, zOffset = -(h/2f)*Block.SIZE;

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                blocks[x][y] = new Stone(xOffset, 0, zOffset += Block.SIZE, this);
                if(x < w - 1) blocks[x][y].cubes.get(0).sides[2].unregister();
                if(x > 0) blocks[x][y].cubes.get(0).sides[1].unregister();
                if(y < h - 1) blocks[x][y].cubes.get(0).sides[3].unregister();
                if(y > 0) blocks[x][y].cubes.get(0).sides[0].unregister();
            }

            zOffset = -(h/2f)*Block.SIZE;
            xOffset += Block.SIZE;
        }

        String t3dt = "This text is waving.", t3d2t = "I.\nLike.\nTests.";
        SystemFont font = new SystemFont("Arial", 48, SystemFont.PLAIN, true);
        float t3dw = Text.approxWidth(t3dt, 24, font), t3d2w = Text.approxWidth(t3d2t, 24, Text.pixelFont);

        Text text3D = new Text(t3dt, 24, -t3dw/2, 1.5f, 0, new Color(1, 0, 0), new Color(0, 0, 0), PiEngine.C_PERSPECTIVE, this/*, new WaveEffect()*/);
        text3D.setFont(font);
        Text text3D2 = new Text(t3d2t, 24, -t3d2w/2, 1.5f, 0.1f, new Color(0, 1, 0), new Color(0, 0, 0), PiEngine.C_PERSPECTIVE, this/*, new WaveEffect(4, 8, 0.5f)*/);
    }
}
