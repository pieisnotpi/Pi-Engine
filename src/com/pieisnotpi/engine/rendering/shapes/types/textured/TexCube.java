package com.pieisnotpi.engine.rendering.shapes.types.textured;

import com.pieisnotpi.engine.rendering.shapes.Cube;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.scene.Scene;

public class TexCube extends Cube
{
    public TexCube(float x, float y, float z, float width, float height, float depth, Sprite topSprite, Sprite sideSprite, Sprite bottomSprite, int shaderID, int matrixID, Scene scene)
    {
        super();

        this.shaderID = shaderID;
        this.matrixID = matrixID;
        this.scene = scene;

        sides[0] = new TexQuad(x, y, z, width, height, 0, sideSprite, shaderID, matrixID, scene);
        sides[1] = new TexQuad(x, y, z, 0, height, depth, sideSprite, shaderID, matrixID, scene);
        sides[2] = new TexQuad(x + width, y, z, 0, height, depth, sideSprite, shaderID, matrixID, scene);
        sides[3] = new TexQuad(x + width, y, z + depth, -width, height, 0, sideSprite, shaderID, matrixID, scene);
        sides[4] = new TexQuad(x, y + height, z, width, 0, depth, topSprite, shaderID, matrixID, scene);
        sides[5] = new TexQuad(x, y, z, width, 0, depth, bottomSprite, shaderID, matrixID, scene);

        register();
    }
}
