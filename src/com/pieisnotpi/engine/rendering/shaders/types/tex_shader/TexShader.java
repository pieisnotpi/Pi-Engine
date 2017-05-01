package com.pieisnotpi.engine.rendering.shaders.types.tex_shader;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.window.Window;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TexShader extends ShaderProgram
{
    public static final int ID = 11246;

    public TexShader(Window window)
    {
        super(window, new ShaderFile("/assets/shaders/textured.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/textured.frag", GL_FRAGMENT_SHADER));
    }

    @Override
    public void bindPMUniforms(Camera camera, Mesh mesh)
    {
        super.bindPMUniforms(camera, mesh);
        TexMaterial m = (TexMaterial) mesh.material;
        for(int i = 0; i < m.textures.length; i++) m.textures[i].bind(i);
    }
}
