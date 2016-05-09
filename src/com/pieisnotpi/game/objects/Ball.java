package com.pieisnotpi.game.objects;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.GameObject;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shapes.types.colored.ColorCircle;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector3f;

public class Ball extends GameObject
{
    protected ColorCircle circle;
    //protected TexCircle circle;
    public Body body;
    protected BodyDef def;
    protected Fixture fixture;
    protected CircleShape shape;

    public final static float radius = 0.05f;
    static Sprite sprite = new Sprite(Texture.getTexture("crate"), 0, 0, 16, 16);

    public Ball(float x, float y, float z, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;

        this.matrixID = PiEngine.CAMERA_2D_ID;
        this.scene = scene;

        def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.position.set(x*PiEngine.PIXELS_PER_METER, y*PiEngine.PIXELS_PER_METER);

        shape = new CircleShape();
        shape.setRadius(radius*PiEngine.PIXELS_PER_METER);

        body = scene.world.createBody(def);
        fixture = body.createFixture(shape, 1);
        body.setGravityScale(2);

        //circle = new TexCircle(x, y, z, radius, 100, sprite, matrixID, scene);
        circle = new ColorCircle(x, y, z, radius, 16, new Color(1, 0.2f, 0.2f), matrixID, scene);

        scene.gameObjects.add(this);
    }

    public void physicsUpdate()
    {
        x = body.getPosition().x/PiEngine.PIXELS_PER_METER;
        y = body.getPosition().y/PiEngine.PIXELS_PER_METER;

        Vector3f center = new Vector3f(circle.getX(), circle.getY(), circle.getZ());

        float angle = (float) Math.toDegrees(body.getAngle());

        //if((angle > 0.1 || angle < -0.1) && angle != quad.zRot) quad.rotateZ((float) Math.toDegrees(body.getAngle()) - quad.zRot, center.x, center.y);

        circle.setX(x);
        circle.setY(y);
    }

    public void setX(float x)
    {
        circle.setX(x);
        def.position.set(x*PiEngine.PIXELS_PER_METER, def.position.y);
    }

    public void setY(float y)
    {
        circle.setY(y);
        def.position.set(def.position.x, y*PiEngine.PIXELS_PER_METER);
    }

    /*private float getCenterX()
    {
        float cx = 0;
        for(Vector3f point : quad.points) cx += point.x;
        return cx/4;
    }

    private float getCenterY()
    {
        float cy = 0;
        for(Vector3f point : quad.points) cy += point.y;
        return cy/4;
    }*/
}
