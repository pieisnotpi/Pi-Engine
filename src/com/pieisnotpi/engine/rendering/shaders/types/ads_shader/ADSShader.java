package com.pieisnotpi.engine.rendering.shaders.types.ads_shader;

import com.pieisnotpi.engine.rendering.Light;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.cameras.CameraMatrix;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.window.Window;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

public class ADSShader extends ShaderProgram
{
    public static final int ID = 62345;

    private Matrix3f normal = new Matrix3f();

    public ADSShader(Window window)
    {
        super(window, ShaderFile.getShaderFile("ads.vert", ShaderFile.TYPE_VERT), ShaderFile.getShaderFile("ads.frag", ShaderFile.TYPE_FRAG));
    }

    public void bindUniforms(Camera camera)
    {
        Matrix4f view = camera.getView();

        List<Light> lights = window.scene.lights;

        int limit = lights.size() > 16 ? 16 : lights.size();

        for(int i = 0; i < limit; i++)
        {
            Light l = lights.get(i);

            if(l == null) continue;

            l.bindUniforms(view, this);
        }

        setUniformMat4("ViewMatrix", view);
    }

    @Override
    public void bindPMUniforms(Camera camera, Mesh mesh)
    {
        ADSMaterial m = (ADSMaterial) mesh.material;

        CameraMatrix mvp = camera.getMatrix(m.matrixID);
        mvp.getMatrix().normal(normal);

        setUniformMat4("MVP", mvp.getBuffer());
        setUniformMat3("NormalMatrix", normal);
        setUniformMat4("ModelMatrix", mesh.getTransform().getBuffer());
        setUniformVec3("m.Ka", m.ka);
        setUniformVec3("m.Kd", m.kd);
        setUniformVec3("m.Ks", m.ks);
        setUniformFloat("m.Shininess", m.s);

        for(int i = 0; i < m.textures.length; i++) m.textures[i].bind(i);
    }
}
