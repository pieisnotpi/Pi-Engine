package com.pieisnotpi.engine.rendering.ui.text;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.UiObject;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shapes.types.text.TextQuad;
import com.pieisnotpi.engine.rendering.ui.text.effects.TextEffect;
import com.pieisnotpi.engine.rendering.ui.text.font.CharSprite;
import com.pieisnotpi.engine.rendering.ui.text.font.Font;
import com.pieisnotpi.engine.rendering.ui.text.font.PixelFont;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Text extends UiObject
{
    public String text;
    public ArrayList<TextQuad> chars;
    public ArrayList<TextEffect> effects;
    public Color textColor, outlineColor;
    public Font font;

    public boolean effectsEnabled = false;
    public float newlineSpace;

    public static PixelFont pixelFont = new PixelFont();
    public int shaderID, alignmentID = -1;

    public static final int CENTERED = 0, LEFT = 1, RIGHT = 2;
    private float ratio = 1;

    public Text(String text, float scale, float x, float y, float z, int matrixID, Scene scene, TextEffect... effects)
    {
        this(text, scale, x, y, z, new Color(1, 1, 1), new Color(0, 0, 0), matrixID, scene, effects);
    }

    public Text(String text, float scale, float x, float y, float z, Color textColor, Color outlineColor, int matrixID, Scene scene, TextEffect... effects)
    {
        this.scale = scale;
        this.x = x;
        this.y = y;
        this.z = z;
        this.matrixID = matrixID;
        this.font = pixelFont;
        this.scene = scene;
        this.textColor = textColor;
        this.outlineColor = outlineColor;

        shaderID = PiEngine.TEXT_ID;

        chars = new ArrayList<>();
        if(effects != null) this.effects = new ArrayList<>(Arrays.asList(effects));
        scene.gameObjects.add(this);

        effectsEnabled = true;

        setText(text);
    }

    public void update()
    {
        if(effectsEnabled) effects.forEach(textEffect -> textEffect.process(this));
        super.update();
    }

    public Font getFont()
    {
        return font;
    }

    public List<TextQuad> getChars()
    {
        return chars;
    }

    public void setX(float value)
    {
        if(x == value) return;

        float dif = value - x;

        width += dif;

        for(TextQuad c : chars) c.setX(dif + c.getX());

        super.setX(value);
    }

    public void setY(float value)
    {
        if(y == value) return;

        float dif = value - y;

        height += dif;

        for(TextQuad c : chars) c.setY(dif + c.getY());

        super.setY(value);
    }

    public void setZ(float value)
    {
        if(z == value) return;

        float t = 0;
        for(TextQuad c : chars) c.setZ(value + (t+=0.001f));

        super.setZ(value);
    }

    public void addToXRot(float amount)
    {
        for(TextQuad c : chars) c.rotateX(amount, getCy(), getCz());

        super.addToXRot(amount);
    }

    public void addToYRot(float amount)
    {
        for(TextQuad c : chars) c.rotateY(amount, getCx(), getCz());

        super.addToYRot(amount);
    }

    public void addToZRot(float amount)
    {
        for(TextQuad c : chars) c.rotateZ(amount, getCx(), getCy());

        super.addToZRot(amount);
    }

    public void setText(String value)
    {
        if(value.equals(text) || !enabled) return;

        unregister();

        float xr = xRot, yr = yRot, zr = zRot;

        xRot = 0;
        yRot = 0;
        zRot = 0;

        text = value;
        chars.clear();

        width = height = 0;

        float actual = scale*font.pixelScale, xOffset = x, yOffset = y, maxX = Float.MIN_VALUE, maxY = -Float.MIN_VALUE, t = y;

        newlineSpace = font.newLineSpace*actual;
        int line = 0;

        for(int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);

            if(c == ' ' && i != text.length() - 1)
            {
                xOffset += font.spaceCharSpace*actual;
                continue;
            }

            if(c == '\n' && i != text.length() - 1)
            {
                xOffset = x;
                yOffset = y -= newlineSpace;
                line++;
            }

            CharSprite sprite = font.getCharSprite(c);

            if(sprite.equals(font.nullChar)) continue;

            float x0 = xOffset + sprite.offsetX*actual, y0 = yOffset + sprite.offsetY*actual, x1 = sprite.sizeX*actual, y1 = sprite.sizeY*actual;

            xOffset += (sprite.sizeX + font.letterSpace)*actual;

            maxX = Float.max(maxX, x0 + x1);
            maxY = Float.max(maxY, y0 + y1);

            chars.add(new TextQuad(x0, y0, z + 0.0001f*i, x1, y1, sprite, textColor, outlineColor, line, matrixID, scene));
        }

        width = maxX - x;
        height = maxY - y;

        setY(t);

        defaultCenter();

        if(xr != 0) addToYRot(xr);
        if(yr != 0) addToYRot(yr);
        if(zr != 0) addToZRot(zr);

        align();
        register();
    }

    public void setFont(Font font)
    {
        this.font = font;
        String t = text;
        text = "";
        setText(t);
    }

    public void onWindowResize(Vector2i res)
    {
        ratio = (float) res.x/res.y;
        align();
    }

    private void align()
    {
        if(alignmentID == 0) setX(-width/2);
        else if(alignmentID == 1) setX(-ratio + 0.05f);
        else if(alignmentID == 2) setX(ratio - width - 0.05f);
    }

    public void enable()
    {
        super.enable();

        register();
    }

    public void disable()
    {
        super.disable();

        unregister();
    }

    public void register()
    {
        chars.forEach(TextQuad::register);
    }

    public void unregister()
    {
        chars.forEach(TextQuad::unregister);
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        unregister();
    }

    public static float approxWidth(String text, int scale, Font font)
    {
        float actual = scale*font.pixelScale;

        float x = 0, maxX = Float.MIN_VALUE;

        for(int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);

            if(c == ' ' && i != text.length() - 1)
            {
                x += 4*actual;
                continue;
            }
            if(c == '\n' && i != text.length() - 1)
            {
                x = 0;
                continue;
            }

            CharSprite sprite = font.getCharSprite(c);

            if(sprite == font.nullChar) continue;

            x += (sprite.sizeX - 1)*actual;
            maxX = Float.max(maxX, x);
        }

        return maxX;
    }

    public void destroy()
    {
        super.destroy();
        unregister();
    }
}
