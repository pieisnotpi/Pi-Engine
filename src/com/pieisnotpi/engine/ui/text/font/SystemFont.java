package com.pieisnotpi.engine.ui.text.font;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SystemFont extends Font
{
    private static final int start = 33, end = 191;
    private static char[] sequence = new char[end - start - 1];

    public static final int PLAIN = java.awt.Font.PLAIN, BOLD = java.awt.Font.BOLD, ITALIC = java.awt.Font.ITALIC;

    private SystemFont(String name, String family, int size, int style, boolean antiAlias, int charShift, int uvShift)
    {
        buildSequence();

        java.awt.Font font = new java.awt.Font(family, style, size);
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if(antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);

        FontMetrics metrics = g.getFontMetrics();
        g.dispose();

        int w = 0, h = metrics.getHeight();
        for(char c : sequence) w += metrics.charWidth(c) + charShift + uvShift;

        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if(antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setPaint(Color.WHITE);

        if(antiAlias) needsSorted = true;
        texture = new Texture(org.lwjgl.opengl.GL11.glGenTextures(), w, h, antiAlias ? Texture.FILTER_LINEAR : Texture.FILTER_NEAREST);

        for(int i = 0, x = 0, cw, ch = metrics.getHeight(); i < sequence.length; i++, x += cw + charShift)
        {
            char c = sequence[i];
            cw = metrics.charWidth(c);
            g.drawString(c + "", x, metrics.getAscent());
            sprites.add(new CharSprite(new Sprite(texture, x, 0, x + cw + uvShift, ch), c, 0, 0));
        }

        g.dispose();

        texture.compileTexture(image);

        spaceCharSpace = metrics.charWidth(' ');
        letterSpace = -spaceCharSpace/6;
        newLineSpace = texture.height;
        nullChar = new CharSprite(new Sprite(texture, 0, 0, 0, 0), ' ', 0, 0);

        PiEngine.glInstance.fonts.put(name, this);
    }

    protected static void buildSequence()
    {
        if(sequence[0] == 0)
        {
            for(int i = 0, c = start; c < end; i++, c++)
            {
                if(c == 127) i--;
                else sequence[i] = (char) c;
            }
        }
    }

    public static SystemFont getFont(String family, int size, int style, boolean antiAlias)
    {
        return getFont(family, size, style, antiAlias, 10, 0);
    }

    public static SystemFont getFont(String family, int size, int style, boolean antiAlias, int charShift, int uvShift)
    {
        String name = String.format("%s,%d,%d,%b,%d,%d", family, size, style, antiAlias, charShift, uvShift);
        Font f = PiEngine.glInstance.fonts.get(name);

        if(f != null) return (SystemFont) f;
        else
        {
            Logger.SYSTEM.debug("Creating new font: '" + name + '\'');
            return new SystemFont(name, family, size, style, antiAlias, charShift, uvShift);
        }
    }
}
