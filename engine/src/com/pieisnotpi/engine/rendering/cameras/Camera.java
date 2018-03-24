package com.pieisnotpi.engine.rendering.cameras;

import com.pieisnotpi.engine.rendering.mesh.Transform;
import com.pieisnotpi.engine.rendering.textures.FrameBuffer;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.utility.MathUtility;
import org.joml.*;

import java.util.HashMap;
import java.util.Map;

import static com.pieisnotpi.engine.utility.MathUtility.toRads;

public class Camera extends GameObject
{
    public static final int ORTHO2D_S = 0, ORTHO2D_R = 1, PERSP = 2, ORTHO = 3;

    public Vector2f viewPos, viewSize;
    public FrameBuffer frameBuffer;
    public Matrix3f lookAtMatrix = new Matrix3f();
    
    private Map<Integer, CameraMatrix> matrices = new HashMap<>();
    private ViewMatrix view;
    
    private static final Vector3f right = new Vector3f(1, 0, 0), up = new Vector3f(0, 1, 0), forward = new Vector3f(0, 0, 1);
    private Vector3f ra = new Vector3f(), ua = new Vector3f(), fa = new Vector3f();
    
    /*private static final Vector3f eye = new Vector3f(0, 0, 1), lookAt = new Vector3f(0, 0, 1), up = new Vector3f(0, 1, 0);
    private Vector3f eyeActual = new Vector3f(), upActual = new Vector3f(), lookAtActual = new Vector3f();*/
    
    protected Quaternionf quaternion = new Quaternionf();

    protected float fov, zNear = 0.001f, zFar = 1000, ratio = -1;
    protected float orthoZoom = 1;
    protected boolean ratioUpdated = false, zoomUpdated = false, fovUpdated = false, drawFbo = false, drawView = false;
    
    private Matrix4f inverse = new Matrix4f();

    public Camera(float fov, Scene scene)
    {
        this(new Vector3f(), fov, scene);
    }

    public Camera(Vector3f position, float fov, Scene scene)
    {
        this.fov = fov;
        this.scene = scene;

        transform = new Transform();
        transform.setTranslate(position);

        view = new ViewMatrix();
        view.matrix = inverse;
        
        matrices.put(ORTHO2D_S, new CameraMatrix(null));
        matrices.put(ORTHO2D_R, new CameraMatrix(null));
        matrices.put(PERSP, new CameraMatrix(view));
        matrices.put(ORTHO, new CameraMatrix(view));

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

    public CameraMatrix getMatrix(int matrixID) { return matrices.get(matrixID); }
    
    public ViewMatrix getViewMatrix() { return view; }

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

    public void drawUpdate(float timeStep)
    {
        boolean m0 = false, m1 = false, m2 = false;

        if(ratioUpdated) m0 = m1 = m2 = true;
        else if(transform.needsBuilt()) m1 = m2 = true;
        else if(fovUpdated) m1 = true;
        else if(zoomUpdated) m2 = true;

        Vector2i res = scene.window.getWindowRes();

        if(transform.needsBuilt())
        {
            
            
            /*lookAtMatrix.set(transform.getRealMatrix());
            lookAtv4.set(0, 0, 1, 1).mul(transform.getRealMatrix());
            lookAt.set(lookAtv4.x, lookAtv4.y, lookAtv4.z);
            view.lookAt(transform.pos, lookAt, up);*/
            
            transform.getRealMatrix().invert(inverse);
            view.matrix = inverse;
            
            /*transform.getRealMatrix().invert(inverse);
            /*inverse.transform(eyeActual.set(eye));
            inverse.transform(lookAtActual.set(lookAt));
            view.lookAt(eyeActual, lookAtActual, up);*/
            //view.matrix = inverse;
            
            //view.lookAlong(new Vector3f(0, 0, 1), up);
            /*view.reset();
            view.matrix.rotateX(transform.rotRad.x).rotateY(transform.rotRad.y).rotateZ(transform.rotRad.z);
            view.matrix.translate(-transform.pos.x, -transform.pos.y, -transform.pos.z);
            view.needsBuilt = true;*/
            /*quaternion.set(0, 0, 0, 1).rotate(transform.rotRad.x, transform.rotRad.y, transform.rotRad.z);
            view.getMatrix().rotate(quaternion).translateLocal(transform.pos.negate(eye));*/
            
            //view.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), up);
            //view.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), up);
            //view.getMatrix().mul(transform.getRealMatrix());
            //transform.getRealMatrix().mul(view.getMatrix(), view.getMatrix());
    
            /*Vector3f pos = transform.pos, rot = transform.rotDeg;
            
            ra.set(right);
            ua.set(up);
            fa.set(forward);
            
            MathUtility.rotateAxisX(rot.x, 0, 0, fa);
            MathUtility.rotateAxisY(rot.y, 0, 0, ra);
            MathUtility.rotateAxisZ(rot.z, 0, 0, ua);
            
            view.matrix.set(ra.x, ra.y, ra.z, 0, ua.x, ua.y, ua.z, 0, fa.x, fa.y, fa.z, 0, -pos.x, -pos.y, -pos.z, 1);
            view.needsBuilt = true;
            System.out.println(view.matrix);*/
            
            /*lookAtActual.set(lookAt);
            MathUtility.rotateAxisX(transform.rotDeg.x, 0, 0, lookAtActual);
            MathUtility.rotateAxisY(-transform.rotDeg.y + 180, 0, 0, lookAtActual);
            lookAtActual.add(transform.pos);
            
            *//*float t = transform.rotDeg.x;
            while(t > 360) t -= 360;
            while(t < -360) t += 360;
            
            if(t > 90 && t < 270) up.negate(upActual);
            else upActual.set(up);*//*
            
            upActual.set(up);
            MathUtility.rotateAxisZ(transform.rotDeg.z, 0, 0, upActual);
            view.lookAt(transform.pos, lookAtActual, upActual);*/
            
            transform.getBuffer();
        }

        if(m0)
        {
            matrices.get(ORTHO2D_S).ortho2D(-ratio, ratio, -1, 1);
            matrices.get(ORTHO2D_R).ortho2D(0, res.x, 0, res.y);
        }
        if(m1) matrices.get(PERSP).perspective(fov*toRads, ratio, zNear, zFar);
        if(m2) matrices.get(ORTHO).ortho(-ratio/orthoZoom, ratio/orthoZoom, -1/orthoZoom, 1/orthoZoom, zNear, zFar);

        fovUpdated = false;
        ratioUpdated = false;
        zoomUpdated = false;
    }
}
