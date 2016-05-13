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

public class PhysicsObject extends GameObject
{
    public Body body;
    public BodyDef def;
    public Fixture fixture;
    public Shape shape;

    public void init(float x, float y, float density, boolean shouldSleep, boolean shouldRotate, BodyType bodyType, Shape shape)
    {
        def = new BodyDef();
        def.type = bodyType;
        def.position.set(toPhysicsCoord(x), toPhysicsCoord(y));
        def.fixedRotation = !shouldRotate;
        def.allowSleep = shouldSleep;

        this.shape = shape;

        body = scene.world.createBody(def);
        fixture = body.createFixture(shape, density);
    }

    public void setX(float x)
    {
        this.x = x;
        body.getPosition().x = toPhysicsCoord(x);
    }

    public void setY(float y)
    {
        this.y = y;
        body.getPosition().y = toPhysicsCoord(y);
    }

    /**
     * @param val A coordinate, in render coordinates
     * @return val, in physics coordinates
     */

    protected float toPhysicsCoord(float val)
    {
        return val/PiEngine.PIXELS_PER_METER;
    }

    /**
     * @param val A coordinate, in physics coordinates
     * @return val, in render coordinates
     */

    protected float toRenderCoord(float val)
    {
        return val*PiEngine.PIXELS_PER_METER;
    }
}
