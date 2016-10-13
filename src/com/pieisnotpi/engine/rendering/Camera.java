package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.textures.FrameBuffer;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.utility.MathUtility;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Camera extends GameObject
{
    public Vector2f viewPos, viewSize;
    public Matrix4f[] matrices = new Matrix4f[4];
    public FrameBuffer frameBuffer;

    protected float fov, zNear = 0.001f, zFar = 1000, ratio = -1;
    protected float orthoZoom = 1;
    protected Vector3f lookAt, lookAtDist = new Vector3f(), up = new Vector3f(0, 1, 0);
    protected boolean ratioUpdated = false, positionUpdated = false, rotationUpdated = false, zoomUpdated = false, fovUpdated = false, drawFbo = false, drawView = false;

    public Camera(float fov, Scene scene)
    {
        this(new Vector3f(), new Vector3f(0, 0, -10), fov, scene);
    }

    public Camera(Vector3f position, Vector3f lookAt, float fov, Scene scene)
    {
        this.pos.set(position);
        this.lookAt = lookAt;
        this.fov = fov;
        this.scene = scene;

        lookAt.sub(position, lookAtDist);

        matrices[0] = new Matrix4f();
        matrices[1] = new Matrix4f();
        matrices[2] = new Matrix4f();
        matrices[3] = new Matrix4f();

        scene.gameObjects.add(this);
    }

    public Camera setViewport(Vector2f viewPos, Vector2f viewSize)
    {
        drawView = true;
        this.viewPos = viewPos;
        this.viewSize = viewSize;
        if(frameBuffer != null) frameBuffer.resLocked = false;
        return this;
    }

    public Camera setFramebufferRes(Vector2i res)
    {
        drawFbo = true;
        if(frameBuffer == null) frameBuffer = new FrameBuffer(res, !drawView);
        else { frameBuffer.setRes(res.x, res.y); frameBuffer.resLocked = !drawView; }
        if(!drawView) { ratio = (float) res.x/res.y; ratioUpdated = true; }
        return this;
    }

    public void disableViewDrawing()
    {
        drawView = false;
    }

    public void disableFboDrawing()
    {
        drawFbo = false;
    }

    public float getOrthoZoom()
    {
        return orthoZoom;
    }

    public Vector3f getLookAt()
    {
        return lookAt;
    }

    public Vector3f getUp()
    {
        return up;
    }

    public Vector2f getViewPos()
    {
        return viewPos;
    }

    public Vector2f getViewSize()
    {
        return viewSize;
    }

    public Matrix4f getMatrix(int matrixID)
    {
        if(matrixID > -1 && matrixID < matrices.length) return matrices[matrixID];
        else return null;
    }

    public float getFov() { return fov; }

    public float getZNear() { return zNear; }

    public float getZFar() { return zFar; }

    public boolean shouldDrawFbo() { return drawFbo; }

    public boolean shouldDrawView() { return drawView; }

    public void onWindowResize(Vector2i res)
    {
        super.onWindowResize(res);

        if(drawView) this.ratio = (res.x*viewSize.x)/(res.y*viewSize.y);
        if(drawFbo && !frameBuffer.resLocked) frameBuffer.setRes((int) (res.x*viewSize.x), (int) (res.y*viewSize.y));

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

    /*public void setLookAt(Vector3f lookAt)
    {
        this.lookAt = lookAt;
        lookAt.sub(pos, lookAtDist);
        positionUpdated = true;
    }*/

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
        boolean m0 = false, m1 = false, m2 = false;

        if(ratioUpdated) m0 = m1 = m2 = true;
        else if(positionUpdated || rotationUpdated) m1 = m2 = true;
        else { if(fovUpdated) m1 = true; if(zoomUpdated) m2 = true; }

        Vector2i res = scene.window.getWindowRes();

        if(m0)
        {
            matrices[PiEngine.M_ORTHO2D_S_ID].setOrtho2D(-ratio, ratio, -1, 1);
            matrices[PiEngine.M_ORTHO2D_R_ID].setOrtho2D(0, res.x, 0, res.y);
        }
        if(m1) matrices[PiEngine.M_PERSP].setPerspective((float) Math.toRadians(fov), ratio, zNear, zFar).lookAt(pos, lookAt, up);
        if(m2) matrices[PiEngine.M_ORTHO].setOrtho(-ratio/orthoZoom, ratio/orthoZoom, -1/orthoZoom, 1/orthoZoom, zNear, zFar).lookAt(pos, lookAt, up);

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
