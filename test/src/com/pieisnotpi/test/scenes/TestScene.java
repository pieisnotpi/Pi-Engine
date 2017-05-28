package com.pieisnotpi.test.scenes;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.mesh.Transform;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexMaterial;
import com.pieisnotpi.engine.rendering.shapes.Quad;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.ui.text.Text;
import com.pieisnotpi.engine.ui.text.effects.WaveEffect;
import com.pieisnotpi.engine.utility.Color;
import com.pieisnotpi.test.blocks.Block;
import com.pieisnotpi.test.blocks.Grass;
import com.pieisnotpi.test.cameras.FirstPersonCamera;
import com.pieisnotpi.test.fonts.PixelFont;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class TestScene extends PauseScene
{
    private static final int w = 100, h = 100;
    private Mesh<Quad> blocksMesh;

    public TestScene init() throws Exception
    {
        super.init();

        name = "Test Scene 1";

        cameras.add(new FirstPersonCamera(90, 0, new Vector2f(0, 0), new Vector2f(1, 1)));
        clearColor.set(0.125f, 0.729f, 0.918f);

        float xOffset = -(w/2f)*Block.SIZE, zOffset = -(h/2f)*Block.SIZE;

        Block block;

        TexMaterial blocksMaterial = new TexMaterial(Camera.PERSP, Texture.getTextureFile("grass"));
        blocksMesh = new Mesh<Quad>(blocksMaterial, new Transform(), MeshConfig.QUAD_STATIC).setRenderables(new ArrayList<>(w*h*4));
        GameObject grass = new GameObject<>(blocksMesh);

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                block = new Grass(xOffset, 0, zOffset += Block.SIZE);
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

        blocksMesh.getTransform().setRotateDegrees(0, 90, 0).scale(0.001f);
        addGameObject(grass);

        String t3dt = "This text is waving.", t3d2t = "This text is hovering.";

        Text text3D = new Text(PixelFont.getFont(), t3dt, new Vector3f(0, 1.5f, 0.5f), new Color(1, 0, 0), new Color(0, 0, 0), Camera.PERSP, new WaveEffect(4, 2, 5));
        text3D.getTransform().setScale(0.1f, 0.1f, 1).translate(-text3D.getWidth(), 0, 0);
        Text text3D2 = new Text(PixelFont.getFont(), t3d2t, new Vector3f(0, 1, 0.45f), new Color(0, 1, 0), new Color(0, 0, 0), Camera.PERSP, new WaveEffect(4, 0, 5));
        text3D2.getTransform().setScale(0.1f, 0.1f, 1).translate(-text3D2.getWidth(), 0, 0);

        return this;
    }
}
