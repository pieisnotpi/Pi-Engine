package com.pieisnotpi.engine.rendering.shaders.types.text_shader;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TextShader extends ShaderProgram
{
    public static final int ID = 73412;

    public TextShader()
    {
        super(new ShaderFile("/assets/shaders/text.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/text.frag", GL_FRAGMENT_SHADER));
    }

    public void bindPMUniforms(Camera camera, Mesh mesh)
    {
        super.bindPMUniforms(camera, mesh);
        TextMaterial m = (TextMaterial) mesh.material;
        for(int i = 0; i < m.textures.length; i++) m.textures[i].bind(i);
    }
}
