package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.Transform;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexMaterial;
import com.pieisnotpi.engine.rendering.shapes.Quad;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.game.blocks.Block;
import com.pieisnotpi.game.blocks.Grass;
import com.pieisnotpi.game.cameras.FirstPersonCamera;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class TestScene extends PauseScene
{
    private static final int w = 100, h = 100;
    protected Mesh<Quad> blocksMesh;

    public TestScene init()
    {
        super.init();

        name = "Test Scene 1";

        cameras.add(new FirstPersonCamera(90, 0, this).setViewport(new Vector2f(0, 0), new Vector2f(1, 1)));
        clearColor.set(0.125f, 0.729f, 0.918f);

        float xOffset = -(w/2f)*Block.SIZE, zOffset = -(h/2f)*Block.SIZE;

        Grass[][] blocks = new Grass[w][h];

        TexMaterial blocksMaterial = new TexMaterial(PiEngine.M_PERSP, Texture.getTextureFile("grass"));
        blocksMesh = new Mesh<Quad>(blocksMaterial, new Transform(), GL11.GL_TRIANGLE_STRIP, 4, true, this).setRenderables(new ArrayList<>(w*h*4));

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                Grass block = blocks[x][y] = new Grass(xOffset, 0, zOffset += Block.SIZE, this);
                if(x < w - 1) block.cubes.get(0).sides[2].enabled = false;
                if(x > 0) block.cubes.get(0).sides[1].enabled = false;
                if(y < h - 1) block.cubes.get(0).sides[3].enabled = false;
                if(y > 0) block.cubes.get(0).sides[0].enabled = false;

                block.cubes.forEach(cube ->
                {
                    for(Quad side : cube.sides) if(side.enabled) blocksMesh.addRenderable(side);
                });
            }

            zOffset = -(h/2f)*Block.SIZE;
            xOffset += Block.SIZE;
        }

        blocksMesh.build().register();

        /*String t3dt = "This text is waving.", t3d2t = "This text is hovering.";
        float t3dw = Text.approxWidth(t3dt, 24, Text.pixelFont), t3d2w = Text.approxWidth(t3d2t, 24, Text.pixelFont);

        Text text3D = new Text(t3dt, new Vector3f(-t3dw/2, 1.5f, 0.5f), new Color(1, 0, 0), new Color(0, 0, 0), PiEngine.M_PERSP, this, new WaveEffect(4, 2, 5));
        text3D.getMesh().setScale(0.1f, 0.1f, 1);
        Text text3D2 = new Text(t3d2t, new Vector3f(-t3d2w/2, 1, 0.45f), new Color(0, 1, 0), new Color(0, 0, 0), PiEngine.M_PERSP, this, new WaveEffect(4, 0, 5));
        text3D2.getMesh().setScale(0.1f, 0.1f, 1);*/

        return this;
    }
}
