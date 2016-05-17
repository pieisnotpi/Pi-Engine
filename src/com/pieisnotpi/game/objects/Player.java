package com.pieisnotpi.game.objects;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.PhysicsObject;
import com.pieisnotpi.engine.input.Joybind;
import com.pieisnotpi.engine.input.joysticks.DS4;
import com.pieisnotpi.engine.input.joysticks.Joystick;
import com.pieisnotpi.engine.input.joysticks.Xbox;
import com.pieisnotpi.engine.rendering.shapes.types.textured.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.utility.Timer;
import com.pieisnotpi.game.scenes.PhysicsTestScene;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyType;

import java.util.ArrayList;
import java.util.List;

/**
 * A player test class, only used in the physics test scene. Summoned with left shift.
 * Controls are determined based on the name of the joystick, compatible with the DS4 controller and Xbox 360/One controllers.
 */

public class Player extends PhysicsObject
{
    public List<Joybind> joybinds = new ArrayList<>();
    private String lastJoyName = "";
    private PhysicsTestScene s;
    private int joystick, jumpTimer = 0;
    protected TexQuad quad;

    private static final int maxJumpTimer = 4;
    public final static float scale = 0.1f, moveAmount = 2, maxSpeed = 20, jumpAmount = 5, maxJumpSpeed = 20, force = 16;
    private static Sprite sprite = new Sprite(Texture.getTexture("crate"), 0, 0, 16, 16);

    private Timer timer = new Timer(true, 2000);

    public Player(float x, float y, float z, int joystick, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.joystick = joystick;

        this.matrixID = PiEngine.CAMERA_2D_ID;
        this.scene = scene;
        s = (PhysicsTestScene) scene;

        PolygonShape t = new PolygonShape();
        t.setAsBox(toPhysicsCoord(scale)/2, toPhysicsCoord(scale)/2);

        init(x, y, 1, BodyType.DYNAMIC, t);

        fixture.setFriction(0.8f);
        fixture.setRestitution(0);
        body.setGravityScale(2);

        quad = new TexQuad(x, y, z, scale, scale, 0, sprite, matrixID, scene);

        scene.gameObjects.add(this);

        body.setSleepingAllowed(false);
    }

    public void updateBindings(Joystick joy)
    {
        lastJoyName = joy.name;

        unregisterInputs();
        joybinds.clear();

        if(lastJoyName.contains("Xbox"))
        {
            joybinds.add(new Joybind(joy.joystick, Xbox.AXIS_LSTICK_X, false, true, this::move, null));
            joybinds.add(new Joybind(joy.joystick, Xbox.BUTTON_A, true, true, (value) -> jump(), (value) -> jumpTimer = 0));
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_B, true, false, (value) -> body.setGravityScale(-body.getGravityScale()), null));
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_X, true, false, (value) -> force(), null));
        }
        else
        {
            joybinds.add(new Joybind(joy.joystick, DS4.AXIS_LSTICK_X, false, true, this::move, null));
            joybinds.add(new Joybind(joy.joystick, DS4.BUTTON_X, true, true, (value) -> jump(), (value) -> jumpTimer = 0));
            joybinds.add(new Joybind(joystick, DS4.BUTTON_CIRCLE, true, false, (value) -> body.setGravityScale(-body.getGravityScale()), null));
            joybinds.add(new Joybind(joystick, DS4.BUTTON_SQUARE, true, false, (value) -> force(), null));
        }

        registerInputs();
    }

    public void update()
    {
        if(scene.window != null)
        {
            Joystick joy = scene.window.inputManager.joysticks[joystick];
            if(joy != null && !joy.name.contentEquals(lastJoyName)) updateBindings(joy);
        }
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

    public void registerInputs()
    {
        for(Joybind joybind : joybinds) scene.addJoybind(joybind);
    }

    public void unregisterInputs()
    {
        for(Joybind joybind : joybinds) scene.removeJoybind(joybind);
    }

    public void destroy()
    {
        super.destroy();
        quad.unregister();
        unregisterInputs();
    }

    private void move(float amount)
    {
        if((amount < 0 && body.getLinearVelocity().x > -maxSpeed) || (amount > 0 && body.getLinearVelocity().x < maxSpeed))
            body.getLinearVelocity().x += moveAmount*amount;
    }

    private void jump()
    {
        if(jumpTimer++ < maxJumpTimer)
        {
            float v = body.getLinearVelocity().y;

            if(body.getGravityScale() < 0)
            {
                if(v < 0) body.getLinearVelocity().y = 0;

                if(v > maxJumpSpeed) body.getLinearVelocity().y = maxJumpSpeed;
                else body.getLinearVelocity().y += jumpAmount;
            }
            else
            {
                if(v > 0) body.getLinearVelocity().y = 0;

                if(v < -maxJumpSpeed) body.getLinearVelocity().y = -maxJumpSpeed;
                else body.getLinearVelocity().y -= jumpAmount;
            }
        }
    }

    private void force()
    {
        boolean appliedForce = false;

        for(Player player : s.players)
        {
            if((timer.isStarted() && !timer.isFinished()) || player.equals(this)) continue;

            float xDif = x - player.x, yDif = y - player.y, dist = (float) Math.sqrt(xDif*xDif + yDif*yDif);

            if(dist > 0.5f) continue;

            appliedForce = true;

            if(xDif < 0) player.body.getLinearVelocity().x += force/dist;
            else player.body.getLinearVelocity().x -= force/dist;

            if(yDif < 0) player.body.getLinearVelocity().y += force/dist;
            else player.body.getLinearVelocity().y -= force/dist;

            timer.start();
        }

        if(!appliedForce) timer.forceFinish();
    }
}
