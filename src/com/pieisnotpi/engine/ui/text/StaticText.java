package com.pieisnotpi.engine.ui.text;

import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.utility.Color;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StaticText extends UiObject
{
    public String text;

    private boolean antiAlias;

    private Font font;
    private TexQuad quad;
    private BufferedImage image;
    private Color textColor;
    private FontMetrics metrics;

    public StaticText(String text, Font font, boolean antiAlias, float scale, float x, float y, float z, Color textColor, int matrixID, Scene scene)
    {
        /*pos.set(x, y, z);
        this.scale = scale;
        this.matrixID = matrixID;
        this.scene = scene;
        this.antiAlias = antiAlias;
        this.textColor = textColor;
        this.text = text;

        setFont(font);*/
    }

    public void setFont(Font font)
    {
        this.font = font;

        image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if(antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        metrics = g.getFontMetrics();
        g.dispose();

        setText(text);
    }

    public void setText(String text)
    {
        this.text = text;

        int w = metrics.charsWidth(text.toCharArray(), 0, text.length()), h = metrics.getHeight();
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if(antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setPaint(new java.awt.Color(textColor.red, textColor.green, textColor.blue, textColor.alpha));
        g.drawString(text, 0, metrics.getAscent());
        g.dispose();

        float r = (float) image.getWidth()/image.getHeight();

        Texture texture = new Texture(image);

        quad = new TexQuad(pos.x, pos.y, pos.z, 5/texture.height, 5/texture.width, 0, new Sprite(texture.width, texture.height, 0, 0, texture.width, texture.height));
    }

    public void setX(float x)
    {
        quad.setX(x);
        super.setX(x);
    }

    public void setY(float y)
    {
        quad.setY(y);
        super.setY(y);
    }

    public void setZ(float z)
    {
        quad.setZ(z);
        super.setZ(z);
    }

    @Override
    public void addToRot(float xr, float yr, float zr)
    {
        if(xr != 0) quad.addToXRot(xr);
        if(yr != 0) quad.addToYRot(yr);
        if(zr != 0) quad.addToZRot(zr);

        super.addToRot(xr, yr, zr);
    }
}