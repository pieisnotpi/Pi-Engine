package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.input.Joybind;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.input.joysticks.DS4;
import com.pieisnotpi.engine.input.joysticks.Joystick;
import com.pieisnotpi.engine.input.joysticks.Xbox;
import com.pieisnotpi.engine.rendering.shapes.Triangle;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.utility.MathUtility;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Camera
{
    public Vector3f location, prevLocation, up = new Vector3f(0, 1, 0);
    public Vector2f rot = new Vector2f(), prevRot = new Vector2f();
    public Scene scene;

    public float localX, localY, localWidth, localHeight, fov, joySensitivity = 1.5f, ratio = -1;
    public int joystick = -1;

    private Vector3f lookAt, lookAtModified;
    private String lastJoyName = "";

    public List<Keybind> keybinds = new ArrayList<>();
    public List<Joybind> joybinds = new ArrayList<>();
    public List<Triangle> triangles = new ArrayList<>();
    public Matrix4f[] matrices = new Matrix4f[3];

    private float rotAmount, moveAmount;
    private boolean paused = false, ratioUpdated = false;
    public int pauseSlot, fullscreenSlot;

    public Camera(float localX, float localY, float localWidth, float localHeight, float fov, Scene scene)
    {
        this(new Vector3f(0, 0, 0), localX, localY, localWidth, localHeight, fov, scene);
    }

    public Camera(Vector3f location, float localX, float localY, float localWidth, float localHeight, float fov, Scene scene)
    {
        this.location = location;
        this.localX = localX;
        this.localY = localY;
        this.localWidth = localWidth;
        this.localHeight = localHeight;
        this.fov = fov;
        this.scene = scene;

        prevLocation = new Vector3f();
        location.add(-1, -1, -1, prevLocation);

        lookAt = new Vector3f(location.x, location.y, location.z - 10);
        lookAtModified = new Vector3f(lookAt);

        matrices[0] = new Matrix4f().ortho2D(-1, 1, -1, 1);
        matrices[1] = new Matrix4f().perspective((float) Math.toRadians(fov), 1, 0.01f, 50);
        matrices[2] = new Matrix4f().ortho2D(-1, 1, -1, 1);
    }

    public Camera(float localX, float localY, float localWidth, float localHeight, float fov, int joystick, Scene scene)
    {
        //this(new Vector3f(0, 6, 6), localX, localY, localWidth, localHeight, fov, joystick, scene);
        this(new Vector3f(0, 0, 0), localX, localY, localWidth, localHeight, fov, joystick, scene);
        //rot.y = -45;
    }

    public Camera(Vector3f location, float localX, float localY, float localWidth, float localHeight, float fov, int joystick, Scene scene)
    {
        this(location, localX, localY, localWidth, localHeight, fov, scene);
        this.joystick = joystick;
    }

    public void setRatio(float ratio)
    {
        if(Float.floatToIntBits(this.ratio) == Float.floatToIntBits(ratio)) return;

        this.ratio = ratio;
        ratioUpdated = true;
    }

    public void registerInputs()
    {
        if(scene.window == null) return;

        //scene.window.inputManager.keybinds.addAll(keybinds);
        scene.window.inputManager.joybinds.addAll(joybinds);
    }

    public void unregisterInputs()
    {
        if(scene.window == null) return;

        //scene.window.inputManager.keybinds.removeAll(keybinds);
        scene.window.inputManager.joybinds.removeAll(joybinds);
    }

    public Vector3f getLookAt()
    {
        if(rot.y < -90) rot.y = -89.9f;
        if(rot.y > 90) rot.y = 89.9f;

        lookAt.set(location.x, location.y, location.z - 10);
        lookAtModified.set(lookAt);

        MathUtility.rotateAxisX(rot.y, location.y, location.z, lookAtModified);
        MathUtility.rotateAxisY(rot.x, location.x, location.z, lookAtModified);

        return lookAtModified;
    }

    public void moveX(float x)
    {
        location.x += x*Math.cos(Math.toRadians(rot.x));
        location.z += x*Math.sin(Math.toRadians(rot.x));
    }

    public void moveY(float y)
    {
        location.y += y;
    }

    public void moveZ(float z)
    {
        location.x += z*Math.sin(Math.toRadians(rot.x + 180));
        location.z -= z*Math.cos(Math.toRadians(rot.x + 180));
    }

    public void forceMatrixUpdate()
    {
        matrices[0].setOrtho2D(-ratio, ratio, -1, 1);
        matrices[1].setPerspective((float) Math.toRadians(fov), ratio, 0.01f, 100).lookAt(location, getLookAt(), up);
        matrices[2].setOrtho2D(-ratio, ratio, -1, 1).translate(location.x/10, location.y/10, 0);

        ratioUpdated = false;
    }

    public void update()
    {
        if(scene.window == null) return;

        rotAmount = 120f/scene.window.getRefreshRate();
        moveAmount = 2.4f/scene.window.getRefreshRate();

        if(ratioUpdated) forceMatrixUpdate();
        else if(!location.equals(prevLocation) || !rot.equals(prevRot))
        {
            Vector3f lookAt = getLookAt();

            matrices[1].setPerspective((float) Math.toRadians(fov), ratio, 0.01f, 100).lookAt(location, lookAt, up);
            matrices[2].setOrtho2D(-ratio, ratio, -1, 1).translate(-location.x/10, location.y/10, 0).lookAt(location, lookAt, up);
        }

        if(joystick != -1 && scene.window != null)
        {
            Joystick joy = scene.window.inputManager.joysticks[joystick];

            if(joy != null && !joy.name.contentEquals(lastJoyName)) updateJoystickBindings(joy);
        }

        prevLocation.set(location);
        prevRot.set(rot);
    }

    private void updateJoystickBindings(Joystick joy)
    {
        lastJoyName = joy.name;

        scene.window.inputManager.joybinds.removeAll(joybinds);
        joybinds.clear();

        if(lastJoyName.contains("Xbox"))
        {
            joybinds.add(new Joybind(joystick, Xbox.AXIS_RSTICK_X, false, true, (value) -> rot.x += joySensitivity* rotAmount *value, null));
            joybinds.add(new Joybind(joystick, Xbox.AXIS_RSTICK_Y, false, true, (value) ->  rot.y += joySensitivity* rotAmount *value, null));
            joybinds.add(new Joybind(joystick, Xbox.AXIS_LSTICK_X, false, true, (value) -> moveX(moveAmount *value), null));
            joybinds.add(new Joybind(joystick, Xbox.AXIS_LSTICK_Y, false, true, (value) -> moveZ(moveAmount *-value), null));
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_A, true, true, (value) -> moveY(moveAmount), null));
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_B, true, true, (value) -> moveY(-moveAmount), null));
            pauseSlot = joybinds.size();
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_START, true, false, (value) -> scene.togglePause(), null));
            fullscreenSlot = joybinds.size();
            joybinds.add(new Joybind(joystick, Xbox.BUTTON_BACK, true, false, (value) -> { if(scene.window != null) scene.window.setFullscreen(!scene.window.isFullscreen());}, null));
        }
        else
        {
            joybinds.add(new Joybind(joystick, DS4.AXIS_RSTICK_X, false, true, (value) -> rot.x += joySensitivity* rotAmount *value, null));
            joybinds.add(new Joybind(joystick, DS4.AXIS_RSTICK_Y, false, true, (value) ->  rot.y -= joySensitivity* rotAmount *value, null));
            joybinds.add(new Joybind(joystick, DS4.AXIS_LSTICK_X, false, true, (value) -> moveX(moveAmount *value), null));
            joybinds.add(new Joybind(joystick, DS4.AXIS_LSTICK_Y, false, true, (value) -> moveZ(moveAmount *value), null));
            joybinds.add(new Joybind(joystick, DS4.BUTTON_X, true, true, (value) -> moveY(moveAmount), null));
            joybinds.add(new Joybind(joystick, DS4.BUTTON_CIRCLE, true, true, (value) -> moveY(-moveAmount), null));
            pauseSlot = joybinds.size();
            joybinds.add(new Joybind(joystick, DS4.BUTTON_OPTIONS, true, false, (value) -> scene.togglePause(), null));
            fullscreenSlot = joybinds.size();
            joybinds.add(new Joybind(joystick, DS4.BUTTON_TOUCHPAD, true, false, (value) -> { if(scene.window != null) scene.window.setFullscreen(!scene.window.isFullscreen());}, null));
        }

        scene.window.inputManager.joybinds.addAll(joybinds);
    }
}
