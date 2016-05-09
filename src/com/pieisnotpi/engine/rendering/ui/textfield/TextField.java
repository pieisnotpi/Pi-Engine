package com.pieisnotpi.engine.rendering.ui.textfield;

import com.pieisnotpi.engine.game_objects.UiObject;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shapes.types.colored.ColorQuad;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.scene.Scene;

import static org.lwjgl.glfw.GLFW.*;

public class TextField extends UiObject
{
    private static final float SPACING_CONST = 0.00417f;
    private static Color top = new Color(1, 1, 1), bottom = new Color(0.9f, 0.9f, 0.9f);

    boolean shiftHeld = false, capsLock = false;
    ColorQuad base;
    Text text;

    public TextField(float scale, float x, float y, float z, int matrixID, Scene scene)
    {
        this(scale, x, y, z, "", matrixID, scene);
    }

    public TextField(float scale, float x, float y, float z, String text, int matrixID, Scene scene)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = scale;
        this.matrixID = matrixID;
        this.scene = scene;

        setText(text);

        scene.gameObjects.add(this);
    }

    public void setText(String textValue)
    {
        if(text == null)
        text = new Text(textValue, scale, x + scale*SPACING_CONST, y + scale*SPACING_CONST, z + 0.001f, new Color(0, 0, 0), new Color(1, 1, 1, 0), matrixID, scene);
        else text.setText(textValue);

        width = text.width + scale*SPACING_CONST*2;
        height = text.font.pixelScale*scale*18;

        if(base == null) base = new ColorQuad(x, y, z, width, height, 0, bottom, bottom, top, top, matrixID, scene);
        else
        {
            base.points[0].set(x, y, z);
            base.points[1].set(x + width, y, z);
            base.points[2].set(x, y + height, z);
            base.points[3].set(x + width, y + height, z);
        }
        /*text.setHighlightToChar(0);
        text.extendHighlightToChar(text.chars.size() - 1);*/
    }

    public void onKeyPressed(int key, int mods)
    {
        if(key == GLFW_KEY_ENTER) System.out.println("SUBMIT");
        else if(key == GLFW_KEY_LEFT_SHIFT) shiftHeld = true;
        else if(key == GLFW_KEY_CAPS_LOCK) capsLock = !capsLock;
        else if(key == GLFW_KEY_BACKSPACE && text.text.length() != 0) setText(text.text.substring(0, text.text.length() - 1));
        else
        {
            int charOffset = 'A' - 'a', numbOffset = '1' - '!';

            if(((capsLock && shiftHeld) || (!capsLock && !shiftHeld)) && key >= 'A' && key <= 'Z') key -= charOffset;
            //else if(shiftHeld) key -= numbOffset;

            setText(text.text + (char) key);
        }
    }

    public void onKeyReleased(int key, int mods)
    {
        if(key == GLFW_KEY_LEFT_SHIFT) shiftHeld = false;
    }
}
