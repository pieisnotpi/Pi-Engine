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
    static Sprite sprite = new Sprite(Texture.getTexture("truck"), 0, 62, 12, 50);

    public Wheel(float x, float y, float z, Scene scene)
    {
        pos.set(x, y, z);
        size.set(radius*2, radius*2, 0);

        this.matrixID = PiEngine.C_ORTHO;
        this.scene = scene;

        CircleShape t = new CircleShape();
        t.setRadius(toPhysicsCoord(radius));

        init(x, y, 2, BodyType.DYNAMIC, t);

        fixture.setFriction(2);
        fixture.setRestitution(0);

        quad = new TexQuad(x, y, z, radius*2, radius*2, 0, sprite, matrixID, scene);

        scene.gameObjects.add(this);

        defaultCenter();
    }

    public void drawUpdate()
    {
        pos.x = toRenderCoord(body.getPosition().x, radius);
        pos.y = toRenderCoord(body.getPosition().y, radius);

        float angle = (float) Math.toDegrees(body.getAngle());

        if(angle < -0.1f || angle > 0.1f)
        {
            quad.setZRot(0, quad.getCenterX(), quad.getCenterY());
            quad.setX(pos.x);
            quad.setY(pos.y);
            quad.setZRot(angle, quad.getCenterX(), quad.getCenterY());
        }
        else
        {
            quad.setX(pos.x);
            quad.setY(pos.y);
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
