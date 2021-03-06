package com.pieisnotpi.test.cameras;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.utility.MathUtility;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class OrbitCamera extends Camera
{
    private float mx, my, radius;
    private boolean leftStatus = false, rightStatus = false, first = true;
    private Vector3f oRot = new Vector3f();

    private static final float mouseRotAmount = 0.1f, mouseMoveAmount = 0.005f, zoomAmount = 0.75f;

    public OrbitCamera(Vector3f pos, Vector3f center, float fov, Scene scene)
    {
        super(pos, fov, new Vector2f(0, 0), new Vector2f(1, 1));
        Vector3f dist = new Vector3f();
        pos.sub(center, dist);

        radius = (float) Math.sqrt(dist.x*dist.x + dist.y*dist.y + dist.z*dist.z);
        oRot.y = (float) Math.toDegrees(Math.atan2(dist.z, dist.x));

        MathUtility.rotateAxisY(-oRot.y, 0, 0, dist);
        oRot.x = (float) Math.toDegrees(Math.atan2(dist.y, dist.x));

        /*scene.listener.setPosition(pos.x, pos.y, pos.z);
        scene.listener.setRotation(rot.x, rot.y, rot.z);*/
    }

    @Override
    public void onMouseMovement(Vector2f scaled, Vector2i unscaled)
    {
        if(!leftStatus && !rightStatus)
        {
            first = true;
            return;
        }

        Vector2i res = scene.window.getWindowRes();

        float cx = unscaled.x*2f - res.x, cy = unscaled.y*2f - res.y;

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

            //moveZ(nx);
            //moveX(ny);

            updateAL(false, true);
        }

        mx = cx;
        my = cy;

        super.onMouseMovement(scaled, unscaled);
    }

    @Override
    public void onScroll(float xAmount, float yAmount)
    {
        if(!isCursorWithinViewport(scene.window.inputManager.cursorPos, scene.window.getWindowRes())) return;

        radius -= yAmount*zoomAmount*(radius/20);
        if(radius <= 0) radius = 0.1f;

        float xr = oRot.x, yr = oRot.y;

        if(xr == 0 && yr == 0)
        {
            //pos.x = lookAt.x + radius;
        }
        else
        {
            oRot.set(0, 0, oRot.z);
            addToRot(xr, yr, 0);
        }

        updateAL(false, true);

        super.onScroll(xAmount, yAmount);
    }

    //@Override
    public void addToRot(float xr, float yr, float zr)
    {
        if(xr == 0 && yr == 0 && zr == 0) return;

        //float px = pos.x, py = pos.y, pz = pos.z;

        //lookAt.add(radius, 0, 0, pos);

        if(oRot.x + xr > 90) xr = 89.9f - oRot.x;
        if(oRot.x + xr < -90) xr = -89.9f - oRot.x;

        //MathUtility.rotateAxisZ(rot.x += xr, lookAt.x, lookAt.y, pos);
        //MathUtility.rotateAxisY(rot.y += yr, lookAt.x, lookAt.z, pos);
        //MathUtility.rotateAxisZ(zr, 0, 0, up);

        updateAL(true, true);
    }

    private void updateAL(boolean updateRot, boolean updatePos)
    {
        /*if(scene.cameras.size() == 1)
        {
            if(updateRot) scene.listener.setRotation(rot.x + 180, rot.y + 180, rot.z);
            if(updatePos) scene.listener.setPosition(pos);
        }*/
    }

    @Override
    public void onLeftClick()
    {
        if(isCursorWithinViewport(scene.window.inputManager.cursorPos, scene.window.getWindowRes())) leftStatus = true;
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
        if(isCursorWithinViewport(scene.window.inputManager.cursorPos, scene.window.getWindowRes())) rightStatus = true;
        super.onRightClick();
    }

    @Override
    public void onRightRelease()
    {
        rightStatus = false;
        super.onRightRelease();
    }

    private boolean isCursorWithinViewport(Vector2i cursorPos, Vector2i res)
    {
        return !(cursorPos.x < res.x*viewPos.x || cursorPos.x > res.x*(viewPos.x + viewSize.x) || cursorPos.y < res.y*viewPos.y || cursorPos.y > res.y*(viewPos.y + viewSize.y));
    }
}
