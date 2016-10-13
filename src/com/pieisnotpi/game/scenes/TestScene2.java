package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.Transform;
import com.pieisnotpi.engine.rendering.shaders.types.ads_shader.ADSMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexQuad;
import com.pieisnotpi.engine.rendering.shapes.Quad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.ui.text.Text;
import com.pieisnotpi.engine.ui.text.effects.RainbowEffect;
import com.pieisnotpi.engine.ui.text.effects.WaveEffect;
import com.pieisnotpi.engine.ui.text.font.PixelFont;
import com.pieisnotpi.engine.ui.text.font.SystemFont;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.game.blocks.Block;
import com.pieisnotpi.game.blocks.Metal;
import com.pieisnotpi.game.cameras.OrbitCamera;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class TestScene2 extends PauseScene
{
    private static final int w = 100, h = 100;

    private float timer = 0;
    protected Mesh<Quad> blocksMesh;

    public TestScene2 init()
    {
        super.init();

        name = "Test Scene 2";
        Camera c;

        cameras.add(c = new OrbitCamera(new Vector3f(4, 2, 4), new Vector3f(0, 1, 0), 90, this).setViewport(new Vector2f(0, 0), new Vector2f(0.5f, 0.5f)).setFramebufferRes(new Vector2i(512, 512)));
        cameras.add(new OrbitCamera(new Vector3f(-4, 2, 4), new Vector3f(0, 1, 0), 90, this).setViewport(new Vector2f(0.5f, 0), new Vector2f(0.5f, 0.5f)));
        cameras.add(new OrbitCamera(new Vector3f(-4, 2, -4), new Vector3f(0, 1, 0), 90, this).setViewport(new Vector2f(0, 0.5f), new Vector2f(0.5f, 0.5f)));
        cameras.add(new OrbitCamera(new Vector3f(4, 2, -4), new Vector3f(0, 1, 0), 90, this).setViewport(new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f)));

        clearColor.set(0.918f, 0.729f, 0.125f);

        float xOffset = -(w/2f)*Block.SIZE, zOffset = -(h/2f)*Block.SIZE;

        Metal[][] blocks = new Metal[w][h];

        ADSMaterial blockMaterial = new ADSMaterial(new Vector3f(0.2f), new Vector3f(0.2f), new Vector3f(0.1f), 10, PiEngine.M_PERSP, Texture.getTextureFile("metal"));
        blocksMesh = new Mesh<Quad>(blockMaterial, new Transform(), GL11.GL_TRIANGLE_STRIP, 4, true, this).setRenderables(new ArrayList<>(w*h*4));

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                Block block = blocks[x][y] = new Metal(xOffset, 0, zOffset += Block.SIZE, this);
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

        TexQuad texQuad = new TexQuad(-0.4445f, 1, 0.2f, 0.889f, 0.5f, 0, new Sprite(-1, -1, 0f, 0f, 1f, 1f, false));
        Mesh fbMesh = new Mesh<TexQuad>(new TexMaterial(PiEngine.M_PERSP, c.frameBuffer.texture), new Transform(), GL11.GL_TRIANGLE_STRIP, 4, true, this).addRenderable(texQuad).build().register();

        String t3dt = "This is a reaaaaalllly long line of text. It's for testing stuff.", t3d2t = "I.\nLike.\nTests.";
        SystemFont font = SystemFont.getFont("Arial", 48, SystemFont.PLAIN, true);

        Text text3D = new Text(font, t3dt, new Vector3f(0, 1.5f, 0), new Color(1, 0, 0), new Color(0, 0, 0), PiEngine.M_PERSP, this, new RainbowEffect(0.01f));
        text3D.getTransform().setParent(blocksMesh.getTransform()).setTranslate(-4, 2, 0).setScale(0.01f, 0.01f, 0.01f);

        Text text3D2 = new Text(PixelFont.getFont(), t3d2t, new Vector3f(0, 1.5f, 0.025f), new Color(0, 1, 0), new Color(0, 0, 0), PiEngine.M_PERSP, this, new WaveEffect(4, 8, 5));
        text3D2.getTransform().setParent(blocksMesh.getTransform()).setTranslate(0, -2, 0).setScale(0.05f, 0.05f, 0.05f);

        return this;
    }
}
