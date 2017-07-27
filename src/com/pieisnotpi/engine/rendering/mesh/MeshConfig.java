package com.pieisnotpi.engine.rendering.mesh;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;

public class MeshConfig
{
    public static final MeshConfig
        
        QUAD = new MeshConfig(GL_TRIANGLE_STRIP, 4, false),
        QUAD_STATIC = new MeshConfig(GL_TRIANGLE_STRIP, 4, true),
        TRIANGLE = new MeshConfig(GL_TRIANGLES, 3, false),
        TRIANGLE_STATIC = new MeshConfig(GL_TRIANGLES, 3, true);
    
    public int drawMode, vpr;
    public boolean isStatic;
    
    public MeshConfig(int drawMode, int vertsPerRenderable, boolean isStatic)
    {
        this.drawMode = drawMode;
        this.vpr = vertsPerRenderable;
        this.isStatic = isStatic;
    }
}
