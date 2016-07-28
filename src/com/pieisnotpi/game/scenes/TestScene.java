package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.Mesh;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexMaterial;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.rendering.ui.text.effects.WaveEffect;
import com.pieisnotpi.game.blocks.Block;
import com.pieisnotpi.game.blocks.Grass;
import com.pieisnotpi.game.cameras.TransitionCamera;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class TestScene extends PauseScene
{
    private static final int w = 100, h = 100;
    protected Mesh blockMesh;

    public void init()
    {
        super.init();

        name = "Test Scene 1";

        TransitionCamera c;
        cameras.add(c = new TransitionCamera(new Vector3f(0, 10, 0), new Vector3f(0, 2, -10), 90, 0.01f, 0.1f, 0.1f, this).setViewport(new Vector2f(0, 0), new Vector2f(1, 1)));
        clearColor.set(0.125f, 0.729f, 0.918f);

        float xOffset = -(w/2f)*Block.SIZE, zOffset = -(h/2f)*Block.SIZE;
        List<Renderable> renderables = new ArrayList<>(w*h*6);

        Block[][] blocks = new Block[w][h];

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                Block block = blocks[x][y] = new Grass(xOffset, 0, zOffset += Block.SIZE, this);
                if(x < w - 1) block.cubes.get(0).sides[2].enabled = false;
                if(x > 0) block.cubes.get(0).sides[1].enabled = false;
                if(y < h - 1) block.cubes.get(0).sides[3].enabled = false;
                if(y > 0) block.cubes.get(0).sides[0].enabled = false;
                renderables.addAll(block.getRenderables());
            }

            zOffset = -(h/2f)*Block.SIZE;
            xOffset += Block.SIZE;
        }

        blockMesh = new Mesh(new TexMaterial(PiEngine.C_PERSPECTIVE, Texture.getTexture("grass")), GL11.GL_TRIANGLE_STRIP, 4, true, this).setRenderables(renderables).build();
        blocks = null;
        renderables.clear();

        String t3dt = "This text is waving.", t3d2t = "This text is hovering.";
        float t3dw = Text.approxWidth(t3dt, 24, Text.pixelFont), t3d2w = Text.approxWidth(t3d2t, 24, Text.pixelFont);

        Text text3D = new Text(t3dt, new Vector3f(-t3dw/2, 1.5f, 0.5f), new Color(1, 0, 0), new Color(0, 0, 0), PiEngine.C_PERSPECTIVE, this, new WaveEffect());
        text3D.getMesh().setScale(0.1f, 0.1f, 1);
        Text text3D2 = new Text(t3d2t, new Vector3f(-t3d2w/2, 1, 0.45f), new Color(0, 1, 0), new Color(0, 0, 0), PiEngine.C_PERSPECTIVE, this, new WaveEffect(4, 0, 0.1f));
        text3D2.getMesh().setScale(0.1f, 0.1f, 1);

        c.transitionX(-2);
        c.transitionY(4);
        c.transitionZ(-2);
        //c.transitionFov(10);
        c.transitionXRot(-80);
        c.transitionYRot(45);
    }
}
