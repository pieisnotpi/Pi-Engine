package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shapes.types.textured.TexQuad;
import com.pieisnotpi.engine.rendering.textures.AnimatedSprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.rendering.ui.text.effects.WaveEffect;
import com.pieisnotpi.engine.rendering.ui.text.font.SystemFont;
import com.pieisnotpi.game.blocks.Block;
import com.pieisnotpi.game.blocks.Stone;
import com.pieisnotpi.game.cameras.ControlCamera;

public class TestScene2 extends PauseScene
{
    protected Text text3D, text3D2;
    protected Block[][] blocks = new Block[30][30];

    public void init()
    {
        super.init();

        name = "Test Scene 2";

        cameras.add(new ControlCamera(0, 0, 1, 1, 90, 0, this));
        clearColor.set(0.918f, 0.729f, 0.125f);

        float xOffset = -(blocks.length/2f)*Block.SIZE, zOffset = -(blocks[0].length/2f)*Block.SIZE;

        for(int x = 0; x < blocks.length; x++)
        {
            for(int y = 0; y < blocks[0].length; y++)
            {
                blocks[x][y] = new Stone(xOffset, 0, zOffset += Block.SIZE, this);
                if(x < blocks.length - 1) blocks[x][y].cubes.get(0).sides[2].unregister();
                if(x > 0) blocks[x][y].cubes.get(0).sides[1].unregister();
                if(y < blocks[0].length - 1) blocks[x][y].cubes.get(0).sides[3].unregister();
                if(y > 0) blocks[x][y].cubes.get(0).sides[0].unregister();
            }

            zOffset = -(blocks[0].length/2f)*Block.SIZE;
            xOffset += Block.SIZE;
        }

        String t3dt = "This text is waving.", t3d2t = "I.\nLike.\nTests.";
        float t3dw = Text.approxWidth(t3dt, 24, Text.pixelFont), t3d2w = Text.approxWidth(t3d2t, 24, Text.pixelFont);

        TexQuad q = new TexQuad(0, 0.5f, 0.2f, 0.5f, 0.5f, 0, new AnimatedSprite(true, 500, Texture.getTexture("tiles"), 0, 0, 48, 48, 16, 16), PiEngine.CAMERA_3D_ID, this);

        text3D = new Text(t3dt, 24, -t3dw/2, 1.5f, 0.5f, new Color(1, 0, 0), new Color(0, 0, 0), PiEngine.CAMERA_3D_ID, this, new WaveEffect());
        text3D.setFont(new SystemFont("Arial", 48, SystemFont.PLAIN, true));
        text3D2 = new Text(t3d2t, 24, -t3d2w/2, 2, 0.45f, new Color(0, 1, 0), new Color(0, 0, 0), PiEngine.CAMERA_3D_ID, this, new WaveEffect(4, 8, 0.5f));
    }
}
