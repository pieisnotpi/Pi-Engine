package com.pieisnotpi.engine.rendering.shaders.types;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Renderable;
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
    private Vector3f kd = new Vector3f(0.2f), ks = new Vector3f(0.2f), ka = new Vector3f(0.2f);
    private float shininess = 10;

    public ADSShader()
    {
        super(new ShaderFile("/assets/shaders/ads.vert", GL_VERTEX_SHADER), new ShaderFile("/assets/shaders/ads.frag", GL_FRAGMENT_SHADER));

        vertices = new Attribute("VertexPosition", 3);
        normals = new Attribute("VertexNormal", 3);
        texCoords = new Attribute("VertexTexCoord", 2);
        array = new VertexArray(vertices, normals, texCoords);
        perspName = "MVP";

        lights[0] = new Light(new Vector3f(10, 5, 10), new Vector3f(2, 0, 0));
        lights[1] = new Light(new Vector3f(10, 2, -10), new Vector3f(0, 2, 0));
        lights[2] = new Light(new Vector3f(-10, 2, 10), new Vector3f(0, 0, 2));
        lights[3] = new Light(new Vector3f(-10, 2, -10), new Vector3f(2, 2, 2));
    }

    public void bindUniforms(Camera camera)
    {
        mv.setLookAt(camera.getPos(), camera.getLookAt(), camera.getUp());
        Matrix4f mvp = camera.matrices[PiEngine.C_PERSPECTIVE];

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
        // Diffuse
        setUniformVec3("Kd", kd);
        // Ambient
        setUniformVec3("Ka", ka);
        // Specular
        setUniformVec3("Ks", ks);
        setUniformFloat("Shininess", shininess);
    }

    @Override
    protected void putElements(List<Renderable> buffer)
    {
        buffer.forEach(r ->
        {
            normalize(r.points, r.normals);
            BufferUtility.putVec3s(vertices.buffer, r.points);
            BufferUtility.putVec3s(normals.buffer, r.normals);
            BufferUtility.putVec2s(texCoords.buffer, r.texCoords);
        });
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

    private void normalize(Vector3f[] points, Vector3f[] normals)
    {
        for(int i = 0; i < points.length; i++) points[i].normalize(normals[i]);
    }
}
