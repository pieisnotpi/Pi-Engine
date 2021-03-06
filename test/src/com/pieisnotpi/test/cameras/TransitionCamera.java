package com.pieisnotpi.test.cameras;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * A camera that has built in transitioning (VERY WIP)
 */

public class TransitionCamera extends Camera
{
    public float speed, angularSpeed, zoomSpeed;
    private float moveX, moveY, moveZ, tFov, rotX = 0, rotY = 0, rotZ = 0, tZoom = 1;

    public TransitionCamera(float fov, float speed, float angularSpeed, float zoomSpeed, Scene scene)
    {
        this(new Vector3f(), fov, speed, angularSpeed, zoomSpeed, scene);
    }

    public TransitionCamera(Vector3f position, float fov, float speed, float angularSpeed, float zoomSpeed, Scene scene)
    {
        super(position, fov, new Vector2f(0, 0), new Vector2f(1, 1));

        this.speed = speed;
        this.angularSpeed = angularSpeed;
        this.zoomSpeed = zoomSpeed;

        /*moveX = pos.x;
        moveY = pos.y;
        moveZ = pos.z;*/
        tFov = fov;
    }

    public void transitionX(float nx)
    {
        moveX = nx;

        //float dif = nx - pos.x;

        /*if(dif > 0 && dif > speed) dif = speed;
        else if(dif < 0 && dif < -speed) dif = -speed;*/
    
        //setX(pos.x + dif);
    }
    
    public void transitionY(float ny)
    {
        moveY = ny;
        
        //float dif = ny - pos.y;
        
        /*if(dif > 0 && dif > speed) dif = speed;
        else if(dif < 0 && dif < -speed) dif = -speed;*/
        
        //setY(pos.y + dif);
    }
    
    public void transitionZ(float nz)
    {
        moveZ = nz;
        
        //float dif = nz - pos.z;
        
        //if(dif > 0 && dif > speed) dif = speed;
        //else if(dif < 0 && dif < -speed) dif = -speed;
        
        //setZ(pos.z + dif);
    }
    
    private float getRotXAmount()
    {
        //float dif = rotX - rot.x;
        
        /*if(dif > angularSpeed) return angularSpeed;
        else if(dif < -angularSpeed) return -angularSpeed;
        else return dif;*/
        
        return 0;
    }

    private float getRotYAmount()
    {
        /*float dif = rotY - rot.y;

        if(dif > angularSpeed) return angularSpeed;
        else if(dif < -angularSpeed) return -angularSpeed;
        else return dif;*/
    
        return 0;
    }

    private float getRotZAmount()
    {
        /*float dif = rotZ - rot.z;

        if(dif > angularSpeed) return angularSpeed;
        else if(dif < -angularSpeed) return -angularSpeed;
        else return dif;*/
    
        return 0;
    }

    public void transitionXRot(float nx)
    {
        /*rotX = nx;
        float dif = getRotXAmount();
        addToXRot(dif);*/
    }

    public void transitionYRot(float ny)
    {
        /*rotY = ny;
        float dif = getRotYAmount();
        addToYRot(dif);*/
    }

    public void transitionZRot(float nz)
    {
        /*rotZ = nz;
        float dif = getRotZAmount();
        addToZRot(dif);*/
    }

    public void transitionOrthoZoom(float nz)
    {
        tZoom = nz;

        float dif = nz - zoom;

        if(dif > 0 && dif > zoomSpeed) dif = zoomSpeed;
        else if(dif < 0 && dif < -zoomSpeed) dif = -zoomSpeed;

        setZoom(zoom + dif);
    }

    public void transitionFov(float nf)
    {
        tFov = nf;

        float dif = nf - fov;

        if(dif > zoomSpeed) dif = zoomSpeed;
        else if(dif < -zoomSpeed) dif = -zoomSpeed;

        setFov(fov + dif);
    }

    public void drawUpdate(float timeStep)
    {
        /*if(tFov != fov) transitionFov(tFov);
        if(moveX != pos.x) transitionX(moveX);
        if(moveY != pos.y) transitionY(moveY);
        if(moveZ != pos.z) transitionZ(moveZ);
        if(rotX != rot.x) transitionXRot(rotX);
        if(rotY != rot.y) transitionYRot(rotY);
        if(rotZ != rot.z) transitionZRot(rotZ);
        if(tZoom != zoom) transitionOrthoZoom(tZoom);*/

        super.drawUpdate(timeStep);
    }
}
