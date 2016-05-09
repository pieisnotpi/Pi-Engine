package com.pieisnotpi.engine.game_objects;

import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

@SuppressWarnings("ALL")
public abstract class GameObject
{
    protected Scene scene;
    protected int matrixID;
    protected boolean enabled = true;
    protected float x = 0, y = 0, z = 0, xRot = 0, yRot = 0, zRot = 0;

    public void update() {}
    public void physicsUpdate() {}
    public void enable() { enabled = true; }
    public void disable() { enabled = false; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setZ(float z) { this.z = z; }
    public void addToXRot(float rot) { addToXRot(rot, y, z); }
    public void addToXRot(float rot, float cy, float cz) { xRot += rot; }
    public void addToYRot(float rot) { addToYRot(rot, x, z); }
    public void addToYRot(float rot, float cx, float cz) { yRot += rot; }
    public void addToZRot(float rot) { addToZRot(rot, x, y); }
    public void addToZRot(float rot, float cx, float cy) { zRot += rot; }
    public void setXRot(float rot) { addToXRot(rot - xRot); }
    public void setXRot(float rot, float cy, float cz) { addToXRot(rot - xRot, cy, cz); }
    public void setYRot(float rot) { addToYRot(rot - yRot); }
    public void setYRot(float rot, float cx, float cz) { addToYRot(rot - yRot, cx, cz); }
    public void setZRot(float rot) { addToZRot(rot - zRot); }
    public void setZRot(float rot, float cx, float cy) { addToZRot(rot - zRot, cx, cy); }
    public void setScene(Scene scene) { this.scene = scene; }
    public void setMatrixID(int matrixID) { this.matrixID = matrixID; }

    public void onLeftClick() {}
    public void onLeftRelease() {}
    public void onRightClick() {}
    public void onRightRelease() {}
    public void onMiddleClick() {}
    public void onMiddleRelease() {}
    public void onKeyPressed(int key, int mods) {}
    public void onKeyReleased(int key, int mods) {}
    public void onWindowResize(Vector2i res) {}
    public void onScroll(float xAmount, float yAmount) {}
    public void onMouseEntered() {}
    public void onMouseExited() {}
    public void onMouseMovement(Vector2f cursorPos) {}
    public void toggle() { if(enabled) disable(); else enable(); }

    public boolean isEnabled() { return enabled; }
    public boolean isPointInsideObject(Vector2f point) { return false; }
    public boolean isPointInsideObject(Vector3f point) { return false; }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    public float getXRot() { return xRot; }
    public float getYRot() { return yRot; }
    public float getZRot() { return zRot; }
    public int getMatrixID() { return matrixID; }
    public Scene getScene() { return scene; }
}
