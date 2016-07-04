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
    private float mx, my, radius;
    private boolean leftStatus = false, rightStatus = false, first = true;

    private static final float mouseRotAmount = 0.1f, mouseMoveAmount = 0.005f, zoomAmount = 0.25f;

    public OrbitCamera(Vector3f pos, Vector3f center, Vector2f viewPos, Vector2f viewSize, float fov, Scene scene)
    {
        super(pos, center, viewPos, viewSize, fov, scene);
        Vector3f dist = new Vector3f();
        pos.sub(center, dist);

        radius = (float) Math.sqrt(dist.x*dist.x + dist.y*dist.y + dist.z*dist.z);
        rot.y = (float) Math.toDegrees(Math.atan2(dist.z, dist.x));

        MathUtility.rotateAxisY(-rot.y, 0, 0, dist);
        rot.x = (float) Math.toDegrees(Math.atan2(dist.y, dist.x));
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
            float nx = -(cy - my)*mouseRotAmount, ny = (cx - mx)*mouseRotAmount;

            addToRot(nx, ny, 0);
        }
        if(rightStatus)
        {
            float nx = (cx - mx)*mouseMoveAmount, ny = (cy - my)*mouseMoveAmount;

            moveZ(nx);
            moveX(ny);
        }

        mx = cx;
        my = cy;

        super.onMouseMovementUnscaled(cursorPos);
    }

    @Override
    public void onScroll(float xAmount, float yAmount)
    {
        if(!isCursorWithinViewport(scene.window.inputManager.cursorPos, scene.window.res)) return;

        radius -= yAmount*zoomAmount;
        if(radius <= 0) radius = 0.1f;

        float xr = rot.x, yr = rot.y;

        if(xr == 0 && yr == 0)
        {
            pos.x = lookAt.x + radius;
            positionUpdated = true;
        }
        else
        {
            rot.set(0, 0, rot.z);
            addToRot(xr, yr, 0);
        }

        super.onScroll(xAmount, yAmount);
    }

    @Override
    public void addToRot(float xr, float yr, float zr)
    {
        if(xr == 0 && yr == 0 && zr == 0) return;

        float px = pos.x, py = pos.y, pz = pos.z;

        lookAt.add(radius, 0, 0, pos);

        if(rot.x + xr > 90) xr = 89.9f - rot.x;
        if(rot.x + xr < -90) xr = -89.9f - rot.x;

        MathUtility.rotateAxisZ(rot.x += xr, lookAt.x, lookAt.y, pos);
        MathUtility.rotateAxisY(rot.y += yr, lookAt.x, lookAt.z, pos);
        MathUtility.rotateAxisZ(zr, 0, 0, up);

        rotationUpdated = true;
    }

    @Override
    public void onLeftClick()
    {
        if(isCursorWithinViewport(scene.window.inputManager.cursorPos, scene.window.res)) leftStatus = true;
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
        if(isCursorWithinViewport(scene.window.inputManager.cursorPos, scene.window.res)) rightStatus = true;
        super.onRightClick();
    }

    @Override
    public void onRightRelease()
    {
        rightStatus = false;
        super.onRightRelease();
    }

    private boolean isCursorWithinViewport(Vector2d cursorPos, Vector2i res)
    {
        return !(cursorPos.x < res.x*viewPos.x || cursorPos.x > res.x*(viewPos.x + viewSize.x) || cursorPos.y < res.y*viewPos.y || cursorPos.y > res.y*(viewPos.y + viewSize.y));
    }
}
