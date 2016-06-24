package com.pieisnotpi.game.cameras;

import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * A camera that has built in transitioning (VERY WIP)
 */

public class TransitionCamera extends Camera
{
    public float speed, angularSpeed, zoomSpeed;
    private float moveX, moveY, moveZ, rotX, rotY, rotZ, tzoom, tfov;

    public TransitionCamera(Vector2f viewPos, Vector2f viewSize, float fov, float speed, float angularSpeed, float zoomSpeed, Scene scene)
    {
        this(new Vector3f(), new Vector3f(0, 0, -10), viewPos, viewSize, fov, speed, angularSpeed, zoomSpeed, scene);
    }

    public TransitionCamera(Vector3f position, Vector3f lookAt, Vector2f viewPos, Vector2f viewSize, float fov, float speed, float angularSpeed, float zoomSpeed, Scene scene)
    {
        super(position, lookAt, viewPos, viewSize, fov, scene);

        this.speed = speed;
        this.angularSpeed = angularSpeed;
        this.zoomSpeed = zoomSpeed;

        moveX = pos.x;
        moveY = pos.y;
        moveZ = pos.z;
        tfov = fov;
    }

    public void transitionX(float nx)
    {
        moveX = nx;

        float dif = nx - pos.x;

        if(dif > 0 && dif > speed) dif = speed;
        else if(dif < 0 && dif < -speed) dif = -speed;

        setX(pos.x + dif);
    }

    public void transitionY(float ny)
    {
        moveY = ny;

        float dif = ny - pos.y;

        if(dif > 0 && dif > speed) dif = speed;
        else if(dif < 0 && dif < -speed) dif = -speed;

        setY(pos.y + dif);
    }

    public void transitionZ(float nz)
    {
        moveZ = nz;

        float dif = nz - pos.z;

        if(dif > 0 && dif > speed) dif = speed;
        else if(dif < 0 && dif < -speed) dif = -speed;

        setZ(pos.z + dif);
    }

    private float getRotXAmount()
    {
        float dif = rotX - rot.x;

        if(dif > angularSpeed) return angularSpeed;
        else if(dif < -angularSpeed) return -angularSpeed;
        else return dif;
    }

    private float getRotYAmount()
    {
        float dif = rotY - rot.y;

        if(dif > angularSpeed) return angularSpeed;
        else if(dif < -angularSpeed) return -angularSpeed;
        else return dif;
    }

    private float getRotZAmount()
    {
        float dif = rotZ - rot.z;

        if(dif > angularSpeed) return angularSpeed;
        else if(dif < -angularSpeed) return -angularSpeed;
        else return dif;
    }

    public void transitionXRot(float nx)
    {
        rotX = nx;
        float dif = getRotXAmount();
        addToRot(dif, 0, 0);
    }

    public void transitionYRot(float ny)
    {
        rotY = ny;
        float dif = getRotYAmount();
        addToRot(0, dif, 0);
    }

    public void transitionZRot(float nz)
    {
        rotZ = nz;
        float dif = getRotZAmount();
        addToRot(0, 0, dif);
    }

    public void transitionOrthoZoom(float nz)
    {
        tzoom = nz;

        float dif = nz - orthoZoom;

        if(dif > 0 && dif > zoomSpeed) dif = zoomSpeed;
        else if(dif < 0 && dif < -zoomSpeed) dif = -zoomSpeed;

        setOrthoZoom(orthoZoom + dif);
    }

    public void transitionFov(float nf)
    {
        tfov = nf;

        float dif = nf - fov;

        if(dif > zoomSpeed) dif = zoomSpeed;
        else if(dif < -zoomSpeed) dif = -zoomSpeed;

        setFov(fov + dif);
    }

    public void drawUpdate()
    {
        if(moveX != pos.x) transitionX(moveX);
        if(moveY != pos.y) transitionY(moveY);
        if(moveZ != pos.z) transitionZ(moveZ);
        if(rotX != rot.x) transitionXRot(rotX);
        if(rotY != rot.y) transitionYRot(rotY);
        if(rotZ != rot.z) transitionZRot(rotZ);
        if(tzoom != orthoZoom) transitionOrthoZoom(tzoom);
        if(tfov != fov) transitionFov(tfov);

        super.drawUpdate();
    }
}
