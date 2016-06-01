package com.pieisnotpi.game.objects;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.PhysicsObject;
import com.pieisnotpi.engine.rendering.shapes.types.textured.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyType;

public class Truck extends PhysicsObject
{
    protected TexQuad quad;

    public final static float width = 5/10f, height = 3/10f;
    static Sprite sprite = new Sprite(Texture.getTexture("truck"), 0, 48, 80, 0);

    public Truck(float x, float y, float z, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.matrixID = PiEngine.CAMERA_2D_ID;
        this.scene = scene;

        super.width = width;
        super.height = height;

        PolygonShape t = new PolygonShape();
        t.setAsBox(toPhysicsCoord(width)/2, toPhysicsCoord(height)/2);

        init(x, y, 4, BodyType.DYNAMIC, t);

        fixture.setFriction(2);
        fixture.setRestitution(0);

        quad = new TexQuad(x, y, z, width, height, 0, sprite, matrixID, scene);

        scene.gameObjects.add(this);

        defaultCenter();
    }

    public void physicsUpdate()
    {
        x = toRenderCoord(body.getPosition().x, width/2);
        y = toRenderCoord(body.getPosition().y, height/2);

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
