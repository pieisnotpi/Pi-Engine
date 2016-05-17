package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.utility.MathUtility;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{
    public Scene scene;
    public Vector2f rot = new Vector2f();
    public Vector3f position, up = new Vector3f(0, 1, 0);
    public Matrix4f[] matrices = new Matrix4f[3];

    public float localX, localY, localWidth, localHeight, fov, ratio = -1;

    private Vector2f prevRot = new Vector2f();
    private Vector3f lookAt, prevLocation = new Vector3f();
    private boolean ratioUpdated = false;

    public Camera(float localX, float localY, float localWidth, float localHeight, float fov, Scene scene)
    {
        this(new Vector3f(0, 0, 0), localX, localY, localWidth, localHeight, fov, scene);
    }

    public Camera(Vector3f location, float localX, float localY, float localWidth, float localHeight, float fov, Scene scene)
    {
        this.position = location;
        this.localX = localX;
        this.localY = localY;
        this.localWidth = localWidth;
        this.localHeight = localHeight;
        this.fov = fov;
        this.scene = scene;

        location.add(-1, -1, -1, prevLocation);

        lookAt = new Vector3f(location.x, location.y, location.z - 10);

        matrices[0] = new Matrix4f().ortho2D(-1, 1, -1, 1);
        matrices[1] = new Matrix4f().perspective((float) Math.toRadians(fov), 1, 0.01f, 50);
        matrices[2] = new Matrix4f().ortho2D(-1, 1, -1, 1);
    }

    public void setRatio(float ratio)
    {
        if(Float.floatToIntBits(this.ratio) == Float.floatToIntBits(ratio)) return;

        this.ratio = ratio;
        ratioUpdated = true;
    }

    public Vector3f getLookAt()
    {
        if(rot.y < -90) rot.y = -89.9f;
        if(rot.y > 90) rot.y = 89.9f;

        lookAt.set(position.x, position.y, position.z - 10);

        MathUtility.rotateAxisX(rot.y, position.y, position.z, lookAt);
        MathUtility.rotateAxisY(rot.x, position.x, position.z, lookAt);

        return lookAt;
    }

    public void moveX(float x)
    {
        position.x += x*Math.cos(Math.toRadians(rot.x));
        position.z += x*Math.sin(Math.toRadians(rot.x));
    }

    public void moveY(float y)
    {
        position.y += y;
    }

    public void moveZ(float z)
    {
        position.x += z*Math.sin(Math.toRadians(rot.x + 180));
        position.z -= z*Math.cos(Math.toRadians(rot.x + 180));
    }

    public void forceMatrixUpdate()
    {
        matrices[0].setOrtho2D(-ratio, ratio, -1, 1);
        matrices[1].setPerspective((float) Math.toRadians(fov), ratio, 0.01f, 100).lookAt(position, getLookAt(), up);
        matrices[2].setOrtho2D(-ratio, ratio, -1, 1).lookAt(position, getLookAt(), up);

        ratioUpdated = false;
    }

    public void update()
    {
        if(scene.window == null) return;

        if(ratioUpdated) forceMatrixUpdate();
        else if(!position.equals(prevLocation) || !rot.equals(prevRot))
        {
            Vector3f lookAt = getLookAt();

            matrices[1].setPerspective((float) Math.toRadians(fov), ratio, 0.01f, 100).lookAt(position, lookAt, up);
            matrices[2].setOrtho2D(-ratio, ratio, -1, 1).lookAt(position, lookAt, up);
        }

        prevLocation.set(position);
        prevRot.set(rot);
    }
}
