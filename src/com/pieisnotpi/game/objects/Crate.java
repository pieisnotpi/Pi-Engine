package com.pieisnotpi.game.objects;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.PhysicsObject;
import com.pieisnotpi.engine.rendering.shapes.types.textured.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyType;

public class Crate extends PhysicsObject
{
    protected TexQuad quad;

    public final static float scale = 0.1f;
    private static Sprite sprite = new Sprite(Texture.getTexture("crate"), 0, 0, 16, 16);

    public Crate(float x, float y, float z, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        width = scale;
        height = scale;

        this.matrixID = PiEngine.CAMERA_2D_ID;
        this.scene = scene;

        PolygonShape t = new PolygonShape();
        t.setAsBox(toPhysicsCoord(scale)/2, toPhysicsCoord(scale)/2);

        init(x, y, 1, BodyType.DYNAMIC, t);

        fixture.setFriction(1.5f);
        fixture.setRestitution(0);

        quad = new TexQuad(x, y, z, scale, scale, 0, sprite, matrixID, scene);

        scene.gameObjects.add(this);
        body.setSleepingAllowed(true);

        setCenter(x + width/2, y + height/2, z);
    }

    public void physicsUpdate()
    {
        x = toRenderCoord(body.getPosition().x);
        y = toRenderCoord(body.getPosition().y);

        float angle = (float) Math.toDegrees(body.getAngle());

        if(angle < -0.1f || angle > 0.1f)
        {
            quad.setZRot(0, quad.getCenterX(), quad.getCenterY());
            quad.setX(x);
            quad.setY(y);
            quad.setZRot(angle, quad.getCenterX(), quad.getCenterY());
        }
        else
        {
            quad.setX(x);
            quad.setY(y);
        }
    }

    public void setX(float x)
    {
        super.setX(x);
        quad.setX(x);
    }

    public void setY(float y)
    {
        super.setY(y);
        quad.setY(y);
    }

    public void destroy()
    {
        super.destroy();
        quad.unregister();
    }
}
