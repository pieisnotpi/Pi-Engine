package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.Mesh;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.types.ads_shader.ADSMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.rendering.ui.text.effects.WaveEffect;
import com.pieisnotpi.engine.rendering.ui.text.font.SystemFont;
import com.pieisnotpi.game.blocks.Block;
import com.pieisnotpi.game.blocks.Metal;
import com.pieisnotpi.game.cameras.OrbitCamera;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class TestScene2 extends PauseScene
{
    private final static int w = 100, h = 100;
    protected Mesh blocksMesh;

    public void init()
    {
        super.init();

        name = "Test Scene 2";
        Camera c;

        //cameras.add(c = new FirstPersonCamera(90, 0, this).setViewport(new Vector2f(0, 0), new Vector2f(1, 1)).setFramebufferRes(new Vector2i(512)));
        cameras.add(c = new OrbitCamera(new Vector3f(8, 2, 8), new Vector3f(0, 1, 0), 90, this).setViewport(new Vector2f(0, 0), new Vector2f(1, 1)).setFramebufferRes(new Vector2i(512, 512)));
        /*cameras.add(c = new OrbitCamera(new Vector3f(4, 2, 4), new Vector3f(0, 1, 0), 90, this).setViewport(new Vector2f(0, 0), new Vector2f(0.5f, 0.5f)).setFramebufferRes(new Vector2i(512, 512)));
        cameras.add(new OrbitCamera(new Vector3f(-4, 2, 4), new Vector3f(0, 1, 0), 90, this).setViewport(new Vector2f(0.5f, 0), new Vector2f(0.5f, 0.5f)));
        cameras.add(new OrbitCamera(new Vector3f(-4, 2, -4), new Vector3f(0, 1, 0), 90, this).setViewport(new Vector2f(0, 0.5f), new Vector2f(0.5f, 0.5f)));
        cameras.add(new OrbitCamera(new Vector3f(4, 2, -4), new Vector3f(0, 1, 0), 90, this).setViewport(new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f)));*/

        Texture tex = c.frameBuffer.texture;
        TexQuad texQuad = new TexQuad(-0.4445f, 1, 0.2f, 0.889f, 0.5f, 0, new Sprite(tex, 0, 0, tex.width, tex.height, false));
        Mesh fbMesh = new Mesh(new TexMaterial(PiEngine.C_PERSPECTIVE, tex), GL11.GL_TRIANGLE_STRIP, 4, true, this).addRenderable(texQuad).build().register();

        clearColor.set(0.918f, 0.729f, 0.125f);

        float xOffset = -(w/2f)*Block.SIZE, zOffset = -(h/2f)*Block.SIZE;
        List<Renderable> renderables = new ArrayList<>(w*h*6);

        Metal[][] blocks = new Metal[w][h];

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                Block block = blocks[x][y] = new Metal(xOffset, 0, zOffset += Block.SIZE, this);
                if(x < w - 1) block.cubes.get(0).sides[2].enabled = false;
                if(x > 0) block.cubes.get(0).sides[1].enabled = false;
                if(y < h - 1) block.cubes.get(0).sides[3].enabled = false;
                if(y > 0) block.cubes.get(0).sides[0].enabled = false;
                renderables.addAll(block.getRenderables());
            }

            zOffset = -(h/2f)*Block.SIZE;
            xOffset += Block.SIZE;
        }

        blocksMesh = new Mesh(new ADSMaterial(new Vector3f(0.2f), new Vector3f(0.2f), new Vector3f(0.1f), 10, PiEngine.C_PERSPECTIVE, Texture.getTexture("metal")), GL11.GL_TRIANGLE_STRIP, 4, true, this).setRenderables(renderables).build().register();
        blocksMesh.rotateDegrees(0, 0, 45);
        renderables.clear();

        //String t = "This is some example text.\nIt's purpose is to test the performance of text\nI've run out of text to test with, so this is some filler:\nThis line is intended to be short.\nThis line is intended to be incredibly long and painful to read. I sincerely hope it is interpreted appropriately.\nDid you know that earth's gravity is approximately 9.81m/s?\nThat's pretty interesting.\nOf course, you're entitled to not like that fact, but you must remember, I'm entitled to not like you.\nThat seems pretty fair, doesn't it? I don't know.\nI guess some may see that as a personal attack for not being interested in physics.\nMaybe they'd see it that way because that's exactly what it is.\nIf you're not interested in physics facts, you're basically dead to me.\nIs that too harsh? Not at all.\nThose with no interest in the fundamental mechanics of the universe obviously have no souls.\nThat might as well be a scientific fact.\n";

        /*Text test = new Text("", 8, -1.2f, 0.5f, -0.2f, PiEngine.C_ORTHO2D_ID, this), test2 = new Text("", 8, -1.2f, 0.495f, -0.3f, new Color(0.1f, 0.1f, 0.1f), new Color(0, 0, 0, 0), PiEngine.C_ORTHO2D_ID, this);
        test.setFont(font);
        test2.setFont(font);
        test.setHAlignment(GameObject.HAlignment.LEFT, 0.05f);
        test2.setHAlignment(GameObject.HAlignment.LEFT, 0.05f);
        test.setText(t);
        test2.setText(t);
        test.disable();
        test2.disable();*/

        String t3dt = "This text is waving.", t3d2t = "I.\nLike.\nTests.";
        SystemFont font = new SystemFont("Arial", 48, SystemFont.PLAIN, true);
        float t3dw = Text.approxWidth(t3dt, 24, font), t3d2w = Text.approxWidth(t3d2t, 24, Text.pixelFont);

        Text text3D = new Text(t3dt, new Vector3f(0, 1.5f, 0), new Color(1, 0, 0), new Color(0, 0, 0), PiEngine.C_PERSPECTIVE, this, new WaveEffect());
        text3D.getMesh().setTranslate(0, 0, 0).setScale(0.01f, 0.01f, 1);
        text3D.setFont(font);
        Text text3D2 = new Text(t3d2t, new Vector3f(0, 1.5f, 0.025f), new Color(0, 1, 0), new Color(0, 0, 0), PiEngine.C_PERSPECTIVE, this, new WaveEffect(4, 8, 0.5f));
        text3D2.getMesh().setScale(0.05f, 0.05f, 1);

        //keybinds.add(new Keybind(Keyboard.KEY_ENTER, false, (value) -> { test.toggle(); test2.toggle(); }, null));
    }
}
