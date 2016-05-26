package com.pieisnotpi.engine.game_objects;

import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public abstract class GameObject
{
    protected Scene scene;
    protected int matrixID;
    protected boolean enabled = true;
    protected float x = 0, y = 0, z = 0, width = 0, height = 0, depth = 0, xRot = 0, yRot = 0, zRot = 0;
    private float cx = 0, cy = 0, cz = 0;

    public void defaultCenter()
    {
        setCenter(width/2, height/2, depth/2);
    }

    public void setCenter(float x, float y, float z)
    {
        if(xRot != 0 && (y != cy || z != cz))
        {
            float t = xRot;
            setXRot(0);
            cy = y;
            cz = z;
            setXRot(t);
        }

        if(yRot != 0 && (x != cx || z != cz))
        {
            float t = yRot;
            setYRot(0);
            cx = x;
            cz = z;
            setYRot(t);
        }

        if(zRot != 0 && (x != cx || y != cy))
        {
            float t = zRot;
            setZRot(0);
            cx = x;
            cy = y;
            setZRot(t);
        }

        cx = x;
        cy = y;
        cz = z;
    }

    public void update() {}
    public void drawUpdate() {}
    public void physicsUpdate() {}
    public void enable() { enabled = true; }
    public void disable() { enabled = false; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setZ(float z) { this.z = z; }
    public void setWidth(float width) { if(cx == this.width/2) setCenter(width/2, cy, cz); this.width = width; }
    public void setHeight(float height) { if(cy == this.height/2) setCenter(cx, height/2, cz); this.height = height; }
    public void setDepth(float depth) { if(cz == this.depth/2) setCenter(cx, cy, depth/2); this.depth = depth; }
    public void addToXRot(float rot) { xRot += rot; }
    public void addToYRot(float rot) { yRot += rot; }
    public void addToZRot(float rot) { zRot += rot; }
    public void setXRot(float rot) { addToXRot(rot - xRot); }
    public void setYRot(float rot) { addToYRot(rot - yRot); }
    public void setZRot(float rot) { addToZRot(rot - zRot); }
    public void setScene(Scene scene) { this.scene = scene; }
    public void setMatrixID(int matrixID) { this.matrixID = matrixID; }
    public void destroy() { scene.gameObjects.remove(this); }

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
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getDepth() { return depth; }
    public float getCx() { return x + cx; }
    public float getCy() { return y + cy; }
    public float getCz() { return z + cz; }
    public float getXRot() { return xRot; }
    public float getYRot() { return yRot; }
    public float getZRot() { return zRot; }
    public int getMatrixID() { return matrixID; }
    public Scene getScene() { return scene; }

    public void setCx(float cx)
    {
        if(this.cx == cx) return;

        if(yRot != 0)
        {
            float t = yRot;
            setYRot(0);
            this.cx = cx;
            setYRot(t);
        }
        if(zRot != 0)
        {
            float t = zRot;
            setZRot(0);
            this.cx = cx;
            setZRot(t);
        }

        this.cx = cx;
    }

    public void setCy(float cy)
    {
        if(this.cy == cy) return;

        if(xRot != 0)
        {
            float t = xRot;
            setXRot(0);
            this.cy = cy;
            setXRot(t);
        }
        if(zRot != 0)
        {
            float t = zRot;
            setZRot(0);
            this.cy = cy;
            setZRot(t);
        }

        this.cy = cy;
    }

    public void setCz(float cz)
    {
        if(this.cz == cz) return;

        if(xRot != 0)
        {
            float t = xRot;
            setXRot(0);
            this.cz = cz;
            setXRot(t);
        }
        if(yRot != 0)
        {
            float t = yRot;
            setYRot(0);
            this.cz = cz;
            setYRot(t);
        }

        this.cz = cz;
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        destroy();
    }
}
