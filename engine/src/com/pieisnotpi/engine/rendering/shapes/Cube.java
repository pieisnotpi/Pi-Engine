package com.pieisnotpi.engine.rendering.shapes;

import com.pieisnotpi.engine.scene.Scene;

public class Cube
{
    public Quad[] sides = new Quad[6];
    public Scene scene;
    public int matrixID;

    protected Cube() {}

    public Cube(float x, float y, float z, float width, float height, float depth)
    {
        sides[0] = new Quad(x, y, z, width, height, 0);
        sides[1] = new Quad(x, y, z, 0, height, depth);
        sides[2] = new Quad(x, y, z, 0, height, depth);
        sides[3] = new Quad(x, y, z + depth, width, height, 0);
        sides[4] = new Quad(x, y + height, z, width, 0, depth);
        sides[5] = new Quad(x, y, z, width, 0, depth);
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
}
