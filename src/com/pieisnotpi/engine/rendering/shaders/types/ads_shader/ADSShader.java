package com.pieisnotpi.engine.rendering.shaders.types.ads_shader;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.cameras.CameraMatrix;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class ADSShader extends ShaderProgram
{
    public static final int ID = 62345;

    private Light[] lights = new Light[4];
    private Matrix4f view = new Matrix4f();
    private Matrix3f normal = new Matrix3f();
    private Vector4f lp = new Vector4f();

    public ADSShader()
    {
        super(new ShaderFile("/assets/shaders/ads.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/ads.frag", GL_FRAGMENT_SHADER));

        lights[0] = new Light(new Vector3f(10, 5, 10), new Vector3f(2, 0, 0));
        lights[1] = new Light(new Vector3f(10, 2, -10), new Vector3f(0, 2, 0));
        lights[2] = new Light(new Vector3f(-10, 2, 10), new Vector3f(0, 0, 2));
        lights[3] = new Light(new Vector3f(-10, 2, -10), new Vector3f(2, 2, 2));
    }

    public void bindUniforms(Camera camera)
    {
        view.setLookAt(camera.getPos(), camera.getLookAt(), camera.getUp());

        for(int i = 0; i < lights.length; i++)
        {
            Light l = lights[i];

            if(l == null) continue;

            lp.set(l.pos.x, -l.pos.y, l.pos.z, 1);
            lp.mul(view);

            setUniformVec4(String.format("lights[%d].Position", i), lp);
            setUniformVec3(String.format("lights[%d].Intensity", i), l.intensity);
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

    public class Light
    {
        public Vector3f pos;
        public Vector3f intensity;

        public Light(Vector3f position, Vector3f intensity)
        {
            pos = position;
            this.intensity = intensity;
        }
    }
}
