package com.pieisnotpi.engine.ui;

import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.primitives.Primitive;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class UiObject<m extends Primitive> extends GameObject<m>
{
    public enum HAlignment { NONE, LEFT, CENTER, RIGHT }
    public enum VAlignment { NONE, BOTTOM, CENTER, TOP }

    private HAlignment hAlign = HAlignment.NONE;
    private VAlignment vAlign = VAlignment.NONE;
    private Vector2f offset = new Vector2f();
    private boolean shouldAlign = false;

    protected boolean mouseHoverStatus = false;
    protected int matrixID;

    protected UiObject() {}
    
    public UiObject(Renderable mesh, int matrixID)
    {
        super(mesh);
        this.matrixID = matrixID;
    }
    
    public void setMatrixID(int matrixID) { this.matrixID = matrixID; }
    public int getMatrixID() { return matrixID; }

    public final void setHAlignment(HAlignment align, float offset) { setAlignment(align, vAlign, offset, this.offset.y); }
    public final void setVAlignment(VAlignment align, float offset) { setAlignment(hAlign, align, this.offset.x, offset); }

    public void setAlignment(HAlignment hAlign, VAlignment vAlign, float hOffset, float vOffset)
    {
        this.hAlign = hAlign;
        this.vAlign = vAlign;

        shouldAlign = !(hAlign.equals(HAlignment.NONE) && vAlign.equals(VAlignment.NONE));

        offset.set(hOffset, vOffset);

        align();
    }

    public void align()
    {
        if(scene != null && scene.window != null) align(scene.window.getWindowRes());
    }

    public void align(Vector2i res)
    {
        if(!shouldAlign) return;

        float ratio = (float) res.x/res.y;

        if(!hAlign.equals(HAlignment.NONE))
        {
            float nx, start = 0, mid = 0, end = 0;

            if(matrixID == Camera.ORTHO2D_S)
            {
                start = -ratio;
                end = ratio;
            }
            else if(matrixID == Camera.ORTHO2D_R)
            {
                start = 0;
                end = res.x;
                mid = res.x/2f;
            }

            if(hAlign.equals(HAlignment.LEFT)) nx = start + offset.x;
            else if(hAlign.equals(HAlignment.CENTER)) nx = mid + -getWidth()/2 + offset.x;
            else nx = end - getWidth() + offset.x;

            transform.translateAbs(nx - transform.pos.x, 0, 0);
        }

        if(!vAlign.equals(VAlignment.NONE))
        {
            float ny, start = 0, mid = 0, end = 0;
            if(matrixID == Camera.ORTHO2D_S)
            {
                start = -1;
                end = 1;
            }
            else if(matrixID == Camera.ORTHO2D_R)
            {
                start = 0;
                end = res.y;
                mid = res.y/2f;
            }

            if(vAlign.equals(VAlignment.BOTTOM)) ny = start + offset.y;
            else if(vAlign.equals(VAlignment.CENTER)) ny = mid + -getHeight()/2 + offset.y;
            else ny = end - getHeight() + offset.y;

            transform.translateAbs(0, ny - transform.pos.y, 0);
        }
    }

    @Override
    public void onWindowResize(Vector2i res) { align(res); }

    @Override
    public void onMouseMovement(Vector2f scaled, Vector2i unscaled)
    {
        if(isPointInsideObject(scaled))
        {
            if(!mouseHoverStatus) onMouseEntered();

            mouseHoverStatus = true;
        }
        else
        {
            if(mouseHoverStatus) onMouseExited();

            mouseHoverStatus = false;
        }
    }

    public boolean isMouseOver()
    {
        return mouseHoverStatus;
    }
    
    @Override
    public void onRegister(Scene scene)
    {
        super.onRegister(scene);
        align();
    }
}
