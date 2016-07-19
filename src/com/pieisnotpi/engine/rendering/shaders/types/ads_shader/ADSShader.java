package com.pieisnotpi.engine.rendering.shaders.types.ads_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.rendering.shaders.Attribute;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.utility.BufferUtility;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class ADSShader extends ShaderProgram
{
    private Attribute vertices, normals, texCoords;
    private Light[] lights = new Light[4];
    private Matrix4f mv = new Matrix4f();
    private Matrix3f normal = new Matrix3f();
    private Vector4f lp = new Vector4f();
    private Vector3f kd = new Vector3f(-1), ks = new Vector3f(-1), ka = new Vector3f(-1);
    private float s = -1;

    public ADSShader()
    {
        super(new ShaderFile("/assets/shaders/ads.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/ads.frag", GL_FRAGMENT_SHADER));

        unsortedArray = new VertexArray(new Attribute("VertexPosition", 3), new Attribute("VertexNormal", 3), new Attribute("VertexTexCoord", 2));
        sortedArray = new VertexArray(new Attribute("VertexPosition", 3), new Attribute("VertexNormal", 3), new Attribute("VertexTexCoord", 2));
        perspName = "MVP";

        lights[0] = new Light(new Vector3f(10, 5, 10), new Vector3f(2, 0, 0));
        lights[1] = new Light(new Vector3f(10, 2, -10), new Vector3f(0, 2, 0));
        lights[2] = new Light(new Vector3f(-10, 2, 10), new Vector3f(0, 0, 2));
        lights[3] = new Light(new Vector3f(-10, 2, -10), new Vector3f(2, 2, 2));
    }

    public void bindUniforms(Camera camera)
    {
        mv.setLookAt(camera.getPos(), camera.getLookAt(), camera.getUp());
        Matrix4f mvp = camera.getMatrix(PiEngine.C_PERSPECTIVE);

        for(int i = 0; i < lights.length; i++)
        {
            Light l = lights[i];

            if(l == null) continue;

            lp.set(l.pos.x, -l.pos.y, l.pos.z + 1, 1);
            lp.mul(mv);
            String name = "lights[" + i + "].";

            setUniformVec4(name + "Position", lp);
            setUniformVec3(name + "Intensity", l.intensity);
        }

        mvp.normal(normal);

        setUniformMat4("ModelViewMatrix", mv);
        setUniformMat3("NormalMatrix", normal);
    }

    @Override
    public void bindPRUniforms(Camera camera, Renderable r)
    {
        super.bindPRUniforms(camera, r);
        if(Window.lastTextureID != r.texture.getTexID()) r.texture.bind(0);
        ADSMaterial m = (ADSMaterial) r.material;

        if(!compare(ka, m.ka)) setUniformVec3("Ka", ka.set(m.ka));
        if(!compare(kd, m.kd)) setUniformVec3("Kd", kd.set(m.kd));
        if(!compare(ks, m.ks)) setUniformVec3("Ks", ks.set(m.ks));
        if(s != m.s) setUniformFloat("Shininess", s = m.s);
    }

    @Override
    protected void putElements(List<Renderable> buffer, Attribute[] a)
    {
        buffer.forEach(r ->
        {
            BufferUtility.putVec3s(a[0].buffer, r.points);
            BufferUtility.putVec3s(a[1].buffer, r.normals);
            BufferUtility.putVec2s(a[2].buffer, r.texCoords);
        });
    }

    @Override
    public void clearUnsorted()
    {
        super.clearUnsorted();

        ka.set(-1);
        kd.set(-1);
        ks.set(-1);
        s = -1;
    }

    @Override
    public void clearSorted()
    {
        super.clearSorted();

        ka.set(-1);
        kd.set(-1);
        ks.set(-1);
        s = -1;
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

    private boolean compare(Vector3f v0, Vector3f v1)
    {
        return v0.hashCode() == v1.hashCode() || (v0.x == v1.x && v0.y == v1.y && v0.z == v1.z);
    }
}
