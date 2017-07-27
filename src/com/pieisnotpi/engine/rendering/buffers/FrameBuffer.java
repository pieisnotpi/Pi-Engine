package com.pieisnotpi.engine.rendering.buffers;

import com.pieisnotpi.engine.rendering.textures.Texture;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer
{
    public Texture texture;
    public Vector2i res;

    private int fboHandle, texHandle, renderHandle;

    public FrameBuffer(Vector2i res)
    {
        fboHandle = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboHandle);

        texHandle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texHandle);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, res.x, res.y, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    
        renderHandle = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, renderHandle);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, res.x, res.y);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderHandle);
        
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texHandle, 0);
        
        glDrawBuffers(GL_COLOR_ATTACHMENT0);
        
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        texture = new Texture(texHandle);
        texture.res = res;

        this.res = res;
    }

    public void setRes(int width, int height)
    {
        if(res.x == width && res.y == height) return;
        
        res.set(width, height);
    
        glBindFramebuffer(GL_FRAMEBUFFER, fboHandle);
        
        texture.bind(4);
        glBindRenderbuffer(GL_RENDERBUFFER, renderHandle);
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
    }

    public void bind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, fboHandle);
    }

    public void unbind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getTexHandle()
    {
        return texHandle;
    }
}
