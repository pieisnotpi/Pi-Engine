package com.pieisnotpi.engine.rendering.mesh;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.pieisnotpi.engine.utility.MathUtility.toDegs;
import static com.pieisnotpi.engine.utility.MathUtility.toRads;

public class Transform
{
    private Quaternionf quaternion = new Quaternionf();
    private List<Transform> children = new ArrayList<>();
    private Matrix4f localMatrix, outputMatrix;
    private Transform parentTransform = null;
    private FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
    private boolean needsBuilt = true;

    public Vector3f pos = new Vector3f(0), scale = new Vector3f(1), rotDeg = new Vector3f(0), rotRad = new Vector3f(0), center = new Vector3f(0);

    public Transform()
    {
        localMatrix = new Matrix4f();
        outputMatrix = new Matrix4f();

        flagForBuild();
    }

    public Transform(Transform parent)
    {
        localMatrix = new Matrix4f();
        outputMatrix = new Matrix4f();

        setParent(parent);
    }

    public Transform addChild(Transform child)
    {
        child.parentTransform = this;
        child.flagForBuild();
        children.add(child);

        return this;
    }

    public Transform setParent(Transform parent)
    {
        parent.addChild(this);
        return this;
    }

    public Transform scale(float x, float y, float z)
    {
        flagForBuild();
        localMatrix.scale(x, y, z);
        scale.mul(x, y, z);

        return this;
    }

    public Transform scale(float a)
    {
        return scale(a, a, a);
    }

    public Transform scale(Vector3f a)
    {
        return scale(a.x, a.y, a.z);
    }

    public Transform setScale(float x, float y, float z)
    {
        return scale(x/scale.x, y/scale.y, z/scale.z);
    }

    public Transform setScale(float a)
    {
        return scale(a/scale.x, a/scale.y, a/scale.z);
    }

    public Transform setScale(Vector3f a)
    {
        return scale(a.x/scale.x, a.y/scale.y, a.z/scale.z);
    }

    public Transform translate(float x, float y, float z)
    {
        flagForBuild();
        localMatrix.translate(x, y, z);
        pos.add(x, y, z);

        return this;
    }

    public Transform translate(float a)
    {
        return translate(a, a, a);
    }

    public Transform translate(Vector3f a)
    {
        return translate(a.x, a.y, a.z);
    }

    public Transform setTranslate(float x, float y, float z)
    {
        return translate(x - pos.x, y - pos.y, z - pos.z);
    }

    public Transform setTranslate(float a)
    {
        return translate(a - pos.x, a - pos.y, a - pos.z);
    }

    public Transform setTranslate(Vector3f a)
    {
        return translate(a.x - pos.x, a.y - pos.y, a.z - pos.z);
    }

    public Transform translateAbs(float x, float y, float z)
    {
        flagForBuild();
        localMatrix.translate(x/scale.x, y/scale.y, z/scale.z);
        pos.add(x, y, z);

        return this;
    }

    public Transform translateAbs(float a)
    {
        return translateAbs(a, a, a);
    }

    public Transform translateAbs(Vector3f a)
    {
        return translateAbs(a.x, a.y, a.z);
    }

    public Transform setTranslateAbs(float x, float y, float z)
    {
        return translateAbs(x - pos.x, y - pos.y, z - pos.z);
    }

    public Transform setTranslateAbs(float a)
    {
        return translateAbs(a - pos.x, a - pos.y, a - pos.z);
    }

    public Transform setTranslateAbs(Vector3f a)
    {
        return translateAbs(a.x - pos.x, a.y - pos.y, a.z - pos.z);
    }

    public Transform rotateDegrees(float x, float y, float z)
    {
        flagForBuild();

        rotDeg.add(x, y, z);
        rotRad.add(x = x*toRads, y = y*toRads, z = z*toRads);

        quaternion.set(0, 0, 0, 1).rotate(x, y, z);

        localMatrix.rotateAround(quaternion, center.x, center.y, center.z);

        return this;
    }

    public Transform setRotateDegrees(float x, float y, float z)
    {
        return rotateDegrees(x - rotDeg.x, y - rotDeg.y, z - rotDeg.z);
    }

    public Transform rotateRadians(float x, float y, float z)
    {
        flagForBuild();

        rotRad.add(x, y, z);
        rotDeg.add(x*toDegs, y*toDegs, z*toDegs);

        quaternion.set(0, 0, 0, 1).rotate(x, y, z);

        localMatrix.rotateAround(quaternion, center.x, center.y, center.z);

        return this;
    }

    public Transform setRotateRadians(float x, float y, float z)
    {
        return rotateRadians(x - rotRad.x, y - rotRad.y, z - rotRad.z);
    }

    public Transform setCenter(float x, float y, float z)
    {
        center.set(x, y, z);

        return this;
    }

    public Matrix4f getLocalMatrix()
    {
        return localMatrix;
    }

    public Matrix4f getRealMatrix()
    {
        if(parentTransform == null) return localMatrix;
        if(needsBuilt) return parentTransform.getRealMatrix().mul(localMatrix, outputMatrix);

        else return outputMatrix;
    }

    public FloatBuffer getBuffer()
    {
        if(needsBuilt) getRealMatrix().get(buffer);
        needsBuilt = false;
        return buffer;
    }

    private void flagForBuild()
    {
        needsBuilt = true;
        children.forEach(c -> c.needsBuilt = true);
    }
}
