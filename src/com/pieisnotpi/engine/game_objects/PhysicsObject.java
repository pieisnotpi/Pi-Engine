package com.pieisnotpi.engine.game_objects;

import com.pieisnotpi.engine.PiEngine;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;

/**
 * A GameObject that implements the necessary parts for use with Jbox2D
 */

public abstract class PhysicsObject extends GameObject
{
    public Body body;
    public BodyDef def;
    public Fixture fixture;
    public Shape shape;

    public boolean bodyAlive = false;

    public static final float offset = toRenderCoord(1);

    public void init(float x, float y, float density, BodyType bodyType, Shape shape)
    {
        def = new BodyDef();
        def.type = bodyType;
        def.position.set(toPhysicsCoord(x), toPhysicsCoord(y));

        this.shape = shape;

        body = scene.world.createBody(def);
        fixture = body.createFixture(shape, density);

        bodyAlive = true;
    }

    public void setX(float x)
    {
        pos.x = x;
        body.getPosition().x = toPhysicsCoord(x);
    }

    public void setY(float y)
    {
        pos.y = y;
        body.getPosition().y = toPhysicsCoord(y);
    }

    /**
     * @param val A coordinate, in render coordinates
     * @return val, in physics coordinates
     */

    public static float toPhysicsCoord(float val)
    {
        return val/PiEngine.PIXELS_PER_METER;
    }

    /**
     * @param val A coordinate, in physics coordinates
     * @return val, in render coordinates
     */

    public static float toRenderCoord(float val)
    {
        return val*PiEngine.PIXELS_PER_METER;
    }

    public static float toRenderCoord(float val, float radius)
    {
        return val*PiEngine.PIXELS_PER_METER + offset - radius;
    }

    public static float calcOffset(float radius)
    {
        return offset - radius;
    }

    public void destroy()
    {
        super.destroy();
        if(scene.world.getBodyCount() > 0 && bodyAlive)
        {
            scene.world.destroyBody(body);
            bodyAlive = false;
        }

    }
}
