package com.pieisnotpi.engine.ui;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.scene.GameObject;
import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class UiObject extends GameObject
{
    public enum HAlignment { NONE, LEFT, CENTER, RIGHT }
    public enum VAlignment { NONE, BOTTOM, CENTER, TOP }

    private HAlignment hAlign = HAlignment.NONE;
    private VAlignment vAlign = VAlignment.NONE;
    private Vector2f offset = new Vector2f();
    private boolean shouldAlign = false;

    public boolean mouseHoverStatus = false;

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

    protected void align()
    {
        if(scene.window != null) align(scene.window.getWindowRes());
    }

    protected void align(Vector2i res)
    {
        if(!shouldAlign) return;

        if(!hAlign.equals(HAlignment.NONE))
        {
            float nx = pos.x, start = 0, end = 0;

            if(matrixID == PiEngine.M_ORTHO2D_S_ID)
            {
                start = -scene.window.ratio;
                end = scene.window.ratio;
            }
            else if(matrixID == PiEngine.M_ORTHO2D_R_ID)
            {
                start = 0;
                end = scene.window.getWindowRes().x;
            }

            if(hAlign.equals(HAlignment.LEFT)) nx = start + offset.x;
            else if(hAlign.equals(HAlignment.CENTER)) nx = -size.x/2;
            else if(hAlign.equals(HAlignment.RIGHT)) nx = end - size.x + offset.x;

            if(nx != pos.x) setX(nx);
        }

        if(!vAlign.equals(VAlignment.NONE))
        {
            float ny = pos.y, start = 0, end = 0;
            if(matrixID == PiEngine.M_ORTHO2D_S_ID)
            {
                start = -1;
                end = 1;
            }
            else if(matrixID == PiEngine.M_ORTHO2D_R_ID)
            {
                start = 0;
                end = scene.window.getWindowRes().y;
            }

            if(vAlign.equals(VAlignment.BOTTOM)) ny = start + offset.y;
            else if(vAlign.equals(VAlignment.CENTER)) ny = -size.y/2;
            else if(vAlign.equals(VAlignment.TOP)) ny = end - size.y + offset.y;

            if(ny != pos.y) setY(ny);
        }
    }

    @Override
    public void onWindowResize(Vector2i res) { align(res); }

    @Override
    public void onMouseMovement(Vector2f cursorPos)
    {
        if(isPointInsideObject(cursorPos))
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

    @Override
    public boolean isPointInsideObject(Vector2f point)
    {
        return (point.x >= pos.x && point.x <= pos.x + size.x && point.y >= pos.y && point.y <= pos.y + size.y);
    }
}
