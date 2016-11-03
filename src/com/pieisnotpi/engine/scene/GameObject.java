package com.pieisnotpi.engine.scene;

import com.pieisnotpi.engine.input.Joystick;
import com.pieisnotpi.engine.rendering.mesh.Transform;
import com.pieisnotpi.engine.rendering.window.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class GameObject
{
    protected List<GameObject> children = new ArrayList<>();
    protected Transform transform = new Transform();
    protected Vector3f pos = new Vector3f(), rot = new Vector3f();
    public Vector3f size = new Vector3f();
    protected Scene scene;
    protected int matrixID;
    protected boolean enabled = true;

    public void update(float timeStep) {}
    public void drawUpdate(float timeStep) {}
    public void physicsUpdate(float timeStep) {}
    public void enable() { enabled = true; }
    public void disable() { enabled = false; }

    public void setX(float x) { pos.x = x; }
    public void setY(float y) { pos.y = y; }
    public void setZ(float z) { pos.z = z; }
    public void setWidth(float width) { size.x = width; }
    public void setHeight(float height) { size.y = height; }
    public void setDepth(float depth) { size.z = depth; }
    public void addToRot(float xr, float yr, float zr) { rot.add(xr, yr, zr); }
    public void setScene(Scene scene) { this.scene = scene; }
    public void setMatrixID(int matrixID) { this.matrixID = matrixID; }
    public void addChild(GameObject child) { children.add(child); transform.addChild(child.transform); }
    public void destroy() { scene.gameObjects.remove(this); }

    public final void addToXRot(float xr) { addToRot(xr, 0, 0); }
    public final void addToYRot(float yr) { addToRot(0, yr, 0); }
    public final void addToZRot(float zr) { addToRot(0, 0, zr); }
    public final void setXRot(float xr) { addToXRot(xr - rot.x); }
    public final void setYRot(float yr) { addToYRot(yr - rot.y); }
    public final void setZRot(float zr) { addToZRot(zr - rot.z); }
    public final void setRot(float xr, float yr, float zr) { addToRot(xr - rot.x, yr - rot.y, zr - rot.z); }

    public void onJoystickConnect(Joystick joystick) {}
    public void onJoystickDisconnect(Joystick joystick) {}
    public void onLeftClick() {}
    public void onLeftHold() {}
    public void onLeftRelease() {}
    public void onRightClick() {}
    public void onRightHold() {}
    public void onRightRelease() {}
    public void onMiddleClick() {}
    public void onMiddleHold() {}
    public void onMiddleRelease() {}
    public void onKeyPressed(int key, int mods) {}
    public void onKeyReleased(int key, int mods) {}
    public void onWindowResize(Vector2i res) {}
    public void onScroll(float xAmount, float yAmount) {}
    public void onMouseEntered() {}
    public void onMouseExited() {}
    public void onMouseMovementUnscaled(Vector2d cursorPos) {}
    public void onMouseMovement(Vector2f cursorPos) {}
    public void onWindowChanged(Window oldWindow, Window newWindow) {}
    public void toggle() { if(enabled) disable(); else enable(); }

    public boolean isEnabled() { return enabled; }
    public boolean isPointInsideObject(Vector2f point) { return false; }

    public float getX() { return pos.x; }
    public float getY() { return pos.y; }
    public float getZ() { return pos.z; }
    public float getWidth() { return size.x*transform.scale.x; }
    public float getHeight() { return size.y*transform.scale.y; }
    public float getDepth() { return size.z*transform.scale.z; }
    public float getXRot() { return rot.x; }
    public float getYRot() { return rot.y; }
    public float getZRot() { return rot.z; }
    public int getMatrixID() { return matrixID; }
    public Scene getScene() { return scene; }
    public Vector3f getPos() { return pos; }
    public Vector3f getRot() { return rot; }
    public Transform getTransform() { return transform; }
}
