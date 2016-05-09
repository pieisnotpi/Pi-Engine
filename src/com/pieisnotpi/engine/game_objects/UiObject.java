package com.pieisnotpi.engine.game_objects;

import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class UiObject extends GameObject
{
    public float width, height, scale;
    public boolean mouseHoverStatus = false;
    private Vector2f lastCursorPos = new Vector2f(-10, -10);
    private Vector3f c0 = new Vector3f(), c1 = new Vector3f();

    @Override
    public void onMouseMovement(Vector2f cursorPos)
    {
        lastCursorPos.set(cursorPos);

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
        c0.set(x, y, z)/*.mul(scene.window.getMatrix(matrixID))*/;
        c1.set(width, height, 0)/*.mul(scene.window.getMatrix(matrixID))*/;

        return (point.x >= c0.x && point.x <= c0.x + c1.x && point.y >= c0.y && point.y <= c0.y + c1.y);
    }

    @Override
    public boolean isPointInsideObject(Vector3f point)
    {
        /*c0.set(x, y, z).mul(scene.window.getMatrix(matrixID));
        c1.set(width, height, 0).mul(scene.window.getMatrix(matrixID));*/

        return (point.x >= c0.x && point.x <= c0.x + c1.x && point.y >= c0.y && point.y <= c0.y + c1.y);
    }
}
