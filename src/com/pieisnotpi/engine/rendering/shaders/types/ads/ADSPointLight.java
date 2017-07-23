package com.pieisnotpi.engine.rendering.shaders.types.ads;

import com.pieisnotpi.engine.rendering.Light;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ADSPointLight extends Light
{
    private Vector4f screenPos = new Vector4f();
    private int index;
    private String posName, intensityName;

    public ADSPointLight(Vector3f position, Vector3f intensity, int index, Scene scene)
    {
        super(position, intensity, true, scene);
        this.index = index;

        posName = String.format("lights[%d].Position", index);
        intensityName = String.format("lights[%d].Intensity", index);
    }

    public void bindUniforms(Matrix4f viewMatrix, ShaderProgram shader)
    {
        screenPos.set(pos.x, -pos.y, pos.z, 1);
        screenPos.mul(viewMatrix);

        shader.setUniformVec4(posName, screenPos);
        shader.setUniformVec3(intensityName, intensity);
    }
}
