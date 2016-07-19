package com.pieisnotpi.engine.rendering.shaders.types.ads_shader;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.shaders.Material;
import org.joml.Vector3f;

public class ADSMaterial extends Material
{
    public Vector3f ka, kd, ks;
    public float s;

    public ADSMaterial(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess)
    {
        super(PiEngine.S_ADS_ID, false, true, true);

        ka = ambient;
        kd = diffuse;
        ks = specular;
        s = shininess;
    }
}
