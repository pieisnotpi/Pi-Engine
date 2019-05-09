package com.pieisnotpi.engine.ui.text.font;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.image.Image;
import com.pieisnotpi.engine.output.Logger;
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

        int midShift = charShift/2;
        int w = metrics.charsWidth(sequence, 0, sequence.length) + sequence.length*charShift;
        int h = metrics.getHeight() + charShift;

        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if(antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setPaint(Color.WHITE);

        if(antiAlias) needsSorted = true;
        texture = new Texture(org.lwjgl.opengl.GL11.glGenTextures(), antiAlias ? Texture.FILTER_LINEAR : Texture.FILTER_NEAREST);

        for(int i = 0, x = midShift, cw, ch = metrics.getHeight(); i < sequence.length; i++, x += cw + charShift)
        {
            char c = sequence[i];
            cw = metrics.charWidth(c);
            g.drawString(Character.toString(c), x, metrics.getAscent() + midShift);
            sprites.put(c, new CharSprite(w, h, x - midShift, 0, x + cw + midShift, ch + charShift, 0, 0));
        }

        g.dispose();

        texture.setImage(new Image(image));

        spaceCharSpace = metrics.charWidth(' ');
        newLineSpace = texture.image.height - charShift;
        condensingFactor = charShift;

        PiEngine.glInstance.registerFont(name, this);
    }

    private static void buildSequence()
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
        return getFont(family, size, style, antiAlias, size/2, 2);
    }

    public static SystemFont getFont(String family, int size, int style, boolean antiAlias, int charShift, int uvShift)
    {
        String name = String.format("%s,%d,%d,%b,%d,%d", family, size, style, antiAlias, charShift, uvShift);
        Font f = PiEngine.glInstance.getFont(name);

        if(f != null) return (SystemFont) f;
        else
        {
            Logger.SYSTEM.debug("Creating new font: '" + name + '\'');
            return new SystemFont(name, family, size, style, antiAlias, charShift, uvShift);
        }
    }
}
