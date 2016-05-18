package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.game_objects.GameObject;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.utility.MathUtility;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Camera extends GameObject
{
    public Vector3f up = new Vector3f(0, 1, 0);
    public Matrix4f[] matrices = new Matrix4f[3];

    public float localX, localY, localWidth, localHeight, fov, ratio = -1;
    protected float orthoZoom = 0;

    private Vector3f lookAt, position = new Vector3f();
    protected boolean ratioUpdated = false, positionUpdated = false, rotationUpdated = false, orthoUpdated = false;

    public Camera(float localX, float localY, float localWidth, float localHeight, float fov, Scene scene)
    {
        this(0, 0, 0, localX, localY, localWidth, localHeight, fov, scene);
    }

    public Camera(float x, float y, float z, float localX, float localY, float localWidth, float localHeight, float fov, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.localX = localX;
        this.localY = localY;
        this.localWidth = localWidth;
        this.localHeight = localHeight;
        this.fov = fov;
        this.scene = scene;

        lookAt = new Vector3f(x, y, z - 10);

        matrices[0] = new Matrix4f().ortho2D(-1, 1, -1, 1);
        matrices[1] = new Matrix4f().perspective((float) Math.toRadians(fov), 1, 0.01f, 50);
        matrices[2] = new Matrix4f().ortho2D(-1, 1, -1, 1);

        scene.gameObjects.add(this);
    }

    public void onWindowResize(Vector2i res)
    {
        super.onWindowResize(res);

        this.ratio = (float) res.x/res.y;
        ratioUpdated = true;
    }

    public Vector3f getLookAt()
    {
        if(yRot < -90) yRot = -89.9f;
        if(yRot > 90) yRot = 89.9f;

        lookAt.set(x, y, z - 10);

        MathUtility.rotateAxisX(yRot, y, z, lookAt);
        MathUtility.rotateAxisY(xRot, x, z, lookAt);

        return lookAt;
    }

    public void setX(float nx)
    {
        x = nx;
        positionUpdated = true;
    }

    public void setY(float ny)
    {
        y = ny;
        positionUpdated = true;
    }

    public void setZ(float nz)
    {
        z = nz;
        positionUpdated = true;
    }

    public void setOrthoZoom(float nz)
    {
        orthoZoom = nz;

    }

    public void moveX(float a)
    {
        x += a*Math.cos(Math.toRadians(xRot));
        z += a*Math.sin(Math.toRadians(xRot));

        positionUpdated = true;
    }

    public void moveY(float a)
    {
        y += a;

        positionUpdated = true;
    }

    public void moveZ(float a)
    {
        x += a*Math.sin(Math.toRadians(xRot + 180));
        z -= a*Math.cos(Math.toRadians(xRot + 180));

        positionUpdated = true;
    }

    public void forceMatrixUpdate()
    {
        position.set(x, y, z);

        matrices[0].setOrtho2D(-ratio, ratio, -1, 1);
        matrices[1].setPerspective((float) Math.toRadians(fov), ratio, 0.01f, 100).lookAt(position, getLookAt(), up);
        matrices[2].setOrtho2D(-ratio*getZoomChange(), ratio*getZoomChange(), -1*getZoomChange(), 1*getZoomChange()).lookAt(position, getLookAt(), up);

        ratioUpdated = false;
        positionUpdated = false;
        rotationUpdated = false;
    }

    public void drawUpdate()
    {
        if(scene.window == null) return;

        if(ratioUpdated) forceMatrixUpdate();
        else if(positionUpdated || rotationUpdated || orthoUpdated)
        {
            Vector3f lookAt = getLookAt();
            position.set(x, y, z);

            matrices[1].setPerspective((float) Math.toRadians(fov), ratio, 0.01f, 100).lookAt(position, lookAt, up);
            matrices[2].setOrtho2D(-ratio*getZoomChange(), ratio*getZoomChange(), -1*getZoomChange(), 1*getZoomChange()).lookAt(position, lookAt, up);
        }

        ratioUpdated = false;
        positionUpdated = false;
        rotationUpdated = false;
        orthoUpdated = false;
    }

    private float getZoomChange()
    {
        float r = 1 - orthoZoom;

        if(r <= 0) r = 0.0001f;

        return r;
    }

    public void addToXRot(float amount, float cy, float cz)
    {
        if(amount == 0) return;
        rotationUpdated = true;
        super.addToXRot(amount, cy, cz);
    }

    public void addToYRot(float amount, float cx, float cz)
    {
        if(amount == 0) return;
        rotationUpdated = true;
        super.addToYRot(amount, cx, cz);
    }

    public void addToZRot(float amount, float cx, float cy)
    {
        if(amount == 0) return;
        rotationUpdated = true;
        MathUtility.rotateAxisZ(amount, cx, cy, up);
        super.addToZRot(amount, cx, cy);
    }
}
