package com.pieisnotpi.engine.rendering.ui.text;

import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.Mesh;
import com.pieisnotpi.engine.rendering.shaders.types.text_shader.TextMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.text_shader.TextQuad;
import com.pieisnotpi.engine.rendering.ui.UiObject;
import com.pieisnotpi.engine.rendering.ui.text.effects.TextEffect;
import com.pieisnotpi.engine.rendering.ui.text.font.CharSprite;
import com.pieisnotpi.engine.rendering.ui.text.font.Font;
import com.pieisnotpi.engine.rendering.ui.text.font.PixelFont;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Text extends UiObject
{
    public String text;

    public ArrayList<TextQuad> chars;
    public ArrayList<TextEffect> effects;

    public boolean effectsEnabled = false;
    public float newlineSpace;

    public static PixelFont pixelFont = new PixelFont();

    private Color textColor, outlineColor;
    private Font font;
    private Mesh mesh;

    public Text(String text, Vector3f pos, int matrixID, Scene scene, TextEffect... effects)
    {
        this(text, pos, new Color(1, 1, 1), new Color(0, 0, 0), matrixID, scene, effects);
    }

    public Text(String text, Vector3f pos, Color textColor, Color outlineColor, int matrixID, Scene scene, TextEffect... effects)
    {
        this.pos = pos;
        this.matrixID = matrixID;
        this.font = pixelFont;
        this.scene = scene;
        this.textColor = textColor;
        this.outlineColor = outlineColor;

        chars = new ArrayList<>(100);
        if(effects != null) this.effects = new ArrayList<>(Arrays.asList(effects));
        scene.gameObjects.add(this);

        mesh = new Mesh(new TextMaterial(matrixID, font.getTexture()), GL11.GL_TRIANGLE_STRIP, 4, false, scene).setTranslate(pos.x, pos.y, pos.z);

        effectsEnabled = true;

        setText(text);
    }

    public void update()
    {
        /*if(effectsEnabled) effects.forEach(textEffect -> textEffect.process(this));
        super.update();*/
    }

    public Mesh getMesh()
    {
        return mesh;
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
        if(pos.x == value) return;

        float dif = value - pos.x;

        mesh.setTranslate(value, pos.y, pos.z);

        super.setX(value);
    }

    public void setY(float value)
    {
        if(pos.y == value) return;

        float dif = value - pos.y;

        mesh.setTranslate(pos.x, value, pos.z);

        super.setY(value);
    }

    public void setZ(float value)
    {
        if(pos.z == value) return;

        float t = 0;
        mesh.setTranslate(pos.x, pos.y, value);

        super.setZ(value);
    }

    public void addToRot(float xr, float yr, float zr)
    {
        /*for(TextQuad c : chars)
        {
            if(xr != 0) c.addToXRot(xr, getCy(), getCz());
            if(yr != 0) c.addToYRot(yr, getCx(), getCz());
            if(zr != 0) c.addToZRot(zr, getCx(), getCy());
        }

        super.addToRot(xr, yr, zr);*/
    }

    public void setText(String value)
    {
        if(value == null || value.equals(text) || value.equals("") || !enabled) return;
        size.set(0);

        text = value;
        chars.clear();

        mesh.destroy();
        float xOffset = 0, yOffset = 0, maxX = Float.MIN_VALUE, maxY = -Float.MIN_VALUE;
        unregister();

        newlineSpace = font.newLineSpace;
        int line = 0;

        for(int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);

            if(c == ' ' && i != text.length() - 1)
            {
                xOffset += font.spaceCharSpace;
                continue;
            }

            if(c == '\n' && i != text.length() - 1)
            {
                xOffset = 0;
                yOffset -= newlineSpace;
                line++;
                continue;
            }

            CharSprite sprite = font.getCharSprite(c);

            if(sprite.equals(font.nullChar)) continue;

            float x0 = xOffset + sprite.offsetX, y0 = yOffset + sprite.offsetY, x1 = sprite.sizeX, y1 = sprite.sizeY;

            xOffset += (sprite.sizeX + font.letterSpace);

            maxX = Float.max(maxX, x0 + x1);
            maxY = Float.max(maxY, y0 + y1);

            chars.add(new TextQuad(x0, y0, pos.z + 0.0001f*i, x1, y1, sprite, textColor, outlineColor, line));
        }

        size.set(maxX - pos.x, maxY - pos.y, 0);
        register();

        build();
        align();
    }

    public void setFont(Font font)
    {
        this.font = font;
        ((TextMaterial) mesh.material).textures[0] = font.getTexture();
        String t = text;
        text = "";
        setText(t);
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
        scene.addMesh(mesh);
    }

    public void unregister()
    {
        scene.removeMesh(mesh);
    }

    public static float approxWidth(String text, float scale, Font font)
    {
        float x = 0, maxX = Float.MIN_VALUE;

        for(int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);

            if(c == ' ' && i != text.length() - 1)
            {
                x += font.spaceCharSpace*scale;
                continue;
            }
            if(c == '\n' && i != text.length() - 1)
            {
                x = 0;
                continue;
            }

            CharSprite sprite = font.getCharSprite(c);

            if(sprite == font.nullChar) continue;

            x += (sprite.sizeX - 1)*scale;
            maxX = Float.max(maxX, x);
        }

        return maxX;
    }

    public void build()
    {
        chars.forEach(r -> mesh.addRenderable(r));
        mesh.build();

        mesh.setTranslate(pos.x, pos.y, pos.z);
    }

    public void destroy()
    {
        super.destroy();
        unregister();
    }
}
