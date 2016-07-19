package com.pieisnotpi.game.objects;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyType;

public class FloorTile extends PhysicsObject
{
    protected TexQuad quad;

    public final static float scale = 0.1f;
    private static final Sprite sprite = new Sprite(Texture.getTexture("tiles"), 16, 32, 32, 16);

    public FloorTile(float x, float y, float z, Scene scene)
    {
        pos.set(x, y, z);
        size.set(scale, scale, 0);

        this.matrixID = PiEngine.C_ORTHO;
        this.scene = scene;

        PolygonShape t = new PolygonShape();
        t.setAsBox(toPhysicsCoord(scale)/2, toPhysicsCoord(scale)/2);

        init(x, y, 1, BodyType.STATIC, t);

        quad = new TexQuad(x + calcOffset(scale/2), y + calcOffset(scale/2), z, scale, scale, z, sprite, matrixID, scene);

        scene.gameObjects.add(this);

        fixture.setFriction(2);
        body.setSleepingAllowed(true);

        defaultCenter();
    }

    public void destroy()
    {
        super.destroy();
        quad.unregister();
    }
}
