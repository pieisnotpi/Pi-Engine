package com.pieisnotpi.engine.rendering.shapes;

import com.pieisnotpi.engine.rendering.renderable_types.Renderable;
import com.pieisnotpi.engine.scene.Scene;

public class Cube extends Renderable
{
    public Quad[] sides = new Quad[6];

    protected Cube() {}

    public Cube(float x, float y, float z, float width, float height, float depth, int shaderID, int matrixID, Scene scene)
    {
        sides[0] = new Quad(x, y, z, width, height, 0, shaderID, matrixID, scene);
        sides[1] = new Quad(x, y, z, 0, height, depth, shaderID, matrixID, scene);
        sides[2] = new Quad(x, y, z, 0, height, depth, shaderID, matrixID, scene);
        sides[3] = new Quad(x, y, z + depth, width, height, 0, shaderID, matrixID, scene);
        sides[4] = new Quad(x, y + height, z, width, 0, depth, shaderID, matrixID, scene);
        sides[5] = new Quad(x, y, z, width, 0, depth, shaderID, matrixID, scene);

        this.scene = scene;
        this.shaderID = shaderID;
        this.matrixID = matrixID;
    }

    public void setX(float x)
    {
        for(Quad side : sides)
        {
            float temp = side.getX() - sides[0].getX();
            side.setX(x + temp);
        }
    }

    public void setY(float y)
    {
        for(Quad side : sides)
        {
            float temp = side.getY() - sides[0].getY();
            side.setY(y + temp);
        }
    }

    public void setZ(float z)
    {
        for(Quad side : sides)
        {
            float temp = side.getZ() - sides[0].getZ();
            side.setZ(z + temp);
        }
    }

    public void register()
    {
        for(Quad side : sides) side.register();
    }

    public void unregister()
    {
        for(Quad side : sides) side.unregister();
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        unregister();
    }
}
