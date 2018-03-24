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
    private Matrix4f lm, om;
    private Transform parentTransform = null;
    private FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
    private boolean needsBuilt = true;

    public Vector3f pos = new Vector3f(0), scale = new Vector3f(1), rotDeg = new Vector3f(0), rotRad = new Vector3f(0), center = new Vector3f(0);

    public Transform()
    {
        lm = new Matrix4f();
        om = new Matrix4f();

        flagForBuild();
    }

    public Transform(Transform parent)
    {
        lm = new Matrix4f();
        om = new Matrix4f();

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
        if(parent == null) return this;
        parent.addChild(this);
        return this;
    }

    public Transform scale(float x, float y, float z)
    {
        flagForBuild();
        lm.scale(x, y, z);
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

    public Transform scaleCentered(float x, float y, float z)
    {
        flagForBuild();
        lm.scaleAround(x, y, z, center.x, center.y, center.z);
        scale.mul(x, y, z);
        return this;
    }

    public Transform scaleCentered(float a)
    {
        return scaleCentered(a, a, a);
    }

    public Transform scaleCentered(Vector3f a)
    {
        return scaleCentered(a.x, a.y, a.z);
    }

    public Transform setScaleCentered(float x, float y, float z)
    {
        return scaleCentered(x/scale.x, y/scale.y, z/scale.z);
    }

    public Transform setScaleCentered(float a)
    {
        return scaleCentered(a/scale.x, a/scale.y, a/scale.z);
    }

    public Transform setScaleCentered(Vector3f a)
    {
        return scaleCentered(a.x/scale.x, a.y/scale.y, a.z/scale.z);
    }

    public Transform translate(float x, float y, float z)
    {
        flagForBuild();
        lm.translate(x, y, z);
        pos.set(lm.m30(), lm.m31(), lm.m32());

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
        if(x == 0 && y == 0 && z == 0) return this;
        flagForBuild();

        float tx = x/scale.x, ty = y/scale.y, tz = z/scale.z;

        lm.translateLocal(x, y, z);
        pos.set(lm.m30(), lm.m31(), lm.m32());

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
        if(x == 0 && y == 0 && z == 0) return this;
        flagForBuild();

        rotDeg.add(x, y, z);
        rotRad.add(x = x*toRads, y = y*toRads, z = z*toRads);

        lm.rotateXYZ(x, y, z);

        return this;
    }

    public Transform setRotateDegrees(float x, float y, float z)
    {
        if(x == rotDeg.x && y == rotDeg.y && z == rotDeg.z) return this;
        flagForBuild();
        
        rotDeg.set(x, y, z);
        rotRad.set(x = x*toRads, y = y*toRads, z = z*toRads);
        
        lm.setRotationXYZ(x, y, z);
        
        return this;
    }

    public Transform rotateRadians(float x, float y, float z)
    {
        if(x == 0 && y == 0 && z == 0) return this;
        flagForBuild();

        rotRad.add(x, y, z);
        rotDeg.add(x*toDegs, y*toDegs, z*toDegs);

        lm.rotateXYZ(x, y, z);

        return this;
    }

    public Transform setRotateRadians(float x, float y, float z)
    {
        if(x == rotRad.x && y == rotRad.y && z == rotRad.z) return this;
        flagForBuild();
    
        rotRad.set(x, y, z);
        rotDeg.set(x*toDegs, y*toDegs, z*toDegs);
    
        lm.setRotationXYZ(x, y, z);
    
        return this;
    }
    
    public Transform rotateDegreesCentered(float x, float y, float z)
    {
        if(x == 0 && y == 0 && z == 0) return this;
        flagForBuild();

        rotDeg.add(x, y, z);
        rotRad.add(x = x*toRads, y = y*toRads, z = z*toRads);

        quaternion.set(0, 0, 0, 1).rotate(x, y, z);
    
        lm.rotateAround(quaternion, center.x, center.y, center.z);

        return this;
    }

    public Transform setRotateDegreesCentered(float x, float y, float z)
    {
        return rotateDegreesCentered(x - rotDeg.x, y - rotDeg.y, z - rotDeg.z);
    }

    public Transform rotateRadiansCentered(float x, float y, float z)
    {
        if(x == 0 && y == 0 && z == 0) return this;
        flagForBuild();

        rotRad.add(x, y, z);
        rotDeg.add(x*toDegs, y*toDegs, z*toDegs);

        quaternion.set(0, 0, 0, 1).rotate(x, y, z);
    
        lm.rotateAround(quaternion, center.x, center.y, center.z);

        return this;
    }

    public Transform setRotateRadiansCentered(float x, float y, float z)
    {
        return rotateRadiansCentered(x - rotRad.x, y - rotRad.y, z - rotRad.z);
    }

    public Transform setCenter(float x, float y, float z)
    {
        center.set(x, y, z);

        return this;
    }

    public Matrix4f getLocalMatrix()
    {
        /*if(needsBuilt)
        {
            lm.mul(lm, lm);
            lm.mul(lm);
        }*/
        return lm;
    }

    public Matrix4f getRealMatrix()
    {
        if(parentTransform == null) return getLocalMatrix();
        if(needsBuilt) return parentTransform.getRealMatrix().mul(getLocalMatrix(), om);
        else return om;
    }

    public FloatBuffer getBuffer()
    {
        if(needsBuilt) getRealMatrix().get(buffer);
        needsBuilt = false;
        return buffer;
    }

    public Transform getParent()
    {
        return parentTransform;
    }

    public Transform removeChild(Transform child)
    {
        children.remove(child);
        return this;
    }

    public Transform removeFromParent()
    {
        if(parentTransform == null) return this;
        parentTransform.removeChild(this);
        parentTransform = null;
        return this;
    }

    public boolean needsBuilt()
    {
        return needsBuilt;
    }
    
    private void flagForBuild()
    {
        needsBuilt = true;
        children.forEach(Transform::flagForBuild);
    }
}
