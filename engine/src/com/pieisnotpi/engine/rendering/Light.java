package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public abstract class Light
{
    public Vector3f pos, intensity;
    public boolean castsShadows;
    public Camera shadowBuffer;
    public Scene scene;

    public Light(Vector3f position, Vector3f intensity, boolean castsShadows, Scene scene)
    {
        this.pos = position;
        this.intensity = intensity;
        this.castsShadows = castsShadows;

        //if(castsShadows) shadowBuffer = new Camera(pos, pos.sub(0, 10, 0, new Vector3f()), 0, scene).setFramebufferRes(new Vector2i(2048, 2048));
    }

    public abstract void bindUniforms(Matrix4f viewMatrix, ShaderProgram shader);
}
