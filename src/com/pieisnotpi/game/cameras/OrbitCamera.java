package com.pieisnotpi.game.cameras;

import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.utility.MathUtility;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class OrbitCamera extends Camera
{
    private Vector3f center;
    private float dist, mx, my;
    private boolean leftStatus = false, rightStatus = false, first = true;

    private static final float mouseRotAmount = 0.1f, mouseMoveAmount = 0.005f;

    public OrbitCamera(Vector3f center, float dist, Vector2f viewPos, Vector2f viewSize, float fov, Scene scene)
    {
        super(new Vector3f(center.x, center.y, center.z + dist), center, viewPos, viewSize, fov, scene);
        this.center = center;
        this.dist = dist;
    }

    @Override
    public void onMouseMovementUnscaled(Vector2d cursorPos)
    {
        if(!leftStatus && !rightStatus)
        {
            first = true;
            return;
        }

        Vector2i res = scene.window.res;
        float cx = (float) (cursorPos.x*2f - res.x), cy = (float) (cursorPos.y*2f - res.y);

        if(cx > -0.001 && cx < 0.001) cx = 0;
        if(cy > -0.001 && cy < 0.001) cy = 0;

        if(first)
        {
            first = false;
            mx = cx;
            my = cy;
            return;
        }

        if(leftStatus)
        {
            float nx = (cy - my)*mouseRotAmount, ny = (cx - mx)*mouseRotAmount;

            addToRot(nx, ny, 0);
        }
        else
        {
            float nx = (cx - mx)*mouseMoveAmount, ny = -(cy - my)*mouseMoveAmount;

            moveX(nx);
            moveZ(ny);
        }

        mx = cx;
        my = cy;

        super.onMouseMovementUnscaled(cursorPos);
    }

    @Override
    public void addToRot(float xr, float yr, float zr)
    {
        if(xr == 0 && yr == 0 && zr == 0) return;

        center.add(0, 0, dist, pos);

        if(rot.x + xr > 90) xr = 89.9f - rot.x;
        if(rot.x + xr < -90) xr = -89.9f - rot.x;

        MathUtility.rotateAxisX(rot.x += xr, center.y, center.z, pos);
        MathUtility.rotateAxisY(rot.y += yr, center.x, center.z, pos);
        MathUtility.rotateAxisZ(rot.z += zr, 0, 0, up);

        rotationUpdated = true;
    }

    @Override
    public void onLeftClick()
    {
        leftStatus = true;
        super.onLeftClick();
    }

    @Override
    public void onLeftRelease()
    {
        leftStatus = false;
        super.onLeftRelease();
    }

    @Override
    public void onRightClick()
    {
        rightStatus = true;
        super.onRightClick();
    }

    @Override
    public void onRightRelease()
    {
        rightStatus = false;
        super.onRightRelease();
    }
}
