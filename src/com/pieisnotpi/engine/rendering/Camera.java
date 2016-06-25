package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.game_objects.GameObject;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.utility.MathUtility;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Camera extends GameObject
{
    public Vector2f viewPos, viewSize;
    public Vector3f up = new Vector3f(0, 1, 0);
    public Matrix4f[] matrices = new Matrix4f[3];

    public float fov, zNear = 0.001f, zFar = 100, ratio = -1;
    protected float orthoZoom = 0;
    protected Vector3f lookAt, lookAtDist = new Vector3f();
    protected boolean ratioUpdated = false, positionUpdated = false, rotationUpdated = false, zoomUpdated = false, fovUpdated = false;

    public Camera(Vector2f viewPos, Vector2f viewSize, float fov, Scene scene)
    {
        this(new Vector3f(), new Vector3f(0, 0, -10), viewPos, viewSize, fov, scene);
    }

    public Camera(Vector3f position, Vector3f lookAt, Vector2f viewPos, Vector2f viewSize, float fov, Scene scene)
    {
        this.pos.set(position);
        this.lookAt = lookAt;
        this.viewPos = viewPos;
        this.viewSize = viewSize;
        this.fov = fov;
        this.scene = scene;

        lookAt.sub(position, lookAtDist);

        matrices[0] = new Matrix4f().ortho2D(-1, 1, -1, 1);
        matrices[1] = new Matrix4f().perspective((float) Math.toRadians(fov), 1, zNear, zFar);
        matrices[2] = new Matrix4f().ortho(-1, 1, -1, 1, zNear, zFar, false);

        scene.gameObjects.add(this);

        setCenter(0, 0, 0);
    }

    public float getOrthoZoom()
    {
        return orthoZoom;
    }

    public void onWindowResize(Vector2i res)
    {
        super.onWindowResize(res);

        this.ratio = (res.x*viewSize.x)/(res.y*viewSize.y);

        ratioUpdated = true;
    }

    public void setX(float nx)
    {
        lookAt.x += nx - pos.x;
        pos.x = nx;
        positionUpdated = true;
    }

    public void setY(float ny)
    {
        lookAt.y += ny - pos.y;
        pos.y = ny;
        positionUpdated = true;
    }

    public void setZ(float nz)
    {
        lookAt.z += nz - pos.z;
        pos.z = nz;
        positionUpdated = true;
    }

    public void setPosition(Vector3f position)
    {
        this.pos.set(position);
        positionUpdated = true;
    }

    public void setLookAt(Vector3f lookAt)
    {
        this.lookAt = lookAt;
        lookAt.sub(pos, lookAtDist);
        positionUpdated = true;
    }

    public void setOrthoZoom(float zoom)
    {
        if(zoom == 0) zoom = 0.0001f;
        this.orthoZoom = zoom;
        zoomUpdated = true;
    }

    public void setFov(float fov)
    {
        this.fov = fov;
        fovUpdated = true;
    }

    public void moveX(float a)
    {
        double r = Math.toRadians(rot.y), xc = a*Math.cos(r), zc = a*Math.sin(r);

        pos.x += xc;
        pos.z += zc;
        lookAt.x += xc;
        lookAt.z += zc;

        positionUpdated = true;
    }

    public void moveY(float a)
    {
        pos.y += a;
        lookAt.y += a;

        positionUpdated = true;
    }

    public void moveZ(float a)
    {
        double r = Math.toRadians(rot.y + 180), xc = a*Math.sin(r), zc = -a*Math.cos(r);

        pos.x += xc;
        pos.z += zc;
        lookAt.x += xc;
        lookAt.z += zc;

        positionUpdated = true;
    }

    public void drawUpdate()
    {
        if(scene.window == null) return;

        boolean m0 = false, m1 = false, m2 = false;

        if(ratioUpdated) m0 = m1 = m2 = true;
        else if(positionUpdated || rotationUpdated) m1 = m2 = true;
        else { if(fovUpdated) m1 = true; if(zoomUpdated) m2 = true; }

        if(m0) matrices[0].setOrtho2D(-ratio, ratio, -1, 1);
        if(m1) matrices[1].setPerspective((float) Math.toRadians(fov), ratio, zNear, zFar).lookAt(pos, lookAt, up);
        if(m2) matrices[2].setOrtho(-ratio*orthoZoom, ratio*orthoZoom, -1*orthoZoom, 1*orthoZoom, zNear, zFar).lookAt(pos, lookAt, up);

        fovUpdated = false;
        ratioUpdated = false;
        positionUpdated = false;
        rotationUpdated = false;
        zoomUpdated = false;
    }

    public void addToRot(float xr, float yr, float zr)
    {
        if(xr == 0 && yr == 0 && zr == 0) return;

        pos.add(lookAtDist, lookAt);

        if(xr + rot.x >= 90) xr = 89.9f - rot.x;
        else if(xr + rot.x <= -90) xr = -89.9f - rot.x;

        MathUtility.rotateAxisX(xr + rot.x, pos.y, pos.z, lookAt);
        MathUtility.rotateAxisY(yr + rot.y, pos.x, pos.z, lookAt);
        MathUtility.rotateAxisZ(zr + rot.z, 0, 0, up);

        rotationUpdated = true;

        super.addToRot(xr, yr, zr);
    }
}
