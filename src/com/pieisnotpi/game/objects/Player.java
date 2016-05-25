package com.pieisnotpi.game.objects;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.PhysicsObject;
import com.pieisnotpi.engine.input.Joybind;
import com.pieisnotpi.engine.input.joysticks.DS4;
import com.pieisnotpi.engine.input.joysticks.Joystick;
import com.pieisnotpi.engine.input.joysticks.Xbox;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shapes.types.textured.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.rendering.ui.text.Text;
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
    public int joystick;
    private int jumpTimer = 0;
    private float moveSpeedX = 0, moveSpeedY = 0, jumpSpeedX = 0, jumpSpeedY = 0;
    protected TexQuad quad;

    private static final int maxJumpTimer = 10;
    private static final float moveAmount = 2, maxSpeed = 10, jumpAmount = 2, maxJumpSpeed = 20, force = 3, gravity = 2;
    public final static float scale = 0.1f;
    private static Sprite sprite = new Sprite(Texture.getTexture("crate"), 0, 0, 16, 16);

    private Timer timer = new Timer(true, 1500);
    private Text number;
    private final float xOffset = scale/4, yOffset = scale + 0.001f;

    public Player(float x, float y, float z, int joystick, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.joystick = joystick;
        this.matrixID = PiEngine.CAMERA_2D_ID;
        this.scene = scene;

        width = scale;
        height = scale;

        s = (PhysicsTestScene) scene;

        PolygonShape t = new PolygonShape();
        t.setAsBox(toPhysicsCoord(scale)/2, toPhysicsCoord(scale)/2);

        init(x, y, 1, BodyType.DYNAMIC, t);

        fixture.setFriction(0.8f);
        fixture.setRestitution(0);
        body.setGravityScale(gravity);

        quad = new TexQuad(x, y, z, scale, scale, 0, sprite, matrixID, scene);
        quad.transparent = false;

        Color numColor = new Color(0, 0, 0);

        if(joystick == 0) numColor.set(1, 0, 0);
        else if(joystick == 1) numColor.set(0, 1, 0);
        else if(joystick == 2) numColor.set(0, 0, 1);

        number = new Text((joystick + 1) + "", 6, x + xOffset, y + yOffset, z, numColor, new Color(0, 0, 0, 0), matrixID, scene);

        scene.gameObjects.add(this);

        body.setSleepingAllowed(false);

        setCenter(x + width/2, y + height/2, z);
    }

    public void updateBindings(Joystick joy)
    {
        lastJoyName = joy.name;

        unregisterInputs();
        joybinds.clear();

        if(lastJoyName.contains("Xbox"))
        {
            joybinds.add(new Joybind(joystick, Xbox.AXIS_LSTICK_Y, false, true, this::shiftGravity, null));
            joybinds.add(new Joybind(joystick, Xbox.AXIS_LSTICK_X, false, true, this::move, null));
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_A, true, true, (value) -> jump(), (value) -> jumpTimer = -1));
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_X, true, false, (value) -> force(), null));
        }
        else
        {
            joybinds.add(new Joybind(joystick, DS4.AXIS_LSTICK_Y, false, true, this::shiftGravity, null));
            joybinds.add(new Joybind(joystick, DS4.AXIS_LSTICK_X, false, true, this::move, null));
            joybinds.add(new Joybind(joystick, DS4.BUTTON_X, true, true, (value) -> jump(), (value) -> jumpTimer = -1));
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

    public void drawUpdate()
    {
        x = toRenderCoord(body.getPosition().x, scale/2);
        y = toRenderCoord(body.getPosition().y, scale/2);

        number.setX(x + xOffset);
        number.setY(y + yOffset);

        float angle = (float) Math.toDegrees(body.getAngle());

        if(angle < -0.1f || angle > 0.1f)
        {
            quad.setZRot(0, cx, cy);
            quad.setX(x);
            quad.setY(y);
            quad.setZRot(angle, cx, cy);
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
        number.destroy();
        unregisterInputs();
    }

    private void flipGravity()
    {
        body.setGravityScale(-body.getGravityScale());
    }

    private void move(float amount)
    {
        double a = Math.toRadians(s.camera.getZRot());

        float cos = (float) Math.cos(a), sin = (float) Math.sin(a), dif = moveAmount*amount, xSpeed = body.getLinearVelocity().y*sin + body.getLinearVelocity().x*cos;

        if((dif < 0 && dif + xSpeed > -maxSpeed) || (dif > 0 && dif + xSpeed < maxSpeed)) body.getLinearVelocity().addLocal(cos*dif, sin*dif);
    }

    private void jump()
    {
        if(jumpTimer++ < maxJumpTimer)
        {
            float rot = s.camera.getZRot();
            double a = Math.toRadians(rot);

            float cos = (float) Math.cos(a), sin = (float) Math.sin(a), dif = jumpAmount, ySpeed = -body.getLinearVelocity().x*sin + body.getLinearVelocity().y*cos;

            if(body.getGravityScale() > 0 && ySpeed < maxJumpSpeed)
            {
                if(ySpeed < 0) body.getLinearVelocity().addLocal(ySpeed*sin, -ySpeed*cos);
                body.getLinearVelocity().addLocal(-sin*dif, cos*dif);
            }
            else if(body.getGravityScale() < 0 && ySpeed > -maxJumpSpeed)
            {
                if(ySpeed > 0) body.getLinearVelocity().addLocal(-ySpeed*sin, ySpeed*cos);
                body.getLinearVelocity().addLocal(sin*dif, -cos*dif);
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

    public void shiftGravity(float amount)
    {
        /*if(body.getGravityScale() < 0) body.setGravityScale(-gravity - amount*1.5f);
        else body.setGravityScale(gravity - amount*1.5f);*/
    }
}
