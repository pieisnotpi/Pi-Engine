package com.pieisnotpi.test.scenes;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.ads_shader.ADSMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.ads_shader.ADSPointLight;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexQuad;
import com.pieisnotpi.engine.rendering.shapes.Quad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.ui.text.Text;
import com.pieisnotpi.engine.ui.text.effects.RainbowEffect;
import com.pieisnotpi.engine.ui.text.effects.WaveEffect;
import com.pieisnotpi.engine.ui.text.font.SystemFont;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.engine.utility.MathUtility;
import com.pieisnotpi.test.blocks.Block;
import com.pieisnotpi.test.blocks.Metal;
import com.pieisnotpi.test.cameras.FirstPersonCamera;
import com.pieisnotpi.test.fonts.PixelFont;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;

public class TestScene2 extends PauseScene
{
    private static final int w = 100, h = 100;

    private float timer = 0;
    private GameObject<Quad> oBlocks, oArrow;
    private float x = 0.5f, y = 0;
    //private Mesh<Quad> blocksMesh;
    //private Mesh<TexQuad> arrow;
    
    Camera c;

    public TestScene2 init()
    {
        super.init();

        name = "Test Scene 2";

        //addCamera(new Camera(1, new Vector2f(0, 0), new Vector2f(1, 1), this));
        addCamera(c = new FirstPersonCamera(new Vector3f(0, 2, 0), 90, 0, new Vector2f(0, 0), new Vector2f(1, 1)));
        /*addCamera(new FirstPersonCamera(new Vector3f(0, 2, 0), 90, 0, new Vector2f(0, 0), new Vector2f(0.5f, 0.5f), this));
        addCamera(new Camera(new Vector3f(-2, 2, -2), 90, new Vector2f(0.5f, 0), new Vector2f(0.5f, 0.5f), this));
        addCamera(new Camera(new Vector3f(-2, 2, 0), 90, new Vector2f(0, 0.5f), new Vector2f(0.5f, 0.5f), this));
        addCamera(new Camera(new Vector3f(2, 2, 0), 90, new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f), this));*/

        //c.getTransform().setRotateDegrees(15, 45, 0);

        clearColor.set(0.918f, 0.729f, 0.125f);

        float xOffset = -(w/2f)*Block.SIZE, zOffset = -(h/2f)*Block.SIZE;

        ADSMaterial blockMaterial = new ADSMaterial(new Vector3f(0.25f), new Vector3f(0.2f), new Vector3f(0.1f), 1, Camera.PERSP, Texture.getTextureFile("metal"));
        oBlocks = new GameObject<>();
        Mesh<Quad> blocksMesh = oBlocks.createMesh(blockMaterial, MeshConfig.QUAD_STATIC).setRenderables(new ArrayList<>(w*h*4));

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
                    for(Quad side : cube.sides) if(side.enabled) blocksMesh.addRenderable(side);
                });
            }

            zOffset = -(h/2f)*Block.SIZE;
            xOffset += Block.SIZE;
        }

        blocksMesh.build();
        addGameObject(oBlocks);

        //Mesh<Quad> blocksMesh2 = new Mesh<Quad>(blocksMesh, new Transform().rotateDegrees(90, 0, 0), this).register();
        //Mesh<Quad> blocksMesh3 = new Mesh<Quad>(blocksMesh, new Transform().rotateDegrees(0, 0, 90), this).register();

        oArrow = new GameObject<>();
        Mesh<Quad> arrow = oArrow.createMesh(new TexMaterial(Camera.PERSP, Texture.getTextureFile("arrow.png")), MeshConfig.QUAD_STATIC);
        Sprite t = new Sprite(0f, 0f, 1f, 1f);
        TexQuad t0 = new TexQuad(-0.5f, 0, -0.5f, 1, 0, 1, t), t1 = new TexQuad(0, -0.5f, -0.5f, 0, 1, 1, t);
        MathUtility.rotateAxisY(90, 0, 0, t0.points);
        arrow.addRenderable(t0).addRenderable(t1);
        arrow.build();
        arrow.getTransform().translate(0, 2, 0);
        addGameObject(oArrow);

        String t3dt = "This is a reaaaaalllly long line of text. It's for testing stuff.", t3d2t = "I.\nLike.\nTests.";
        SystemFont font = SystemFont.getFont("Arial", 48, SystemFont.PLAIN, true);

        Text text3D = new Text(font, t3dt, new Vector3f(0, 1.5f, 0), new Color(1, 0, 0), new Color(0, 0, 0), Camera.PERSP, new RainbowEffect(0.6f));
        addGameObject(text3D);
        text3D.setParent(oBlocks);
        text3D.getTransform().setTranslate(-4, 2, 0).setScale(0.01f, 0.01f, 0.01f);

        Text text3D2 = new Text(PixelFont.getFont(), t3d2t, new Vector3f(0, 1.5f, 0.025f), new Color(0, 1, 0), new Color(0, 0, 0), Camera.PERSP, new WaveEffect(4, 0.1f, 5));
        addGameObject(text3D2);
        text3D2.setOutlineSize(3);
        text3D2.setParent(oBlocks);
        text3D2.getTransform().setTranslate(0, -2, 0).setScale(0.05f, 0.05f, 0.05f);

        lights.add(new ADSPointLight(new Vector3f(10, 2, 10), new Vector3f(1.25f, 0, 0), 0, this));
        lights.add(new ADSPointLight(new Vector3f(10, 2, -10), new Vector3f(0, 1.25f, 0), 1, this));
        lights.add(new ADSPointLight(new Vector3f(-10, 2, 10), new Vector3f(0, 0, 1.25f), 2, this));
        lights.add(new ADSPointLight(new Vector3f(-10, 2, -10), new Vector3f(0.8f), 3, this));

        return this;
    }
    
    @Override
    public void onMouseMovement(Vector2f scaled, Vector2i unscaled)
    {
        super.onMouseMovement(scaled, unscaled);
    
        Vector2i res = window.getWindowRes();
        float cx = (float) (unscaled.x*2 - res.x), cy = (float) (unscaled.y*2 - res.y);
    
        if(cx > -0.001 && cx < 0.001) cx = 0;
        if(cy > -0.001 && cy < 0.001) cy = 0;
    
        float nx = cy*0.05f, ny = -cx*0.05f;
    
        oArrow.getTransform().setRotateDegrees(nx, ny, 0);
    }
    
    @Override
    public void update(float timeStep)
    {
        super.update(timeStep);
        
        //c.getTransform().setRotateDegrees((float) Math.sin(y += 0.005f)*-45, (float) Math.sin(x += 0.005f)*45, 0);
    }
}
