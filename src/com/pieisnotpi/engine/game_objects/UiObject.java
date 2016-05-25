package com.pieisnotpi.engine.game_objects;

import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class UiObject extends GameObject
{
    public float scale;
    public boolean mouseHoverStatus = false;
    private Vector2f lastCursorPos = new Vector2f(-10, -10);

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
        return (point.x >= x && point.x <= x + width && point.y >= y && point.y <= y + height);
    }

    @Override
    public boolean isPointInsideObject(Vector3f point)
    {
        return (point.x >= x && point.x <= x + width && point.y >= y && point.y <= y + height && point.z >= z && point.z <= z + depth);
    }
}
