package com.pieisnotpi.engine.rendering.cameras;

import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.buffers.FrameBuffer;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.mesh.Transform;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.scene.IgnoreMeshWarning;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.*;

import static com.pieisnotpi.engine.utility.MathUtility.toRads;

@IgnoreMeshWarning
public class Camera extends GameObject
{
    public static final int CQ = 0, ORTHO2D_S = 1, ORTHO2D_R = 2, PERSP = 3, ORTHO = 4;
    public static final Sprite sprite = new Sprite(0f, 0f, 1f, 1f, false);

    public static final Transform blankTransform = new Transform();
    
    public static DrawPassInit passInitializer = new DrawPassInit(){};
    
    public Vector2f viewPos, viewSize;
    public FrameBuffer frameBuffer;
    public TexQuad quad;
    public Mesh<TexQuad> mesh;
    public List<Material> shaders = new LinkedList<>();
    
    protected float fov, zNear = 0.001f, zFar = 1000, ratio = -1;
    protected float zoom = 1;
    protected boolean ratioUpdated = false, zoomUpdated = false, fovUpdated = false;

    private Map<Integer, DrawPass> passes = new TreeMap<>();
    private Map<Integer, CameraMatrix> matrices = new HashMap<>();
    private Matrix4f mView = new Matrix4f();

    public Camera(float fov, Vector2i res)
    {
        this(new Vector3f(), fov, res);
    }

    public Camera(float fov, Vector2f viewPos, Vector2f viewSize)
    {
        this(new Vector3f(), fov, viewPos, viewSize);
    }
    
    public Camera(Vector3f position, float fov, Vector2i res)
    {
        this.fov = fov;

        transform = new Transform();
        transform.setTranslate(position);

        matrices.put(CQ, new CameraMatrix(null));
        matrices.put(ORTHO2D_S, new CameraMatrix(null));
        matrices.put(ORTHO2D_R, new CameraMatrix(null));
        matrices.put(PERSP, new CameraMatrix(mView));
        matrices.put(ORTHO, new CameraMatrix(mView));

        matrices.get(CQ).ortho2D(0, 1, 0, 1);
        
        frameBuffer = new FrameBuffer(res);
        ratio = (float) res.x/res.y;
        
        quad = new TexQuad(0, 0, -0.1f, ratio, 1, 0, sprite);
        mesh = new Mesh<TexQuad>(new TexMaterial(CQ, frameBuffer.texture), MeshConfig.QUAD).addPrimitive(quad).build();
        passInitializer.init(this);
    }

    public Camera(Vector3f position, float fov, Vector2f viewPos, Vector2f viewSize)
    {
        this.fov = fov;
        this.viewPos = viewPos;
        this.viewSize = viewSize;

        transform = new Transform();
        transform.setTranslate(position);

        matrices.put(CQ, new CameraMatrix(null));
        matrices.put(ORTHO2D_S, new CameraMatrix(null));
        matrices.put(ORTHO2D_R, new CameraMatrix(null));
        matrices.put(PERSP, new CameraMatrix(mView));
        matrices.put(ORTHO, new CameraMatrix(mView));

        matrices.get(CQ).ortho2D(0, 1, 0, 1);
        
        frameBuffer = new FrameBuffer(new Vector2i(300, 300));
        quad = new TexQuad(0, 0, -0.1f, 1, 1, 0, sprite);
        mesh = new Mesh<TexQuad>(new TexMaterial(CQ, frameBuffer.texture), MeshConfig.QUAD).addPrimitive(quad).build();
        passInitializer.init(this);
    }

    public float getZoom()
    {
        return zoom;
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

    public float getFov() { return fov; }

    public float getZNear() { return zNear; }

    public float getZFar() { return zFar; }
    
    public Matrix4f getView()
    {
        return mView;
    }

    public void onWindowResize(Vector2i res)
    {
        super.onWindowResize(res);

        if(viewSize != null)
        {
            frameBuffer.setRes((int) (res.x*viewSize.x), (int) (res.y*viewSize.y));
            ratio = (res.x*viewSize.x)/(res.y*viewSize.y);
        }

        ratioUpdated = true;
    }

    public void setViewport(Vector2f viewPos, Vector2f viewSize)
    {
        this.viewPos = viewPos;
        this.viewSize = viewSize;

        if(scene != null && scene.window != null)
        {
            Vector2i res = scene.window.getWindowRes();
            ratio = (viewSize.x*res.x)/(viewSize.y*res.y);
            frameBuffer.setRes((int) (res.x*viewSize.x), (int) (res.y*viewSize.y));

            if(quad == null) quad = new TexQuad(0, 0, -0.1f, 1, 1, 0, sprite);
        }
    }

    public void setZoom(float zoom)
    {
        if(zoom == 0) zoom = 0.0001f;
        this.zoom = zoom;
        zoomUpdated = true;
    }

    public void setFov(float fov)
    {
        this.fov = fov;
        fovUpdated = true;
    }
    
    public void addDrawPass(int pass, DrawPass drawPass)
    {
        passes.put(pass, drawPass);
    }

    public void drawUpdate(float timeStep)
    {
        boolean m0 = false, m1 = false, m2 = false;

        if(ratioUpdated) m0 = m1 = m2 = true;
        else if(transform.needsBuilt()) m1 = m2 = true;
        else if(fovUpdated) m1 = true;
        else if(zoomUpdated) m2 = true;

        Vector2i res = scene.window.getWindowRes();

        if(transform.needsBuilt()) transform.getRealMatrix().invert(mView);
        
        if(m0)
        {
            matrices.get(ORTHO2D_S).ortho2D(-ratio, ratio, -1, 1);
            matrices.get(ORTHO2D_R).ortho2D(0, res.x*viewSize.x, 0, res.y*viewSize.y);
        }
        if(m1) matrices.get(PERSP).perspective(fov*toRads, ratio, zNear, zFar);
        if(m2) matrices.get(ORTHO).ortho(-ratio/ zoom, ratio/ zoom, -1/ zoom, 1/ zoom, zNear, zFar);
        
        matrices.forEach((i, m) -> m.compile());

        fovUpdated = false;
        ratioUpdated = false;
        zoomUpdated = false;
    }

    public void draw()
    {
        passes.forEach((i, p) ->
        {
            List<Renderable> r = scene.renderables.get(i);
            p.draw(r);
        });
    }
}
