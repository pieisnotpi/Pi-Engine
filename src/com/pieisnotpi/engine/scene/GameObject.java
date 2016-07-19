package com.pieisnotpi.engine.scene;

import com.pieisnotpi.engine.input.Joystick;
import com.pieisnotpi.engine.rendering.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public abstract class GameObject
{
    public enum HAlignment { NONE, LEFT, CENTER, RIGHT }
    public enum VAlignment { NONE, BOTTOM, CENTER, TOP }

    protected final Vector3f pos = new Vector3f(), rot = new Vector3f(), size = new Vector3f();
    protected Scene scene;
    protected int matrixID;
    protected boolean enabled = true;
    private final Vector3f c = new Vector3f();
    private HAlignment hAlign = HAlignment.NONE;
    private VAlignment vAlign = VAlignment.NONE;
    private Vector2f offset = new Vector2f();
    private boolean shouldAlign = false;

    public void update() {}
    public void drawUpdate() {}
    public void physicsUpdate() {}
    public void enable() { enabled = true; }
    public void disable() { enabled = false; }

    public void setX(float x) { pos.x = x; }
    public void setY(float y) { pos.y = y; }
    public void setZ(float z) { pos.z = z; }
    public void setWidth(float width) { setCx(c.x/size.x*width); size.x = width; }
    public void setHeight(float height) { setCy(c.y/size.y*height); size.y = height; }
    public void setDepth(float depth) { setCz(c.z/size.z*depth); size.z = depth; }
    public void addToRot(float xr, float yr, float zr) { rot.x += xr; rot.y += yr; rot.z += zr; }
    public void setScene(Scene scene) { this.scene = scene; }
    public void setMatrixID(int matrixID) { this.matrixID = matrixID; }
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
    public void onLeftRelease() {}
    public void onRightClick() {}
    public void onRightRelease() {}
    public void onMiddleClick() {}
    public void onMiddleRelease() {}
    public void onKeyPressed(int key, int mods) {}
    public void onKeyReleased(int key, int mods) {}
    public void onWindowResize(Vector2i res) { align(res); }
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
    public float getWidth() { return size.x; }
    public float getHeight() { return size.y; }
    public float getDepth() { return size.z; }
    public float getCx() { return pos.x + c.x; }
    public float getCy() { return pos.y + c.y; }
    public float getCz() { return pos.z + c.z; }
    public float getXRot() { return rot.x; }
    public float getYRot() { return rot.y; }
    public float getZRot() { return rot.z; }
    public int getMatrixID() { return matrixID; }
    public Scene getScene() { return scene; }
    public Vector3f getPos() { return pos; }
    public Vector3f getRot() { return rot; }

    public final void setHAlignment(HAlignment align, float offset) { setAlignment(align, vAlign, offset, this.offset.y); }
    public final void setVAlignment(VAlignment align, float offset) { setAlignment(hAlign, align, this.offset.x, offset); }

    public void setAlignment(HAlignment hAlign, VAlignment vAlign, float hOffset, float vOffset)
    {
        this.hAlign = hAlign;
        this.vAlign = vAlign;

        shouldAlign = !(hAlign.equals(HAlignment.NONE) && vAlign.equals(VAlignment.NONE));

        offset.set(hOffset, vOffset);

        align();
    }

    protected void align()
    {
        if(scene.window != null) align(scene.window.getWindowRes());
    }

    protected void align(Vector2i res)
    {
        if(!shouldAlign) return;

        float ratio = (float) res.x/res.y;

        if(!hAlign.equals(HAlignment.NONE))
        {
            float nx = pos.x;

            if(hAlign.equals(HAlignment.LEFT)) nx = -ratio + offset.x;
            else if(hAlign.equals(HAlignment.CENTER)) nx = -size.x/2;
            else if(hAlign.equals(HAlignment.RIGHT)) nx = ratio - size.x + offset.x;

            if(nx != pos.x) setX(nx);
        }

        if(!vAlign.equals(VAlignment.NONE))
        {
            float ny = pos.y;

            if(vAlign.equals(VAlignment.BOTTOM)) ny = -1 + offset.y;
            else if(vAlign.equals(VAlignment.CENTER)) ny = -size.y/2;
            else if(vAlign.equals(VAlignment.TOP)) ny = 1 + offset.y;

            if(ny != pos.y) setY(ny);
        }
    }

    public void setCx(float cx)
    {
        if(c.x == cx) return;

        if(rot.y != 0)
        {
            float t = rot.y;
            addToRot(0, -t, 0);
            c.x = cx;
            addToRot(0, t, 0);
        }
        if(rot.z != 0)
        {
            float t = rot.z;
            addToRot(0, 0, -t);
            c.x = cx;
            addToRot(0, 0, t);
        }

        c.x = cx;
    }

    public void setCy(float cy)
    {
        if(c.y == cy) return;

        if(rot.x != 0)
        {
            float t = rot.x;
            addToRot(-t, 0, 0);
            c.y = cy;
            addToRot(t, 0, 0);
        }
        if(rot.z != 0)
        {
            float t = rot.z;
            addToRot(0, 0, -t);
            c.y = cy;
            addToRot(0, 0, t);
        }

        c.y = cy;
    }

    public void setCz(float cz)
    {
        if(c.z == cz) return;

        if(rot.x != 0)
        {
            float t = rot.x;
            addToRot(-t, 0, 0);
            c.z = cz;
            addToRot(t, 0, 0);
        }
        if(rot.y != 0)
        {
            float t = rot.y;
            addToRot(0, -t, 0);
            c.z = cz;
            addToRot(0, t, 0);
        }

        c.z = cz;
    }

    public void setCenter(float cx, float cy, float cz)
    {
        if(rot.x != 0)
        {
            float t = rot.x;
            addToRot(-t, 0, 0);
            c.y = cy;
            addToRot(t, 0, 0);
        }

        if(rot.y != 0)
        {
            float t = rot.y;
            addToRot(0, -t, 0);
            c.x = cx;
            addToRot(0, t, 0);
        }

        if(rot.z != 0)
        {
            float t = rot.z;
            addToRot(0, 0, -t);
            c.x = cx;
            addToRot(0, 0, t);
        }

        c.x = cx;
        c.y = cy;
        c.z = cz;
    }

    public void defaultCenter()
    {
        setCenter(size.x/2, size.y/2, size.z/2);
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        destroy();
    }
}
