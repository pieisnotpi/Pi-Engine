package com.pieisnotpi.game.objects;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.PhysicsObject;
import com.pieisnotpi.engine.rendering.shapes.types.textured.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyType;

public class Wheel extends PhysicsObject
{
    protected TexQuad quad;

    public final static float radius = 0.05f;
    static Sprite sprite = new Sprite(Texture.getTexture("truck"), 0, 62, 12, 50, false, true);

    public Wheel(float x, float y, float z, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.matrixID = PiEngine.CAMERA_2D_ID;
        this.scene = scene;

        width = radius*2;
        height = radius*2;

        CircleShape t = new CircleShape();
        t.setRadius(toPhysicsCoord(radius));

        init(x, y, 2, BodyType.DYNAMIC, t);

        fixture.setFriction(2);
        fixture.setRestitution(0);

        quad = new TexQuad(x, y, z, radius*2, radius*2, 0, sprite, matrixID, scene);

        scene.gameObjects.add(this);

        setCenter(x + radius, y + radius, z);
    }

    public void drawUpdate()
    {
        x = toRenderCoord(body.getPosition().x, radius);
        y = toRenderCoord(body.getPosition().y, radius);

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

    public void physicsUpdate()
    {
        body.applyAngularImpulse(8);
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
