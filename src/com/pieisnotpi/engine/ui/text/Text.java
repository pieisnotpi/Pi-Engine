package com.pieisnotpi.engine.ui.text;

import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.text.TextMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.text.TextQuad;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.ui.text.effects.TextEffect;
import com.pieisnotpi.engine.ui.text.font.CharSprite;
import com.pieisnotpi.engine.ui.text.font.Font;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Text extends UiObject<TextQuad>
{
    public String text;
    public List<TextQuad> chars;
    public ArrayList<TextEffect> effects;

    public boolean effectsEnabled = false;
    public float newlineSpace;

    private Color textColor, outlineColor;
    private Font font;
    private TextMaterial material;

    public Text(Font font, String text, Vector3f pos, int matrixID, TextEffect... effects)
    {
        this(font, text, pos, new Color(1, 1, 1), new Color(0, 0, 0), matrixID, effects);
    }

    public Text(Font font, String text, Vector3f pos, Color textColor, Color outlineColor, int matrixID, TextEffect... effects)
    {
        this.matrixID = matrixID;
        this.font = font;
        this.textColor = textColor;
        this.outlineColor = outlineColor;

        if(effects != null)
        {
            this.effects = new ArrayList<>(Arrays.asList(effects));
            this.effects.forEach(e -> e.setText(this));

            effectsEnabled = true;
        }

        chars = new ArrayList<>(100);
        mesh = new Mesh<>(material = new TextMaterial(matrixID, font.getTexture()), transform, MeshConfig.QUAD);
        mesh.setSorting(font.needsSorted);
        transform.setTranslate(pos.x, pos.y, pos.z);
        chars = mesh.renderables;

        setText(text);
    }

    public void update(float timeStep)
    {
        if(effectsEnabled)
        {
            effects.forEach(tf -> tf.process(timeStep));
            mesh.flagForBuild();
        }
        super.update(timeStep);
    }

    public Color getTextColor()
    {
        return textColor;
    }
    
    public Color getOutlineColor()
    {
        return outlineColor;
    }
    
    public int getOutlineSize()
    {
        return material.outlineSize;
    }
    
    public String getText()
    {
        return text;
    }

    public Font getFont()
    {
        return font;
    }

    public List<TextQuad> getChars()
    {
        return chars;
    }

    public void setTextColor(Color textColor)
    {
        this.textColor = textColor;
        chars.forEach(c -> c.setQuadTextColor(textColor));
        mesh.flagForBuild();
    }
    
    public void setOutlineColor(Color outlineColor)
    {
        this.outlineColor = outlineColor;
        chars.forEach(c -> c.setQuadOutlineColor(textColor));
        mesh.flagForBuild();
    }
    
    public void setOutlineSize(int size)
    {
        if(size < 0) size = 0;
        material.outlineSize = size;
    }

    public void setOutlineSmoothing(boolean smooth)
    {
        material.outlineSmoothing = smooth;
    }

    public void setText(String value)
    {
        if(value == null || value.equals(text) || value.equals("")) return;
        size.set(0);

        text = value;
        chars.forEach(TextRenderable::nullify);
        chars.clear();

        float xOffset = 0, yOffset = 0, maxX = Float.MIN_VALUE, maxY = -Float.MIN_VALUE;

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

            xOffset += (sprite.sizeX - font.condensingFactor);

            maxX = Float.max(maxX, x0 + x1);
            maxY = Float.max(maxY, y0 + y1);

            chars.add(new TextQuad(x0, y0, 0.0001f*i, x1, y1, sprite, textColor, outlineColor, line));
        }

        size.set(maxX, maxY, 0);
        transform.setCenter(size.x/2, size.y/2, 0);

        if(effectsEnabled) effects.forEach(TextEffect::onTextUpdated);
        mesh.flagForBuild();
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

    public static float approxWidth(String text, Font font)
    {
        float x = 0, maxX = Float.MIN_VALUE;

        for(int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);

            if(c == ' ' && i != text.length() - 1)
            {
                x += font.spaceCharSpace;
                continue;
            }
            if(c == '\n' && i != text.length() - 1)
            {
                x = 0;
                continue;
            }

            CharSprite sprite = font.getCharSprite(c);

            if(sprite == font.nullChar) continue;

            x += (sprite.sizeX - 1);
            maxX = Float.max(maxX, x);
        }

        return maxX;
    }

    public void destroy()
    {
        super.destroy();
        mesh.destroy();
    }
}
