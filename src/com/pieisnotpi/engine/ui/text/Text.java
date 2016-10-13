package com.pieisnotpi.engine.ui.text;

import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.shaders.types.text_shader.TextMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.text_shader.TextQuad;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.ui.text.effects.TextEffect;
import com.pieisnotpi.engine.ui.text.font.CharSprite;
import com.pieisnotpi.engine.ui.text.font.Font;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Text extends UiObject
{
    public String text;
    public List<TextQuad> chars;
    public ArrayList<TextEffect> effects;

    public boolean effectsEnabled = false;
    public float newlineSpace;

    private Color textColor, outlineColor;
    private Font font;
    private Mesh<TextQuad> mesh;

    public Text(Font font, String text, Vector3f pos, int matrixID, Scene scene, TextEffect... effects)
    {
        this(font, text, pos, new Color(1, 1, 1), new Color(0, 0, 0), matrixID, scene, effects);
    }

    public Text(Font font, String text, Vector3f pos, Color textColor, Color outlineColor, int matrixID, Scene scene, TextEffect... effects)
    {
        this.pos = pos;
        this.matrixID = matrixID;
        this.font = font;
        this.scene = scene;
        this.textColor = textColor;
        this.outlineColor = outlineColor;

        if(effects != null)
        {
            this.effects = new ArrayList<>(Arrays.asList(effects));
            this.effects.forEach(e -> e.setText(this));

            effectsEnabled = true;
        }

        chars = new ArrayList<>(100);
        mesh = new Mesh<>(new TextMaterial(matrixID, font.getTexture()), transform, GL11.GL_TRIANGLE_STRIP, 4, false, scene);
        transform.setTranslate(pos.x, pos.y, pos.z);
        chars = mesh.renderables;

        scene.gameObjects.add(this);

        setText(text);
    }

    public void update()
    {
        if(effectsEnabled)
        {
            effects.forEach(TextEffect::process);
            mesh.flagForBuild();
        }
        super.update();
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
        if(transform.pos.x == value) return;

        float dif = value - transform.pos.x;

        if(dif != 0) transform.translateAbs(dif, 0, 0);

        super.setX(value);
    }

    public void setY(float value)
    {
        if(transform.pos.y == value) return;

        float dif = value - transform.pos.y;
        if(dif != 0) transform.translateAbs(0, dif, 0);

        super.setY(value);
    }

    public void setZ(float value)
    {
        if(transform.pos.z == value) return;

        float dif = value - transform.pos.z;
        if(dif != 0) transform.translateAbs(0, 0, dif);

        super.setZ(value);
    }

    public void setText(String value)
    {
        if(value == null || value.equals(text) || value.equals("") || !enabled) return;
        size.set(0);

        text = value;
        chars.clear();

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

            chars.add(new TextQuad(x0, y0, 0.0001f*i, x1, y1, sprite, textColor, outlineColor, line));
        }

        size.set(maxX - transform.pos.x, maxY - transform.pos.y, 0).mul(transform.scale);
        transform.setCenter(size.x/2, size.y/2, 0);
        /*float mx = size.x/2f, my = size.y/2f;

        chars.forEach(c -> c.setPos(-mx + c.getX(), -my + c.getY(), c.getZ()));*/

        register();

        align();
        mesh.flagForBuild();
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
        mesh.build();
    }

    public void destroy()
    {
        super.destroy();
        unregister();
    }
}
