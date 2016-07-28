package com.pieisnotpi.engine.rendering.shaders.types.text_shader;

import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Mesh;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.shaders.Attribute;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.ui.text.TextRenderable;
import com.pieisnotpi.engine.utility.BufferUtility;

import java.util.List;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TextShader extends ShaderProgram
{
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

    protected void putElements(List<Renderable> buffer, Attribute[] a)
    {
        buffer.forEach(r ->
        {
            TextRenderable t = (TextRenderable) r;

            BufferUtility.putVec3s(a[0].buffer, t.points);
            BufferUtility.putVec2s(a[1].buffer, t.texCoords);
            BufferUtility.putColors(a[2].buffer, t.textColors);
            BufferUtility.putColors(a[3].buffer, t.outlineColors);
        });
    }
}
