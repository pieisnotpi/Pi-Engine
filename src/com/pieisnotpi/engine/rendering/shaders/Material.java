package com.pieisnotpi.engine.rendering.shaders;

public class Material
{
    public int shaderID;
    public boolean usesColors, usesNormals, usesTexCoords;

    public Material(int shaderID, boolean usesColors, boolean usesNormals, boolean usesTexCoords)
    {
        this.shaderID = shaderID;
        this.usesColors = usesColors;
        this.usesNormals = usesNormals;
        this.usesTexCoords = usesTexCoords;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean t = super.equals(obj);
        if(t) return true;

        if(!getClass().equals(obj.getClass())) return false;
        Material m = (Material) obj;
        return (shaderID == m.shaderID && usesColors == m.usesColors && usesNormals == m.usesNormals && usesTexCoords == m.usesTexCoords);
    }
}
