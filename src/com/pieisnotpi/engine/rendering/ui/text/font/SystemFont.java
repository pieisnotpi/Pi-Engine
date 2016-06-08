package com.pieisnotpi.engine.rendering.ui.text.font;

import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SystemFont extends Font
{
    private FontMetrics metrics;
    private String sequence = "";

    public static final int PLAIN = java.awt.Font.PLAIN, BOLD = java.awt.Font.BOLD;

    public SystemFont(String family, int size, int style, boolean antiAlias)
    {
        final int start = 33, end = 191;

        StringBuilder builder = new StringBuilder(256 - 32);
        for(int i = start; i < end; i++) if(i != 127) builder.append((char) i).append((char) 32);
        sequence = builder.toString();

        java.awt.Font font = new java.awt.Font(family, style, size);
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if(antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);

        metrics = g.getFontMetrics();
        g.dispose();

        int w = metrics.charsWidth(sequence.toCharArray(), 0, sequence.length()), h = metrics.getHeight();
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if(antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setPaint(Color.WHITE);
        g.drawString(sequence, 0, metrics.getAscent());
        g.dispose();

        texture = new Texture(image);
        texture.hasTransparency = true;

        pixelScale = 0.006f/metrics.charWidth('A');
        spaceCharSpace = metrics.charWidth(' ');
        letterSpace = -spaceCharSpace/3;
        newLineSpace = texture.height;
        nullChar = new CharSprite(new Sprite(texture, 0, 0, 0, 0), ' ', 0, 0);

        init();
    }

    public SystemFont(String family, int size, int style, boolean antiAlias, float letterSpace, float newLineSpace)
    {
        final int start = 33, end = 191;

        StringBuilder builder = new StringBuilder(256 - 32);
        for(int i = start; i < end; i++) if(i != 127) builder.append((char) i).append((char) 32);
        sequence = builder.toString();

        java.awt.Font font = new java.awt.Font(family, style, size);
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if(antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);

        metrics = g.getFontMetrics();
        g.dispose();

        int w = metrics.charsWidth(sequence.toCharArray(), 0, sequence.length()), h = metrics.getHeight();
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if(antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setPaint(Color.WHITE);
        g.drawString(sequence, 0, metrics.getAscent());
        g.dispose();

        texture = new Texture(image);
        texture.hasTransparency = true;

        pixelScale = 0.006f/metrics.charWidth('A');
        spaceCharSpace = metrics.charWidth(' ');
        this.letterSpace = letterSpace;
        this.newLineSpace = newLineSpace;
        nullChar = new CharSprite(new Sprite(texture, 0, 0, 0, 0), ' ', 0, 0);

        init();
    }

    protected void init()
    {
        int x = 0;

        for(int i = 0; i < sequence.length(); i++)
        {
            int w = metrics.charWidth(sequence.charAt(i));
            sprites.add(new CharSprite(new Sprite(texture, x, 0, (x += w) + w/5, texture.height), sequence.charAt(i), 0, 0));
        }
    }
}
