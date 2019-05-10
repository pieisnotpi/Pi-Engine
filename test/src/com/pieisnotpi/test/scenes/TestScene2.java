package com.pieisnotpi.test.scenes;

import com.pieisnotpi.engine.input.keyboard.Keybind;
import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.mesh.Transform;
import com.pieisnotpi.engine.rendering.primitives.Quad;
import com.pieisnotpi.engine.rendering.shaders.types.ads.ADSMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.ads.ADSPointLight;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.ui.text.Text;
import com.pieisnotpi.engine.ui.text.effects.RainbowEffect;
import com.pieisnotpi.engine.ui.text.effects.UnfoldEffect;
import com.pieisnotpi.engine.ui.text.effects.WaveEffect;
import com.pieisnotpi.engine.ui.text.font.SystemFont;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.engine.utility.MathUtility;
import com.pieisnotpi.test.blocks.Block;
import com.pieisnotpi.test.blocks.Metal;
import com.pieisnotpi.test.cameras.FirstPersonCamera;
import com.pieisnotpi.test.fonts.PixelFont;
import org.joml.*;

import java.io.InvalidObjectException;
import java.lang.Math;
import java.util.ArrayList;

public class TestScene2 extends PauseScene
{
    private static final int w = 100, h = 100;
    private GameObject oBlocks, oArrow;
    private int[] view = new int[] {0, 0, 1, 1};
    private final Vector3f arrowPos = new Vector3f(0, 2, 0);
    private Vector4f arrowPostMul = new Vector4f();

    public TestScene2 init() throws Exception
    {
        super.init();

        name = "Test Scene 2";

        Camera c = new FirstPersonCamera(new Vector3f(0, 2, 10), 70, 0, new Vector2f(0, 0), new Vector2f(1, 1));

        addCamera(c);
        /*addCamera(new Camera(new Vector3f(-2, 2, -2), 90, new Vector2f(0.5f, 0), new Vector2f(0.5f, 0.5f)));
        addCamera(new Camera(new Vector3f(-2, 2, 0), 90, new Vector2f(0, 0.5f), new Vector2f(0.5f, 0.5f)));
        addCamera(new Camera(new Vector3f(2, 2, 0), 90, new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f)));*/

        clearColor.set(0.918f, 0.729f, 0.125f);

        float xOffset = -(w/2f)*Block.SIZE, zOffset = -(h/2f)*Block.SIZE;

        ADSMaterial blockMaterial = new ADSMaterial(new Vector4f(0.25f), new Vector4f(0.2f), new Vector4f(0.5f), 1, Camera.PERSP, Texture.getTextureFile("metal"));
        Mesh<Quad> blocksMesh = new Mesh<Quad>(blockMaterial, MeshConfig.QUAD_STATIC).setPrimitives(new ArrayList<>(w*h*4));
        oBlocks = new GameObject(new Renderable(0, 0, new Transform(), blocksMesh));

        Block block;

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                block = new Metal(xOffset, 0, zOffset += Block.SIZE);
                if(x < w - 1) block.cubes.get(0).sides[2].enabled = false;
                if(x > 0) block.cubes.get(0).sides[1].enabled = false;
                if(y < h - 1) block.cubes.get(0).sides[3].enabled = false;
                if(y > 0) block.cubes.get(0).sides[0].enabled = false;

                block.cubes.forEach(cube ->
                {
                    for(Quad side : cube.sides) if(side.enabled) blocksMesh.addPrimitive(side);
                });
            }

            zOffset = -(h/2f)*Block.SIZE;
            xOffset += Block.SIZE;
        }

        blocksMesh.build();
        addGameObject(oBlocks);
        /*GameObject object = new GameObject();
        object.setRenderable(new Renderable(0, 0, object.getTransform(), "/assets/models/Wooden well.FBX"));
        addGameObject(object);*/

        /*Mesh<Quad> blocksMesh2 = new Mesh<Quad>(blocksMesh, new Transform().rotateDegrees(90, 0, 0), this).register();
        Mesh<Quad> blocksMesh3 = new Mesh<Quad>(blocksMesh, new Transform().rotateDegrees(0, 0, 90), this).register();*/

        Mesh<Quad> arrow = new Mesh<>(new TexMaterial(Camera.PERSP, Texture.getTextureFile("arrow.png")), MeshConfig.QUAD_STATIC);
        oArrow = new GameObject();
        oArrow.createRenderable(0, 0, arrow);
        Sprite t = new Sprite(0f, 0f, 1f, 1f);
        TexQuad t0 = new TexQuad(-0.5f, 0, -0.5f, 1, 0, 1, t), t1 = new TexQuad(0, -0.5f, -0.5f, 0, 1, 1, t);
        MathUtility.rotateAxisY(90, 0, 0, t0.points);
        arrow.addPrimitive(t0).addPrimitive(t1);
        arrow.build();
        oArrow.getTransform().translate(0, 2, 0);
        addGameObject(oArrow);

        String t3dt = "This is a reaaaaalllly long line of text.\nIt's for testing stuff.", t3d2t = "I.\nLike.\nTests.";
        SystemFont font = SystemFont.getFont("Arial", 96, SystemFont.PLAIN, true);

        UnfoldEffect e = new UnfoldEffect(10);

        Text text3D = new Text(font, t3dt, new Vector3f(0, 1.5f, 0), Camera.PERSP, new RainbowEffect(0.6f), e, new WaveEffect(0.1f, 0.1f, 1f));
        text3D.setOutlineSize(4);
        text3D.setOutlineSmoothing(true);
        oBlocks.addChild(text3D);
        text3D.getTransform().setTranslate(-4, 2, 0).setScale(0.01f, 0.01f, 0.01f);

        Text text3D2 = new Text(PixelFont.getFont(), t3d2t, new Vector3f(0, 1.5f, 0.025f), new Color(0, 1, 0), new Color(0, 0, 0), Camera.PERSP, new WaveEffect(4, 0.1f, 5));
        text3D2.setOutlineSize(1);
        text3D2.setOutlineSmoothing(false);
        oBlocks.addChild(text3D2);
        text3D2.getTransform().setTranslate(0, -2, 0).setScale(0.05f, 0.05f, 0.05f);

        lights.add(new ADSPointLight(new Vector3f(10, 2, 10), new Vector3f(1.25f, 0, 0), 0, this));
        lights.add(new ADSPointLight(new Vector3f(10, 2, -10), new Vector3f(0, 1.25f, 0), 1, this));
        lights.add(new ADSPointLight(new Vector3f(-10, 2, 10), new Vector3f(0, 0, 1.25f), 2, this));
        lights.add(new ADSPointLight(new Vector3f(-10, 2, -10), new Vector3f(0.8f), 3, this));

        addKeybind(new Keybind(Keyboard.KEY_ENTER, () -> { throw new InvalidObjectException("Invalid key pressed"); }, null, null));
        addKeybind(new Keybind(Keyboard.KEY_Q, e::start, null, null));
        addKeybind(new Keybind(Keyboard.KEY_E, e::end, null, null));
        addKeybind(new Keybind(Keyboard.KEY_T, () -> removeGameObject(text3D)/*text3D.setOutlineSize(0)*/, null, null));

        return this;
    }

    @Override
    public void drawUpdate(float timeStep) throws Exception
    {
        super.drawUpdate(timeStep);
        //cameras.get(0).getTransform().rotateDegrees(0, 0.1f, 0);
    }

    @Override
    public void onWindowResize(Vector2i res)
    {
        super.onWindowResize(res);
        view[2] = res.x;
        view[3] = res.y;
    }
    
    @Override
    public void onMouseMovement(Vector2f scaled, Vector2i unscaled)
    {
        super.onMouseMovement(scaled, unscaled);
    
        Camera c = cameras.get(0);
        Matrix4f matrix = c.getMatrix(Camera.PERSP).getMatrix();
        arrowPostMul = matrix.project(arrowPos, view, arrowPostMul.set(0, 0, 0, 1));
        float cx = unscaled.x - arrowPostMul.x, cy = unscaled.y - arrowPostMul.y;
    
        if(cx > -0.001 && cx < 0.001) cx = 0;
        if(cy > -0.001 && cy < 0.001) cy = 0;
    
        double r = Math.atan2(cy, cx), cr = c.getTransform().rotRad.y;
        float rz = (float) Math.toDegrees(r*Math.cos(cr)), ry = (float) Math.toDegrees(r*Math.sin(cr));
    
        oArrow.getTransform().setRotateDegrees(90, ry, rz - 90);
    }
}
