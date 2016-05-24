package com.pieisnotpi.game.cameras;

import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.scene.Scene;

/**
 * A camera that has built in transitioning (VERY WIP)
 */

public class TransitionCamera extends Camera
{
    public float speed, angularSpeed, zoomSpeed;
    private float moveX, moveY, moveZ, rotX, rotY, rotZ, zoom, speedMin, angularSpeedMin, zoomSpeedMin;

    public TransitionCamera(float localX, float localY, float localWidth, float localHeight, float fov, float speed, float angularSpeed, float zoomSpeed, Scene scene)
    {
        this(0, 0, 0, localX, localY, localWidth, localHeight, fov, speed, angularSpeed, zoomSpeed, scene);
    }

    public TransitionCamera(float x, float y, float z, float localX, float localY, float localWidth, float localHeight, float fov, float speed, float angularSpeed, float zoomSpeed, Scene scene)
    {
        super(x, y, z, localX, localY, localWidth, localHeight, fov, scene);

        this.speed = speed;
        this.angularSpeed = angularSpeed;
        this.zoomSpeed = zoomSpeed;

        speedMin = speed/10;
        angularSpeedMin = angularSpeed/10;
        zoomSpeedMin = zoomSpeed/10;
    }

    public void transitionX(float nx)
    {
        moveX = nx;

        float dif = nx - x;

        if(dif > 0 && dif > speed) dif = speed;
        else if(dif < 0 && dif < -speed) dif = -speed;

        moveX -= dif;
        setX(x + dif);
    }

    public void transitionY(float ny)
    {
        moveY = ny;

        float dif = ny - y;

        if(dif > 0 && dif > speed) dif = speed;
        else if(dif < 0 && dif < -speed) dif = -speed;

        moveY -= dif;
        setY(y + dif);
    }

    public void transitionZ(float nz)
    {
        moveZ = nz;

        float dif = nz - z;

        if(dif > 0 && dif > speed) dif = speed;
        else if(dif < 0 && dif < -speed) dif = -speed;

        moveZ -= dif;
        setZ(z + dif);
    }

    public void transitionXRot(float nx)
    {
        rotX = nx;

        float dif = nx - xRot;

        if(dif > 0 && dif > angularSpeed) dif = angularSpeed;
        else if(dif < 0 && dif < -angularSpeed) dif = -angularSpeed;

        rotX -= dif;
        addToXRot(dif);
    }

    public void transitionYRot(float ny)
    {
        rotY = ny;

        float dif = ny - yRot;

        if(dif > 0 && dif > angularSpeed) dif = angularSpeed;
        else if(dif < 0 && dif < -angularSpeed) dif = -angularSpeed;

        rotY -= dif;
        addToYRot(dif);
    }

    public void transitionZRot(float nz)
    {
        rotZ = nz;

        float dif = nz - zRot;

        if(dif > 0 && dif > angularSpeed) dif = angularSpeed;
        else if(dif < 0 && dif < -angularSpeed) dif = -angularSpeed;

        rotZ -= dif;
        addToZRot(dif);
    }

    public void transitionOrthoZoom(float nz)
    {
        zoom = nz;

        float dif = nz - orthoZoom;

        if(dif > 0 && dif > zoomSpeed) dif = zoomSpeed;
        else if(dif < 0 && dif < -zoomSpeed) dif = -zoomSpeed;

        zoom -= dif;
        setOrthoZoom(orthoZoom + dif);
    }

    public void drawUpdate()
    {
        /*if(moveX < -speedMin || moveX > speedMin) transitionX(moveX);
        if(moveY < -speedMin || moveX > speedMin) transitionY(moveY);
        if(moveZ < -speedMin || moveX > speedMin) transitionZ(moveZ);
        if(rotX < -angularSpeedMin || rotX > angularSpeedMin) transitionXRot(rotX);
        if(rotY < -angularSpeedMin || rotY > angularSpeedMin) transitionYRot(rotY);
        if(rotZ < -angularSpeedMin || rotZ > angularSpeedMin) transitionZRot(rotZ);
        if(zoom < -zoomSpeedMin || zoom > zoomSpeedMin) transitionOrthoZoom(zoom);*/

        if(moveX != 0) transitionX(moveX);
        if(moveY != 0) transitionY(moveY);
        if(moveZ != 0) transitionZ(moveZ);
        if(rotX != 0) transitionXRot(rotX);
        if(rotY != 0) transitionYRot(rotY);
        if(rotZ != 0) transitionZRot(rotZ);
        if(zoom != 0) transitionOrthoZoom(zoom);

        super.drawUpdate();
    }
}
