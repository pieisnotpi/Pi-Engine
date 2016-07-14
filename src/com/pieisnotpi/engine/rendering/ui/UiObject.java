package com.pieisnotpi.engine.rendering.ui;

import com.pieisnotpi.engine.scene.GameObject;
import org.joml.Vector2f;

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
        return (point.x >= pos.x && point.x <= pos.x + size.x && point.y >= pos.y && point.y <= pos.y + size.y);
    }
}
